package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.AuthPostPtype;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**角色类别关系 
 * @author cbtg自动生成  2025-10-29 11:56:43 
 */
@Repository
public class AuthPostPtypeDao extends SuperDao {
    @Autowired
    private AuthPostPtypeMapper authPostPtypeMapper;
    private static String TABLE_NAME = "FARM2_AUTH_POST_PTYPE";
    private static String PRIMARY_KEY = "ID";


    public AuthPostPtype findById(String id) {
        return authPostPtypeMapper.findById(TABLE_NAME, id);
    }

    public List<AuthPostPtype> findAll() {
        return authPostPtypeMapper.findAll(TABLE_NAME);
    }

    public void insert(AuthPostPtype authPostPtype) {
        insert(authPostPtypeMapper, authPostPtype, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(AuthPostPtype authPostPtype) {
        update(authPostPtypeMapper, authPostPtype, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        authPostPtypeMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        authPostPtypeMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(authPostPtypeMapper, query, generateSelectFields(AuthPostPtype.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public AuthPostPtype queryOne(List<QueryRule> rules) {
        return queryOne(authPostPtypeMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(AuthPostPtype.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), AuthPostPtype.class);
    }
    public int countData(DataQuery query) {
        return countData(authPostPtypeMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(AuthPostPtype.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(authPostPtypeMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = authPostPtypeMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<AuthPostPtype> find(List<QueryRule> rules) {
        return authPostPtypeMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<AuthPostPtype> findSubNodes(String id) {
        return authPostPtypeMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<AuthPostPtype> findByParentId(String parentId) {
        return authPostPtypeMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
