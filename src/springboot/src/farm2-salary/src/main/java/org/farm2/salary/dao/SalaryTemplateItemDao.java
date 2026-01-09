package org.farm2.salary.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.salary.domain.SalaryTemplateItem;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**薪酬项 
 * @author cbtg自动生成  2026-1-7 10:48:10 
 */
@Repository
public class SalaryTemplateItemDao extends SuperDao {
    @Autowired
    private SalaryTemplateItemMapper salaryTemplateItemMapper;
    private static String TABLE_NAME = "WHRS_SALARY_TEMPLATE_ITEM";
    private static String PRIMARY_KEY = "ID";


    public SalaryTemplateItem findById(String id) {
        return salaryTemplateItemMapper.findById(TABLE_NAME, id);
    }

    public List<SalaryTemplateItem> findAll() {
        return salaryTemplateItemMapper.findAll(TABLE_NAME);
    }

    public void insert(SalaryTemplateItem salaryTemplateItem) {
        insert(salaryTemplateItemMapper, salaryTemplateItem, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(SalaryTemplateItem salaryTemplateItem) {
        update(salaryTemplateItemMapper, salaryTemplateItem, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        salaryTemplateItemMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        salaryTemplateItemMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(salaryTemplateItemMapper, query, generateSelectFields(SalaryTemplateItem.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public SalaryTemplateItem queryOne(List<QueryRule> rules) {
        return queryOne(salaryTemplateItemMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(SalaryTemplateItem.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), SalaryTemplateItem.class);
    }
    public int countData(DataQuery query) {
        return countData(salaryTemplateItemMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(SalaryTemplateItem.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(salaryTemplateItemMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = salaryTemplateItemMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<SalaryTemplateItem> find(List<QueryRule> rules) {
        return salaryTemplateItemMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<SalaryTemplateItem> findSubNodes(String id) {
        return salaryTemplateItemMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<SalaryTemplateItem> findByParentId(String parentId) {
        return salaryTemplateItemMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
