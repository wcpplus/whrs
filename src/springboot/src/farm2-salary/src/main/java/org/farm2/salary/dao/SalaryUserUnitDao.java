package org.farm2.salary.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.salary.domain.SalaryUserUnit;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户薪资 
 * @author cbtg自动生成  2026-1-8 14:47:36 
 */
@Repository
public class SalaryUserUnitDao extends SuperDao {
    @Autowired
    private SalaryUserUnitMapper salaryUserUnitMapper;
    private static String TABLE_NAME = "WHRS_SALARY_USER_UNIT";
    private static String PRIMARY_KEY = "ID";


    public SalaryUserUnit findById(String id) {
        return salaryUserUnitMapper.findById(TABLE_NAME, id);
    }

    public List<SalaryUserUnit> findAll() {
        return salaryUserUnitMapper.findAll(TABLE_NAME);
    }

    public void insert(SalaryUserUnit salaryUserUnit) {
        insert(salaryUserUnitMapper, salaryUserUnit, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(SalaryUserUnit salaryUserUnit) {
        update(salaryUserUnitMapper, salaryUserUnit, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        salaryUserUnitMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        salaryUserUnitMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(salaryUserUnitMapper, query, generateSelectFields(SalaryUserUnit.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public SalaryUserUnit queryOne(List<QueryRule> rules) {
        return queryOne(salaryUserUnitMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(SalaryUserUnit.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), SalaryUserUnit.class);
    }
    public int countData(DataQuery query) {
        return countData(salaryUserUnitMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(SalaryUserUnit.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(salaryUserUnitMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = salaryUserUnitMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<SalaryUserUnit> find(List<QueryRule> rules) {
        return salaryUserUnitMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<SalaryUserUnit> findSubNodes(String id) {
        return salaryUserUnitMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<SalaryUserUnit> findByParentId(String parentId) {
        return salaryUserUnitMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
