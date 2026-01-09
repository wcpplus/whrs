package org.farm2.salary.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.salary.domain.SalaryTemplate;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**薪酬模板 
 * @author cbtg自动生成  2026-1-7 10:20:27 
 */
@Repository
public class SalaryTemplateDao extends SuperDao {
    @Autowired
    private SalaryTemplateMapper salaryTemplateMapper;
    private static String TABLE_NAME = "WHRS_SALARY_TEMPLATE";
    private static String PRIMARY_KEY = "ID";


    public SalaryTemplate findById(String id) {
        return salaryTemplateMapper.findById(TABLE_NAME, id);
    }

    public List<SalaryTemplate> findAll() {
        return salaryTemplateMapper.findAll(TABLE_NAME);
    }

    public void insert(SalaryTemplate salaryTemplate) {
        insert(salaryTemplateMapper, salaryTemplate, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(SalaryTemplate salaryTemplate) {
        update(salaryTemplateMapper, salaryTemplate, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        salaryTemplateMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        salaryTemplateMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(salaryTemplateMapper, query, generateSelectFields(SalaryTemplate.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public SalaryTemplate queryOne(List<QueryRule> rules) {
        return queryOne(salaryTemplateMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(SalaryTemplate.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), SalaryTemplate.class);
    }
    public int countData(DataQuery query) {
        return countData(salaryTemplateMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(SalaryTemplate.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(salaryTemplateMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = salaryTemplateMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<SalaryTemplate> find(List<QueryRule> rules) {
        return salaryTemplateMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<SalaryTemplate> findSubNodes(String id) {
        return salaryTemplateMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<SalaryTemplate> findByParentId(String parentId) {
        return salaryTemplateMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
