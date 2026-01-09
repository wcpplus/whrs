package org.farm2.salary.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.salary.domain.SalaryUserItem;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**用户薪酬明细 
 * @author cbtg自动生成  2026-1-8 14:48:48 
 */
@Repository
public class SalaryUserItemDao extends SuperDao {
    @Autowired
    private SalaryUserItemMapper salaryUserItemMapper;
    private static String TABLE_NAME = "WHRS_SALARY_USER_ITEM";
    private static String PRIMARY_KEY = "ID";


    public SalaryUserItem findById(String id) {
        return salaryUserItemMapper.findById(TABLE_NAME, id);
    }

    public List<SalaryUserItem> findAll() {
        return salaryUserItemMapper.findAll(TABLE_NAME);
    }

    public void insert(SalaryUserItem salaryUserItem) {
        insert(salaryUserItemMapper, salaryUserItem, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(SalaryUserItem salaryUserItem) {
        update(salaryUserItemMapper, salaryUserItem, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        salaryUserItemMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        salaryUserItemMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(salaryUserItemMapper, query, generateSelectFields(SalaryUserItem.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public SalaryUserItem queryOne(List<QueryRule> rules) {
        return queryOne(salaryUserItemMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(SalaryUserItem.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), SalaryUserItem.class);
    }
    public int countData(DataQuery query) {
        return countData(salaryUserItemMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(SalaryUserItem.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(salaryUserItemMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = salaryUserItemMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<SalaryUserItem> find(List<QueryRule> rules) {
        return salaryUserItemMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<SalaryUserItem> findSubNodes(String id) {
        return salaryUserItemMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<SalaryUserItem> findByParentId(String parentId) {
        return salaryUserItemMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
