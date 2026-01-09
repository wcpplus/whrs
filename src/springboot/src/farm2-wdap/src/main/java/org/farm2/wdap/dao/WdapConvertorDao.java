package org.farm2.wdap.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**文件转换器 
 * @author cbtg自动生成  2025-1-24 10:22:07 
 */
@Repository
public class WdapConvertorDao extends SuperDao {
    @Autowired
    private WdapConvertorMapper wdapConvertorMapper;
    private static String TABLE_NAME = "WDAP2_CONVERTOR";
    private static String PRIMARY_KEY = "ID";


    public WdapConvertor findById(String id) {
        return wdapConvertorMapper.findById(TABLE_NAME, id);
    }

    public List<WdapConvertor> findAll() {
        return wdapConvertorMapper.findAll(TABLE_NAME);
    }

    public void insert(WdapConvertor wdapConvertor) {
        insert(wdapConvertorMapper, wdapConvertor, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(WdapConvertor wdapConvertor) {
        update(wdapConvertorMapper, wdapConvertor, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        wdapConvertorMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        wdapConvertorMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(wdapConvertorMapper, query, generateSelectFields(WdapConvertor.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public WdapConvertor queryOne(List<QueryRule> rules) {
        return queryOne(wdapConvertorMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(WdapConvertor.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), WdapConvertor.class);
    }
    public int countData(DataQuery query) {
        return countData(wdapConvertorMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(WdapConvertor.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(wdapConvertorMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = wdapConvertorMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    
    
    public List<WdapConvertor> find(List<QueryRule> rules) {
        return wdapConvertorMapper.find(TABLE_NAME, rules);
    }
    
    
    /**[tree：树形结构使用]
    public List<WdapConvertor> findSubNodes(String id) {
        return wdapConvertorMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<WdapConvertor> findByParentId(String parentId) {
        return wdapConvertorMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
