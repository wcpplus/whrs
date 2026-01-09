package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.AuthPtype;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**角色类别 
 * @author cbtg自动生成  2025-10-29 11:55:20 
 */
@Repository
public class AuthPtypeDao extends SuperDao {
    @Autowired
    private AuthPtypeMapper authPtypeMapper;
    private static String TABLE_NAME = "FARM2_AUTH_PTYPE";
    private static String PRIMARY_KEY = "ID";


    public AuthPtype findById(String id) {
        return authPtypeMapper.findById(TABLE_NAME, id);
    }

    public List<AuthPtype> findAll() {
        return authPtypeMapper.findAll(TABLE_NAME);
    }

    public void insert(AuthPtype authPtype) {
        insert(authPtypeMapper, authPtype, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(AuthPtype authPtype) {
        update(authPtypeMapper, authPtype, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        authPtypeMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        authPtypeMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(authPtypeMapper, query, generateSelectFields(AuthPtype.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public AuthPtype queryOne(List<QueryRule> rules) {
        return queryOne(authPtypeMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(AuthPtype.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), AuthPtype.class);
    }
    public int countData(DataQuery query) {
        return countData(authPtypeMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(AuthPtype.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(authPtypeMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = authPtypeMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<AuthPtype> find(List<QueryRule> rules) {
        return authPtypeMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<AuthPtype> findSubNodes(String id) {
        return authPtypeMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<AuthPtype> findByParentId(String parentId) {
        return authPtypeMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
