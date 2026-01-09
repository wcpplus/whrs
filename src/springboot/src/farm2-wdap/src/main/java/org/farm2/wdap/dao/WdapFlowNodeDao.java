package org.farm2.wdap.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.wdap.domain.WdapFlowNode;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**流程节点 
 * @author cbtg自动生成  2025-1-21 22:39:41 
 */
@Repository
public class WdapFlowNodeDao extends SuperDao {
    @Autowired
    private WdapFlowNodeMapper wdapFlowNodeMapper;
    private static String TABLE_NAME = "WDAP2_FLOW_NODE";
    private static String PRIMARY_KEY = "ID";


    public WdapFlowNode findById(String id) {
        return wdapFlowNodeMapper.findById(TABLE_NAME, id);
    }

    public List<WdapFlowNode> findAll() {
        return wdapFlowNodeMapper.findAll(TABLE_NAME);
    }

    public void insert(WdapFlowNode wdapFlowNode) {
        insert(wdapFlowNodeMapper, wdapFlowNode, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(WdapFlowNode wdapFlowNode) {
        update(wdapFlowNodeMapper, wdapFlowNode, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        wdapFlowNodeMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        wdapFlowNodeMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(wdapFlowNodeMapper, query, generateSelectFields(WdapFlowNode.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public WdapFlowNode queryOne(List<QueryRule> rules) {
        return queryOne(wdapFlowNodeMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(WdapFlowNode.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), WdapFlowNode.class);
    }
    public int countData(DataQuery query) {
        return countData(wdapFlowNodeMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(WdapFlowNode.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(wdapFlowNodeMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public List<WdapFlowNode> find(List<QueryRule> rules) {
        return wdapFlowNodeMapper.find(TABLE_NAME, rules);
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
        int n = wdapFlowNodeMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<WdapFlowNode> findSubNodes(String id) {
        return wdapFlowNodeMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<WdapFlowNode> findByParentId(String parentId) {
        return wdapFlowNodeMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
