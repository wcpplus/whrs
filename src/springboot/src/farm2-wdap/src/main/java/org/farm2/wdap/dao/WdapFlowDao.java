package org.farm2.wdap.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.farm2.wdap.domain.WdapFlowNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**转换流程 
 * @author cbtg自动生成  2025-1-21 18:42:38 
 */
@Repository
public class WdapFlowDao extends SuperDao {
    @Autowired
    private WdapFlowMapper wdapFlowMapper;
    private static String TABLE_NAME = "WDAP2_FLOW";
    private static String PRIMARY_KEY = "ID";


    public WdapFlow findById(String id) {
        return wdapFlowMapper.findById(TABLE_NAME, id);
    }

    public List<WdapFlow> findAll() {
        return wdapFlowMapper.findAll(TABLE_NAME);
    }

    public void insert(WdapFlow wdapFlow) {
        insert(wdapFlowMapper, wdapFlow, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(WdapFlow wdapFlow) {
        update(wdapFlowMapper, wdapFlow, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        wdapFlowMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        wdapFlowMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(wdapFlowMapper, query, generateSelectFields(WdapFlow.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public WdapFlow queryOne(List<QueryRule> rules) {
        return queryOne(wdapFlowMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(WdapFlow.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), WdapFlow.class);
    }
    public int countData(DataQuery query) {
        return countData(wdapFlowMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(WdapFlow.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(wdapFlowMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public List<WdapFlow> find(List<QueryRule> rules) {
        return wdapFlowMapper.find(TABLE_NAME, rules);
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
        int n = wdapFlowMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<WdapFlow> findSubNodes(String id) {
        return wdapFlowMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<WdapFlow> findByParentId(String parentId) {
        return wdapFlowMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
