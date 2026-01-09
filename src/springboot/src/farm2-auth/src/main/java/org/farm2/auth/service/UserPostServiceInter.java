package org.farm2.auth.service;

import org.farm2.auth.domain.UserPost;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**用户岗位 
 * @author cbtg自动生成  2025-5-10 22:11:54 
 */
public interface UserPostServiceInter {

    public UserPost insertUserPostEntity(UserPost userPost);

    public UserPost editUserPostEntity(UserPost userPost);

    public void delUserPost(String id);

    public UserPost getUserPostById(String id);

    public List<UserPost> getUserPosts(DataQuery query);

    public DataResult searchUserPost(DataQuery query);

    public int getUserPostNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
