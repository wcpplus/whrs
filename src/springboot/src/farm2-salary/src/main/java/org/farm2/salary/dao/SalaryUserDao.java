package org.farm2.salary.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.salary.domain.SalaryUser;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户薪酬WHRS_SALARY_USER 
 * @author cbtg自动生成  2026-1-8 11:14:25 
 */
@Repository
public class SalaryUserDao extends SuperDao {
    @Autowired
    private SalaryUserMapper salaryUserMapper;
    private static String TABLE_NAME = "WHRS_SALARY_USER";
    private static String PRIMARY_KEY = "ID";


    public SalaryUser findById(String id) {
        return salaryUserMapper.findById(TABLE_NAME, id);
    }

    public List<SalaryUser> findAll() {
        return salaryUserMapper.findAll(TABLE_NAME);
    }

    public void insert(SalaryUser salaryUser) {
        insert(salaryUserMapper, salaryUser, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(SalaryUser salaryUser) {
        update(salaryUserMapper, salaryUser, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        salaryUserMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        salaryUserMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(salaryUserMapper, query, generateSelectFields(SalaryUser.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public SalaryUser queryOne(List<QueryRule> rules) {
        return queryOne(salaryUserMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(SalaryUser.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), SalaryUser.class);
    }
    public int countData(DataQuery query) {
        return countData(salaryUserMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(SalaryUser.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(salaryUserMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = salaryUserMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<SalaryUser> find(List<QueryRule> rules) {
        return salaryUserMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<SalaryUser> findSubNodes(String id) {
        return salaryUserMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<SalaryUser> findByParentId(String parentId) {
        return salaryUserMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
