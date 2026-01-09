package org.farm2.salary.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.salary.domain.SalaryUserBase;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户薪酬定义 
 * @author cbtg自动生成  2026-1-8 14:45:42 
 */
@Repository
public class SalaryUserBaseDao extends SuperDao {
    @Autowired
    private SalaryUserBaseMapper salaryUserBaseMapper;
    private static String TABLE_NAME = "WHRS_SALARY_USER_BASE";
    private static String PRIMARY_KEY = "ID";


    public SalaryUserBase findById(String id) {
        return salaryUserBaseMapper.findById(TABLE_NAME, id);
    }

    public List<SalaryUserBase> findAll() {
        return salaryUserBaseMapper.findAll(TABLE_NAME);
    }

    public void insert(SalaryUserBase salaryUserBase) {
        insert(salaryUserBaseMapper, salaryUserBase, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(SalaryUserBase salaryUserBase) {
        update(salaryUserBaseMapper, salaryUserBase, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        salaryUserBaseMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        salaryUserBaseMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(salaryUserBaseMapper, query, generateSelectFields(SalaryUserBase.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public SalaryUserBase queryOne(List<QueryRule> rules) {
        return queryOne(salaryUserBaseMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(SalaryUserBase.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), SalaryUserBase.class);
    }
    public int countData(DataQuery query) {
        return countData(salaryUserBaseMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(SalaryUserBase.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(salaryUserBaseMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = salaryUserBaseMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<SalaryUserBase> find(List<QueryRule> rules) {
        return salaryUserBaseMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<SalaryUserBase> findSubNodes(String id) {
        return salaryUserBaseMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<SalaryUserBase> findByParentId(String parentId) {
        return salaryUserBaseMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
