package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.AuthFamily;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**岗位族 
 * @author cbtg自动生成  2026-1-5 10:12:10 
 */
@Repository
public class AuthFamilyDao extends SuperDao {
    @Autowired
    private AuthFamilyMapper authFamilyMapper;
    private static String TABLE_NAME = "FARM2_AUTH_FAMILY";
    private static String PRIMARY_KEY = "ID";


    public AuthFamily findById(String id) {
        return authFamilyMapper.findById(TABLE_NAME, id);
    }

    public List<AuthFamily> findAll() {
        return authFamilyMapper.findAll(TABLE_NAME);
    }

    public void insert(AuthFamily authFamily) {
        insert(authFamilyMapper, authFamily, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(AuthFamily authFamily) {
        update(authFamilyMapper, authFamily, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        authFamilyMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        authFamilyMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(authFamilyMapper, query, generateSelectFields(AuthFamily.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public AuthFamily queryOne(List<QueryRule> rules) {
        return queryOne(authFamilyMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(AuthFamily.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), AuthFamily.class);
    }
    public int countData(DataQuery query) {
        return countData(authFamilyMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(AuthFamily.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(authFamilyMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = authFamilyMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<AuthFamily> find(List<QueryRule> rules) {
        return authFamilyMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<AuthFamily> findSubNodes(String id) {
        return authFamilyMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<AuthFamily> findByParentId(String parentId) {
        return authFamilyMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
