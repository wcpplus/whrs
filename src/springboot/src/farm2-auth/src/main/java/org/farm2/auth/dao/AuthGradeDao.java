package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.AuthGrade;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**职级 
 * @author cbtg自动生成  2026-1-5 11:41:21 
 */
@Repository
public class AuthGradeDao extends SuperDao {
    @Autowired
    private AuthGradeMapper authGradeMapper;
    private static String TABLE_NAME = "FARM2_AUTH_GRADE";
    private static String PRIMARY_KEY = "ID";


    public AuthGrade findById(String id) {
        return authGradeMapper.findById(TABLE_NAME, id);
    }

    public List<AuthGrade> findAll() {
        return authGradeMapper.findAll(TABLE_NAME);
    }

    public void insert(AuthGrade authGrade) {
        insert(authGradeMapper, authGrade, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(AuthGrade authGrade) {
        update(authGradeMapper, authGrade, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        authGradeMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        authGradeMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(authGradeMapper, query, generateSelectFields(AuthGrade.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public AuthGrade queryOne(List<QueryRule> rules) {
        return queryOne(authGradeMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(AuthGrade.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), AuthGrade.class);
    }
    public int countData(DataQuery query) {
        return countData(authGradeMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(AuthGrade.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(authGradeMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = authGradeMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<AuthGrade> find(List<QueryRule> rules) {
        return authGradeMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<AuthGrade> findSubNodes(String id) {
        return authGradeMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<AuthGrade> findByParentId(String parentId) {
        return authGradeMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
