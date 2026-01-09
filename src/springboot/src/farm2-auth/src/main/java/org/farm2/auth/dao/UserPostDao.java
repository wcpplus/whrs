package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.UserPost;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户角色 
 * @author cbtg自动生成  2025-1-7 12:31:51 
 */
@Repository
public class UserPostDao extends SuperDao {
    @Autowired
    private UserPostMapper userPostMapper;
    private static String TABLE_NAME = "FARM2_AUTH_USERPOST";
    private static String PRIMARY_KEY = "ID";

    public List<UserPost> find(List<QueryRule> rules) {
        return userPostMapper.find(TABLE_NAME, rules);
    }
    public UserPost findById(String id) {
        return userPostMapper.findById(TABLE_NAME, id);
    }

    public List<UserPost> findAll() {
        return userPostMapper.findAll(TABLE_NAME);
    }

    public void insert(UserPost userPost) {
        insert(userPostMapper, userPost, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(UserPost userPost) {
        update(userPostMapper, userPost, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        userPostMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        userPostMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(userPostMapper, query, generateSelectFields(UserPost.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public DataResult queryPostUserData(DataQuery query) {
        return queryData(userPostMapper, query, generateSelectFields(UserPost.class, "A")
                        .ignore("PASSWORD")//
                        .getTitles() + ",B.NAME AS USERNAME", //
                addAlias(TABLE_NAME, "A") + " LEFT JOIN FARM2_LOCAL_USER B ON A.USERKEY=B.LOGINNAME");
    }

    public UserPost queryOne(List<QueryRule> rules) {
        return queryOne(userPostMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(UserPost.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), UserPost.class);
    }
    public int countData(DataQuery query) {
        return countData(userPostMapper, query, addAlias(TABLE_NAME, "A"));
    }

    /**
     * 字段field上是否存在值value，id为id的记录除外（可以判断数据库中某个key是否存在的业务）
     *
     * @param excludeId 排除的id
     * @param fieldName
     * @param value
     * @return
     */
    public boolean isExist(String excludeId, String fieldName, String value) {
        if (StringUtils.isBlank(excludeId)) {
            excludeId = FarmUUID.getUUID32();
        }
        int n = userPostMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }

    public List<String> findPostIdsByUserKey(String loginname) {
        return userPostMapper.findPostIdsByUserKey(TABLE_NAME, loginname);
    }
    /**[tree：树形结构使用]
    public List<UserPost> findSubNodes(String id) {
        return userPostMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<UserPost> findByParentId(String parentId) {
        return userPostMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
