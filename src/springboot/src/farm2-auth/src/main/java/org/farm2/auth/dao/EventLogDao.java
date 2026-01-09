package org.farm2.auth.dao;

import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.UserPost;
import org.farm2.base.db.SuperDao;
import org.farm2.auth.domain.EventLog;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.FarmUUID;
import org.farm2.tools.db.commons.QueryRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**事件日志 
 * @author cbtg自动生成  2025-1-8 22:28:50 
 */
@Repository
public class EventLogDao extends SuperDao {
    @Autowired
    private EventLogMapper eventLogMapper;
    private static String TABLE_NAME = "FARM2_AUTH_EVENTLOG";
    private static String PRIMARY_KEY = "ID";

    public List<EventLog> find(List<QueryRule> rules) {
        return eventLogMapper.find(TABLE_NAME, rules);
    }
    public EventLog findById(String id) {
        return eventLogMapper.findById(TABLE_NAME, id);
    }

    public List<EventLog> findAll() {
        return eventLogMapper.findAll(TABLE_NAME);
    }

    public void insert(EventLog eventLog) {
        insert(eventLogMapper, eventLog, TABLE_NAME, PRIMARY_KEY);
    }

    public void update(EventLog eventLog) {
        update(eventLogMapper, eventLog, TABLE_NAME, PRIMARY_KEY);
    }


    public void deleteById(String id) {
        eventLogMapper.deleteById(TABLE_NAME,id);
    }

    public void delete(List<QueryRule> rules) {
        eventLogMapper.delete(TABLE_NAME, rules);
    }

    public DataResult queryData(DataQuery query) {
        return queryData(eventLogMapper, query, generateSelectFields(EventLog.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"));
    }

    public EventLog queryOne(List<QueryRule> rules) {
        return queryOne(eventLogMapper, DataQuery.getInstance().addRules(rules), generateSelectFields(EventLog.class, "A").ignore("PASSWORD").getTitles(), addAlias(TABLE_NAME, "A"), EventLog.class);
    }
    public int countData(DataQuery query) {
        return countData(eventLogMapper, query, addAlias(TABLE_NAME, "A"));
    }

    public int countData(List<QueryRule> rules) {
        DataQuery query = DataQuery.getInstance().addRules(rules);
        query.setSql(generateSelectFields(EventLog.class, "A").getTitles(), addAlias(TABLE_NAME, "A"));
        return countData(eventLogMapper, query, addAlias(TABLE_NAME, "A"));
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
        int n = eventLogMapper.isExist(TABLE_NAME, excludeId, fieldName, value);
        return n > 0;
    }
    /**[tree：树形结构使用]
    public List<EventLog> findSubNodes(String id) {
        return eventLogMapper.findSubNodes(TABLE_NAME, findById(id).getTreecode() + "%");
    }
    */
    /**[tree：树形结构使用]
    public List<EventLog> findByParentId(String parentId) {
        return eventLogMapper.findByParentId(TABLE_NAME, parentId);
    }
     */
}
