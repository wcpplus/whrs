package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.dao.PostActionsDao;
import org.farm2.auth.dao.UserPostDao;
import org.farm2.auth.domain.PostActions;
import org.farm2.auth.domain.UserPost;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmPost;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.auth.dao.PostDao;
import org.farm2.auth.domain.Post;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.caches.handle.FarmCachesCreator;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 角色
 *
 * @author cbtg自动生成  2025-1-6 15:10:13
 */
@Service
@Slf4j
public class PostServiceImpl implements PostServiceInter {
    @Autowired
    private PostActionsDao postActionsDao;
    @Autowired
    private UserPostDao userPostDao;
    @Autowired
    private PostDao postDao;

    @Transactional
    @Override
    public Post insertPostEntity(Post post) {
        FarmDbFields.initInsertBean(post, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(post.getType(), "1", post::setType);
        postDao.insert(post);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return post;
    }

    @Transactional
    @Override
    public Post editPostEntity(Post post) {
        Post savePost = getPostById(post.getId());
        FarmExceptionUtils.throwNullEx(savePost, I18n.msg("角色不存在:?", post.getId()));
        savePost.setState(post.getState());
        savePost.setNote(post.getNote());
        savePost.setFamilyid(post.getFamilyid());
        savePost.setGradeid(post.getGradeid());
        savePost.setTracktype(post.getTracktype());
        savePost.setMaxunum(post.getMaxunum());
        savePost.setOrgid(post.getOrgid());
        savePost.setName(post.getName());
        savePost.setKeyid(post.getKeyid());
        FarmDbFields.initUpdateBean(savePost, FarmUserContextLoader.getCurrentUser());
        postDao.update(savePost);
        return savePost;
    }

    @Transactional
    @Override
    public Post getPostById(String id) {
        return postDao.findById(id);
    }

    @Override
    public List<Post> getPosts(DataQuery query) {
        return postDao.queryData(query.setCount(false)).getData(Post.class);
    }


    @Transactional
    @Override
    public DataResult searchPost(DataQuery query) {
        DataResult result = postDao.queryData(query);
        return result;
    }

    @Override
    public int getPostNum(DataQuery query) {
        return postDao.countData(query);
    }


    @Transactional
    @Override
    public void delPost(String id) {
        //删除岗位菜单
        postActionsDao.delete(DBRule.getInstance("POSTID", id, "=").getRules());
        //删除岗位用户
        userPostDao.delete(DBRule.getInstance("POSTID", id, "=").getRules());
        postDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return postDao.countData(query);
    }

    @Transactional
    @Override
    public void savePostActions(String postid, List<String> actionsids) {
        postActionsDao.delete(DBRule.getInstance("POSTID", postid, "=").getRules());
        for (String actionsid : actionsids) {
            PostActions pactions = new PostActions();
            pactions.setActionsid(actionsid);
            pactions.setPostid(postid);
            postActionsDao.insert(pactions);
        }
    }

    @Override
    public List<String> findActions(String postId) {
        List<String> actions = postActionsDao.findByPostId(postId);
        return actions;
    }

    @Transactional
    @Override
    public void savePostUser(String loginname, List<String> posts) {
        userPostDao.delete(DBRule.getInstance("USERKEY", loginname, "=").getRules());
        if (posts != null) {
            for (String postid : posts) {
                UserPost spost = new UserPost();
                spost.setUserkey(loginname);
                spost.setPostid(postid);
                userPostDao.insert(spost);
            }
        }
    }

    @Transactional
    @Override
    public void savePostUser(List<String> userkeys, String postid) {
        for (String loginname : userkeys) {
            userPostDao.delete(DBRuleList.getInstance()//
                    .add(new DBRule("USERKEY", loginname, "="))//
                    .add(new DBRule("POSTID", postid, "="))//
                    .toList());
            UserPost spost = new UserPost();
            spost.setUserkey(loginname);
            spost.setPostid(postid);
            userPostDao.insert(spost);
        }
    }

    @Override
    public List<String> getUserPostIds(String loginname) {
        DataResult result = userPostDao.queryData(DataQuery.getInstance().setPageSizeAll().setCount(false).addRule(new DBRule("USERKEY", loginname, "=")));
        List<String> ids = result.getData().stream().map(new Function<Map<String, Object>, String>() {
            @Override
            public String apply(Map<String, Object> stringObjectMap) {
                return (String) stringObjectMap.get("POSTID");
            }
        }).toList();
        return ids;
    }

    @Override
    public List<FarmPost> getUserPosts(String loginname, boolean isCache) {
        FarmCachesCreator creator = new FarmCachesCreator() {
            @Override
            public Object handle(String key) {
                List<Post> posts = new ArrayList<>();
                List<FarmPost> fposts = new ArrayList<>();
                Set<String> postIds = new HashSet<>();
                for (String postid : getUserPostIds(loginname)) {
                    Post post = postDao.findById(postid);
                    if (post != null) {
                        posts.add(post);
                    }
                }
                for (Post post : posts) {
                    if (!postIds.contains(post.getId())) {
                        postIds.add(post.getId());
                        FarmPost fpost = new FarmPost(post.getKeyid(), post.getName(), post.getId());
                        fposts.add(fpost);
                    }
                }
                return fposts;
            }
        };
        return FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.USER_POSTS, creator, isCache);
    }

    @Override
    public Post getPostByKey(String key) {
        List<Post> posts = postDao.findByKeyid(key);
        return posts.size() > 0 ? posts.get(0) : null;
    }

    @Override
    public int getPostUserNum(String postId) {
        return userPostDao.countData(new DataQuery().addRule(new DBRule("POSTID", postId, "=")));
    }

    @Override
    public Set<String> getUserkeysByPostKey(String postkey) {
        Post post = getPostByKey(postkey);
        return userPostDao.
                find(DBRuleList.getInstance()
                        .add(new DBRule("POSTID", post.getId(), "="))
                        .toList()).stream().map(node -> node.getUserkey()).collect(Collectors.toSet());
    }

    @Override
    public Set<String> getUserkeysByPostId(String postid) {
        return userPostDao.
                find(DBRuleList.getInstance()
                        .add(new DBRule("POSTID", postid, "="))
                        .toList()).stream().map(node -> node.getUserkey()).collect(Collectors.toSet());
    }

    @Override
    public List<Post> getUserPost(String loginname) {
        Set<String> postIds = userPostDao.
                find(DBRuleList.getInstance()
                        .add(new DBRule("USERKEY", loginname, "="))
                        .toList()).stream().map(node -> node.getPostid()).collect(Collectors.toSet());
        List<Post> posts = postDao.find(DBRuleList.getInstance().add(new WhereInRule("ID", postIds)).toList());
        return posts;
    }

}
