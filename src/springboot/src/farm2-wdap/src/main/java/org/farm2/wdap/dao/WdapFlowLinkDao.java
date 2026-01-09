package org.farm2.wdap.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.wdap.domain.WdapFlowLink;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.farm2.wdap.domain.WdapFlowNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**流程连线 
 * @author cbtg自动生成  2025-1-21 22:42:51 
 */
@Repository
public class WdapFlowLinkDao extends SuperDao {
    @Autowired
    private WdapFlowLinkMapper wdapFlowLinkMapper;
    private static String TABLE_NAME = "WDAP2_FLOW_LINK";
    private static String PRIMARY_KEY = "ID";


    public WdapFlowLink findById(String id) {
        return wdapFlowLinkMapper.findById(TABLE_NAME, id);
    }

    public List<WdapFlowLink> findAll() {
        return wdapFlowLinkMapper.findAll(TABLE_NAME);
    }

    public List<WdapFlowLink> find(List<QueryRule> rules) {
        return wdapFlowLinkMapper.find(TABLE_NAME, rules);
    }

    public void insert(WdapFlowLink wdapFlowLink) {
        insert(wdapFlowLinkMapper, wdapFlowLink, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(WdapFlowLink wdapFlowLink) {
        update(wdapFlowLinkMapper, wdapFlowLink, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        wdapFlowLinkMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        wdapFlowLinkMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(wdapFlowLinkMapper, query, generateSelectFields(WdapFlowLink.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public WdapFlowLink queryOne(List<QueryRule> rules) {
        return queryOne(wdapFlowLinkMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(WdapFlowLink.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), WdapFlowLink.class);
    }
    public int countData(DataQuery query) {
        return countData(wdapFlowLinkMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(WdapFlowLink.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(wdapFlowLinkMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = wdapFlowLinkMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<WdapFlowLink> findSubNodes(String id) {
        return wdapFlowLinkMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<WdapFlowLink> findByParentId(String parentId) {
        return wdapFlowLinkMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
