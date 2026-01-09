package org.farm2.wdap.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.SuperDao;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 转换任务
 *
 * @author cbtg自动生成  2025-1-25 9:21:04
 */
@Repository
public class WdapTaskDao extends SuperDao {
    @Autowired
    private WdapTaskMapper wdapTaskMapper;
    private static String TABLE_NAME = "WDAP2_TASK";
    private static String PRIMARY_KEY = "ID";


    public WdapTask findById(String id) {
        return wdapTaskMapper.findById(TABLE_NAME, id);
    }

    public List<WdapTask> findAll() {
        return wdapTaskMapper.findAll(TABLE_NAME);
    }

    public void insert(WdapTask wdapTask) {
        insert(wdapTaskMapper, wdapTask, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(WdapTask wdapTask) {
        update(wdapTaskMapper, wdapTask, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        wdapTaskMapper.deleteById(TABLE_NAME, id);
    }

    public void delete(List<QueryRule> rules) {
        wdapTaskMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(wdapTaskMapper, query, generateSelectFields(WdapTask.class, "A").ignore("PASSWORD").getTitles() + ",B.TITLE AS TITLE", addAlias(TABLE_NAME, "A") + " LEFT JOIN FARM2_RESOURCE_FILE AS B on A.FILEID=B.ID");
    }

    public WdapTask queryOne(List<QueryRule> rules) {
        return queryOne(wdapTaskMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(WdapTask.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), WdapTask.class);
    }

    public int countData(DataQuery query) {
        return countData(wdapTaskMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(WdapTask.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(wdapTaskMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = wdapTaskMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }


    public List<WdapTask> find(List<QueryRule> rules) {
        return wdapTaskMapper.find(TABLE_NAME, rules);
    }


    /**[tree：树形结构使用]
     public List<WdapTask> findSubNodes(String id) {
     return wdapTaskMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
     }
     */
    /**[tree：树形结构使用]
     public List<WdapTask> findByParentId(String parentId) {
     return wdapTaskMapper.findByParentId(TABLE_NAME, parentId);
     }
     */
}
