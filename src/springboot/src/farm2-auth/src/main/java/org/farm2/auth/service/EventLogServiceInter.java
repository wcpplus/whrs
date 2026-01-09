package org.farm2.auth.service;

import org.farm2.auth.domain.EventLog;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.QueryRule;
import org.farm2.tools.structure.ResourceInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 事件日志
 *
 * @author cbtg自动生成  2025-1-8 22:28:50
 */
public interface EventLogServiceInter {

    public EventLog insertEventLogEntity(EventLog eventLog);

    public EventLog editEventLogEntity(EventLog eventLog);

    public void delEventLog(String id);

    public EventLog getEventLogById(String id);

    public List<EventLog> getEventLogs(DataQuery query);

    public DataResult searchEventLog(DataQuery query);

    public int getEventLogNum(DataQuery query);

    public int getNum(DataQuery query);

    public ResourceInfo getAllNum();

    /**
     * 清理之前的日志
     *
     * @param maxDate
     */
    public void clearByDate(Date maxDate);

    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
    public void delEventLogs(List<QueryRule> rules);

    public List<EventLog> findEventLog(List<QueryRule> rules);

    /**
     * 获取用户今日资源数量
     *
     * @param isCache
     * @return
     */
    public Map<String, Integer> getTodayNums(boolean isCache);
}

