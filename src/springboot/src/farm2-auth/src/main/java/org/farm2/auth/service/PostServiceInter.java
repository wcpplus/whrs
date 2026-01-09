package org.farm2.auth.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.farm2.auth.domain.Post;
import org.farm2.base.domain.FarmPost;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;
import java.util.Set;

/**角色 
 * @author cbtg自动生成  2025-1-6 15:10:13 
 */
public interface PostServiceInter {

    public Post insertPostEntity(Post post);

    public Post editPostEntity(Post post);

    public void delPost(String id);

    public Post getPostById(String id);

    public List<Post> getPosts(DataQuery query);

    public DataResult searchPost(DataQuery query);

    public int getPostNum(DataQuery query);
    
    public int getNum(DataQuery query);

    /**
     * 保存角色的权限设置
     *
     * @param postid
     * @param actionsids
     */
    public void savePostActions(String postid, List<String> actionsids);

    /**
     * 获得岗位对应权限ids
     *
     * @param postId
     * @return
     */
    public List<String> findActions(@Valid String postId);

    /**
     * 保存用户岗位
     *
     * @param post
     * @param loginname
     */
    public void savePostUser(String loginname, List<String> post);

    /**
     * 保存岗位用户
     *
     * @param userkeys
     * @param postid
     */
    public void savePostUser(List<String> userkeys, String postid);

    /**
     * 获得用户岗位
     *
     * @param loginname
     * @return
     */
    public List<String> getUserPostIds(String loginname);

    /**
     * 获得用户岗位
     *
     * @param loginname
     * @return
     */
    public List<FarmPost> getUserPosts(String loginname,boolean isCache);

    /**
     * 通过岗位key查找岗位
     *
     * @param key
     * @return
     */
    public Post getPostByKey(String key);

    /**
     * 获得用户数量
     *
     * @param postId
     * @return
     */
    public int getPostUserNum(String postId);

    public Set<String> getUserkeysByPostKey(String postkey);
    public Set<String> getUserkeysByPostId(String postkey);

    public List<Post> getUserPost(String loginname);
}
