package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.auth.dao.UserPostDao;
import org.farm2.auth.domain.UserPost;
import org.farm2.auth.service.UserPostServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**用户岗位 
 * @author cbtg自动生成  2025-5-10 22:11:54 
 */
@Service
@Slf4j
public class UserPostServiceImpl implements UserPostServiceInter {


    @Autowired
    private UserPostDao userPostDao;

    @Transactional
    @Override
    public UserPost insertUserPostEntity(UserPost userPost) {
        FarmDbFields.initInsertBean(userPost, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(userPost.getType(), "1", userPost::setType);
        userPostDao.insert(userPost);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return userPost;
    }

    @Transactional
    @Override
    public UserPost editUserPostEntity(UserPost userPost) {
        UserPost saveUserPost = getUserPostById(userPost.getId());
        FarmExceptionUtils.throwNullEx(saveUserPost, I18n.msg("用户岗位不存在:?", userPost.getId()));
        saveUserPost.setId(userPost.getId());
        saveUserPost.setUserkey(userPost.getUserkey());
        saveUserPost.setPostid(userPost.getPostid());
         
        FarmDbFields.initUpdateBean(saveUserPost, FarmUserContextLoader.getCurrentUser());
        userPostDao.update(saveUserPost);
        return saveUserPost;
    }

    @Transactional
    @Override
    public UserPost getUserPostById(String id) {
        return userPostDao.findById(id);
    }

    @Override
    public List<UserPost> getUserPosts(DataQuery query) {
        return userPostDao.queryData(query.setCount(false)).getData(UserPost.class);
    }



    @Transactional
    @Override
    public DataResult searchUserPost(DataQuery query) {
        DataResult result = userPostDao.queryPostUserData(query);
        return result;
    }

    @Override
    public int getUserPostNum(DataQuery query) {
        return userPostDao.countData(query);
    }


    @Transactional
    @Override
    public void delUserPost(String id) {
        /*[tree：树形结构使用]
        if ( userPostDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        userPostDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  userPostDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            UserPost node = getUserPostById(sourceId);
            if (!"NONE".equals(targetId)) {
                UserPost target = getUserPostById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            userPostDao.update(node);
            // 构造所有树TREECODE
            List<UserPost> list = userPostDao.findSubNodes(sourceId);
            for (UserPost treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        UserPost node = userPostDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(userPostDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        userPostDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            UserPost node =  userPostDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            userPostDao.update(node);
        }
    }*/
}
