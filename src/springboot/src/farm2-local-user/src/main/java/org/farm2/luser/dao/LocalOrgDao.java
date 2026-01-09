package org.farm2.luser.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**组织机构 
 * @author cbtg自动生成  2025-1-2 22:01:57 
 */
@Repository
public class LocalOrgDao extends SuperDao {
    @Autowired
    private LocalOrgMapper localOrgMapper;
    private static String TABLE_NAME = "FARM2_LOCAL_ORG";
    private static String PRIMARY_KEY = "ID";


    public LocalOrg findById(String id) {
        return localOrgMapper.findById(TABLE_NAME, id);
    }

    public List<LocalOrg> find(List<QueryRule> rules) {
        return localOrgMapper.find(TABLE_NAME, rules);
    }
    public List<LocalOrg> findAll() {
        return localOrgMapper.findAll(TABLE_NAME);
    }

    /**
     * 获得子节点
     *
     * @param id
     * @return
     */
    public List<LocalOrg> findSubNodes(String id) {
        return localOrgMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }

    public void insert(LocalOrg localOrg) {
        insert(localOrgMapper, localOrg, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(LocalOrg localOrg) {
        update(localOrgMapper, localOrg, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        localOrgMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        localOrgMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(localOrgMapper, query, generateSelectFields(LocalOrg.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public LocalOrg queryOne(List<QueryRule> rules) {
        return queryOne(localOrgMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(LocalOrg.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), LocalOrg.class);
    }
    public int countData(DataQuery query) {
        return countData(localOrgMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = localOrgMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }

    public List<LocalOrg> findByParentId(String parentId) {
        return localOrgMapper.findByParentId(TABLE_NAME, parentId);
    }

    public List<LocalOrg> findByName(String orgname) {
        return localOrgMapper.findByName(TABLE_NAME, orgname);
    }
}
