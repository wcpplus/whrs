package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.PostActions;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色权限
 *
 * @author cbtg自动生成  2025-1-7 10:07:59
 */
@Repository
public class PostActionsDao extends SuperDao {
    @Autowired
    private PostActionsMapper postActionsMapper;
    private static String TABLE_NAME = "FARM2_AUTH_POSTACTION";
    private static String PRIMARY_KEY = "ID";


    public PostActions findById(String id) {
        return postActionsMapper.findById(TABLE_NAME, id);
    }

    public List<PostActions> findAll() {
        return postActionsMapper.findAll(TABLE_NAME);
    }

    public void insert(PostActions postActions) {
        insert(postActionsMapper, postActions, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(PostActions postActions) {
        update(postActionsMapper, postActions, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        postActionsMapper.deleteById(TABLE_NAME, id);
    }

    public void delete(List<QueryRule> rules) {
        postActionsMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(postActionsMapper, query, generateSelectFields(PostActions.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public PostActions queryOne(List<QueryRule> rules) {
        return queryOne(postActionsMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(PostActions.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), PostActions.class);
    }

    public int countData(DataQuery query) {
        return countData(postActionsMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = postActionsMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }

    public List<String> findByPostId(String postId) {
        return postActionsMapper.findActionsByPostId(TABLE_NAME, postId);
    }

    public List<String> findByPostIds(List<String> postIds) {
        if (postIds.size() <= 0) {
            return new ArrayList<>();
        }
        return postActionsMapper.findActionsByPostIds(TABLE_NAME, postIds);
    }

    /**[tree：树形结构使用]
     public List<PostActions> findSubNodes(String id) {
     return postActionsMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
     }
     */
    /**[tree：树形结构使用]
     public List<PostActions> findByParentId(String parentId) {
     return postActionsMapper.findByParentId(TABLE_NAME, parentId);
     }
     */
}
