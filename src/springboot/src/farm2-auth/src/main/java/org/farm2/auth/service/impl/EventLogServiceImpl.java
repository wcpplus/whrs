package org.farm2.auth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.auth.dao.EventLogDao;
import org.farm2.auth.domain.EventLog;
import org.farm2.auth.service.EventLogServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.caches.handle.FarmCachesCreator;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.QueryRule;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.structure.ResourceInfo;
import org.farm2.tools.time.FarmTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 事件日志
 *
 * @author cbtg自动生成  2025-1-8 22:28:50
 */
@Service
@Slf4j
public class EventLogServiceImpl implements EventLogServiceInter {


    @Autowired
    private EventLogDao eventLogDao;

    @Transactional
    @Override
    public EventLog insertEventLogEntity(EventLog eventLog) {
        FarmDbFields.initInsertBean(eventLog, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(eventLog.getType(), "1", eventLog::setType);
        eventLogDao.insert(eventLog);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return eventLog;
    }

    @Transactional
    @Override
    public EventLog editEventLogEntity(EventLog eventLog) {
        EventLog saveEventLog = getEventLogById(eventLog.getId());
        FarmExceptionUtils.throwNullEx(saveEventLog, I18n.msg("事件日志不存在:?", eventLog.getId()));
        saveEventLog.setId(eventLog.getId());
        saveEventLog.setCtime(eventLog.getCtime());
        saveEventLog.setActiontype(eventLog.getActiontype());
        saveEventLog.setObjtype(eventLog.getObjtype());
        saveEventLog.setObjid(eventLog.getObjid());
        saveEventLog.setUserip(eventLog.getUserip());
        saveEventLog.setUserkey(eventLog.getUserkey());
        saveEventLog.setNote(eventLog.getNote());

        FarmDbFields.initUpdateBean(saveEventLog, FarmUserContextLoader.getCurrentUser());
        eventLogDao.update(saveEventLog);
        return saveEventLog;
    }

    @Transactional
    @Override
    public EventLog getEventLogById(String id) {
        return eventLogDao.findById(id);
    }

    @Override
    public List<EventLog> getEventLogs(DataQuery query) {
        return eventLogDao.queryData(query.setCount(false)).getData(EventLog.class);
    }


    @Transactional
    @Override
    public DataResult searchEventLog(DataQuery query) {
        DataResult result = eventLogDao.queryData(query);
        return result;
    }

    @Override
    public int getEventLogNum(DataQuery query) {
        return eventLogDao.countData(query);
    }


    @Transactional
    @Override
    public void delEventLog(String id) {
        /*[tree：树形结构使用]
        if ( eventLogDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        eventLogDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return eventLogDao.countData(query);
    }

    @Override
    public ResourceInfo getAllNum() {
        return new ResourceInfo("事件日志", eventLogDao.countData(new ArrayList<>()));
    }

    @Transactional
    @Override
    public void clearByDate(Date maxDate) {
        String maxDateStr = FarmTimeTool.getTimeDate(maxDate, "yyyyMMddHHmmss");
        eventLogDao.delete(DBRuleList.getInstance().add(new DBRule("CTIME", maxDateStr, "<")).toList());
    }

    @Override
    public void delEventLogs(List<QueryRule> rules) {
        eventLogDao.delete(rules);
    }

    @Override
    public List<EventLog> findEventLog(List<QueryRule> rules) {
        return eventLogDao.find(rules);
    }

    @Override
    public Map<String, Integer> getTodayNums(boolean isCache) {
        String userKey = FarmUserContextLoader.getCurrentUserKey();
        return FarmCaches.getInstance().getCacheData(userKey, FarmCacheKeys.USER_TODAY_EVENT_NUMS,
                new FarmCachesCreator() {
                    @Override
                    public Object handle(String key) {
                        String time14 = FarmTimeTool.getTimeDate14ForDay(-7, new Date());
                        Map<String, Integer> map = new HashMap<>();
                        map.put("creatKnowNum", getNum(DataQuery.getInstance()
                                .addRule(new DBRule("OBJTYPE", F2EObjectT.KNOW.name(), "="))
                                .addRule(new DBRule("ACTIONTYPE", F2EActionT.ADD.name(), "="))
                                .addRule(new DBRule("USERKEY", userKey, "="))
                                .addRule(new DBRule("CTIME", time14, ">="))
                        ));
                        map.put("completeCourseNum", getNum(DataQuery.getInstance()
                                .addRule(new DBRule("OBJTYPE", F2EObjectT.LMS_COURSE.name(), "="))
                                .addRule(new DBRule("ACTIONTYPE", F2EActionT.LEARNING_COMPLETE.name(), "="))
                                .addRule(new DBRule("USERKEY", userKey, "="))
                                .addRule(new DBRule("CTIME", time14, ">="))
                        ));
                        map.put("submitCardNum", getNum(DataQuery.getInstance()
                                .addRule(new DBRule("OBJTYPE", F2EObjectT.WTS_CARD.name(), "="))
                                .addRule(new DBRule("ACTIONTYPE", F2EActionT.SUBMIT.name(), "="))
                                .addRule(new DBRule("USERKEY", userKey, "="))
                                .addRule(new DBRule("CTIME", time14, ">="))
                        ));
                        map.put("archiveTaskNum", getNum(DataQuery.getInstance()
                                .addRule(new DBRule("OBJTYPE", F2EObjectT.PROJ_TASK.name(), "="))
                                .addRule(new DBRule("ACTIONTYPE", F2EActionT.ARCHIVE.name(), "="))
                                .addRule(new DBRule("USERKEY", userKey, "="))
                                .addRule(new DBRule("CTIME", time14, ">="))
                        ));
                        return map;
                    }
                }, isCache);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            EventLog node = getEventLogById(sourceId);
            if (!"NONE".equals(targetId)) {
                EventLog target = getEventLogById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            eventLogDao.update(node);
            // 构造所有树TREECODE
            List<EventLog> list = eventLogDao.findSubNodes(sourceId);
            for (EventLog treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    EventLog node = eventLogDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(eventLogDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    eventLogDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            EventLog node =  eventLogDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            eventLogDao.update(node);
        }
    }*/
}
