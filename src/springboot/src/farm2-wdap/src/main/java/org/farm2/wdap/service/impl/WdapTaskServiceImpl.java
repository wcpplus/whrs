package org.farm2.wdap.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.files.Farm2FileUtils;
import org.farm2.tools.time.FarmTimeTool;
import org.farm2.wdap.dao.WdapExtendFileDao;
import org.farm2.wdap.dao.WdapTaskDao;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.service.WdapFlowServiceInter;
import org.farm2.wdap.service.WdapTaskServiceInter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.wdap.utils.WdapJsonLogs;
import org.farm2.wdap.utils.WdapTaskStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 转换任务
 *
 * @author cbtg自动生成  2025-1-25 9:21:04
 */
@Service
@Slf4j
public class WdapTaskServiceImpl implements WdapTaskServiceInter {
    @Autowired
    private WdapFlowServiceInter wdapFlowServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private WdapTaskDao wdapTaskDao;
    @Autowired
    private WdapExtendFileDao wdapExtendFileDao;
//    @Transactional
//    @Override
//    public WdapTask insertWdapTaskEntity(WdapTask wdapTask) {
//        FarmDbFields.initInsertBean(wdapTask, FarmUserContextLoader.getCurrentUser());
//        //FarmBeanUtils.runFunctionByBlank(wdapTask.getType(), "1", wdapTask::setType);
//        wdapTaskDao.insert(wdapTask);
//       //[tree：树形结构使用]
//       //initTreeCode(actions.getId());
//        return wdapTask;
//    }
//
//    @Transactional
//    @Override
//    public WdapTask editWdapTaskEntity(WdapTask wdapTask) {
//        WdapTask saveWdapTask = getWdapTaskById(wdapTask.getId());
//        FarmExceptionUtils.throwNullEx(saveWdapTask, I18n.msg("转换任务不存在:?", wdapTask.getId()));
//        saveWdapTask.setId(wdapTask.getId());
//        saveWdapTask.setFileid(wdapTask.getFileid());
//        saveWdapTask.setPstate(wdapTask.getPstate());
//        saveWdapTask.setLogjson(wdapTask.getLogjson());
//        saveWdapTask.setEtime(wdapTask.getEtime());
//        saveWdapTask.setCtime(wdapTask.getCtime());
//        saveWdapTask.setNote(wdapTask.getNote());
//        saveWdapTask.setServerid(wdapTask.getServerid());
//
//        FarmDbFields.initUpdateBean(saveWdapTask, FarmUserContextLoader.getCurrentUser());
//        wdapTaskDao.update(saveWdapTask);
//        return saveWdapTask;
//    }

    @Transactional
    @Override
    public WdapTask getWdapTaskById(String id) {
        return wdapTaskDao.findById(id);
    }

    @Override
    public List<WdapTask> getWdapTasks(DataQuery query) {
        return wdapTaskDao.queryData(query.setCount(false)).getData(WdapTask.class);
    }


    @Transactional
    @Override
    public DataResult searchWdapTask(DataQuery query) {
        query.addRule(new DBRule("SERVERID", Farm2ConfigUtils.getServiceId(), "="));
        DataResult result = wdapTaskDao.queryData(query);
        return result;
    }

    @Override
    public int getWdapTaskNum(DataQuery query) {
        return wdapTaskDao.countData(query);
    }


    @Transactional
    @Override
    public void delWdapTask(String wdapTaskId) {
        WdapTask task = wdapTaskDao.findById(wdapTaskId);
        wdapExtendFileDao.delete(DBRuleList.getInstance().add(new DBRule("TASKID", wdapTaskId, "=")).toList());
        wdapTaskDao.deleteById(wdapTaskId);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(task.getFileid(),false);
        if (rfile != null) {
            File exDirPath = resourceFileServiceImpl.getExDirPath(rfile);
            if (exDirPath.getPath().indexOf("tempdir") > 0) {
                try {
                    Farm2FileUtils.deleteFolderRecursively(exDirPath.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    @Transactional
    public int getNum(DataQuery query) {
        return wdapTaskDao.countData(query);
    }

    @Override
    @Transactional
    public WdapTask addTask(String fileid) {
        if (wdapTaskDao.countData(DBRuleList.getInstance()
                .add(new DBRule("FILEID", fileid, "=")).toList()) <= 0) {
            WdapTask wdapTask = new WdapTask();
            wdapTask.setFileid(fileid);
            wdapTask.setPstate("0");
            wdapTask.setLogjson(WdapJsonLogs.getInstance().add(WdapJsonLogs.LogType.INFO, "创建任务").toJson());
            wdapTask.setServerid(Farm2ConfigUtils.getServiceId());
            FarmDbFields.initInsertBean(wdapTask, FarmUserContextLoader.getCurrentUser());
            wdapTaskDao.insert(wdapTask);
            return wdapTask;
        }
        return null;
    }

    @Override
    public void delTaskByFileid(String fileid) {
        List<WdapTask> tasks = wdapTaskDao.find(DBRuleList.getInstance().add(new DBRule("FILEID", fileid, "=")).toList());
        for (WdapTask task : tasks) {
            delWdapTask(task.getId());
        }
    }

    @Override
    @Transactional
    public List<WdapFlow> findTaskFlowsAndUpdateState(String taskid) {
        WdapTask wdapTask = getWdapTaskById(taskid);
        //在转换流程中找到可用流程
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(wdapTask.getFileid(),false);
        List<WdapFlow> flows = wdapFlowServiceImpl.getFlows(rfile);
        if (flows.size() > 0) {
            WdapTask task = wdapTaskDao.findById(taskid);
            task.setEtime(FarmTimeTool.getTimeDate14());
            //0等待，1转换中，2完成，3无需转换，9失败
            task.setPstate(WdapTaskStateEnum.RUNNING.code);
            task.setLogjson(WdapJsonLogs.getInstance(task.getLogjson()).toJson());
            wdapTaskDao.update(task);
            FarmProcessUtils.setProcess(wdapTask.getFileid(), FarmProcessTypeEnum.CONVERT_EXFILE, WdapTaskStateEnum.getDic().get(task.getPstate()));
            //执行转换
        } else {
            WdapTask task = wdapTaskDao.findById(taskid);
            task.setEtime(FarmTimeTool.getTimeDate14());
            //0等待，1转换中，2完成，3无需转换，9失败
            task.setPstate(WdapTaskStateEnum.DISABLE.code);
            task.setLogjson(WdapJsonLogs.getInstance(task.getLogjson()).add(WdapJsonLogs.LogType.INFO, "无可用转换流程").toJson());
            wdapTaskDao.update(task);
            FarmProcessUtils.setProcess(wdapTask.getFileid(), FarmProcessTypeEnum.CONVERT_EXFILE, WdapTaskStateEnum.getDic().get(task.getPstate()));
        }
        return flows;
    }

    @Override
    public ResourceFile getResourceFileIdByTaskId(String taskid) {
        WdapTask task = wdapTaskDao.findById(taskid);
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(task.getFileid(),false);
        return rfile;
    }


    @Override
    @Transactional
    public void updateState(WdapTask task) {
        WdapTask uptask = wdapTaskDao.findById(task.getId());
        uptask.setEtime(FarmTimeTool.getTimeDate14());
        uptask.setPstate(task.getPstate());
        uptask.setLogjson(task.getLogjson());
        FarmProcessUtils.setProcess(task.getFileid(), FarmProcessTypeEnum.CONVERT_EXFILE, WdapTaskStateEnum.getDic().get(task.getPstate()));
        wdapTaskDao.update(uptask);
    }

    @Override
    @Transactional
    public void initStateToWait(String taskid) {
        WdapTask task = getWdapTaskById(taskid);
        if (!task.getPstate().equals("1")) {
            task.setPstate("0");
            task.setLogjson(WdapJsonLogs.getInstance().add(WdapJsonLogs.LogType.INFO, "创建任务").toJson());
            updateState(task);
        }
    }

    @Override
    @Transactional
    public List<WdapTask> getWaitTask(int num) {
        DataQuery query = new DataQuery();
        query.setCount(false);
        query.setPageSize(num);
        query.addRule(new DBRule("PSTATE", "0", "="));
        query.addSort(new DBSort("CTIME", DBSort.SORT_TYPE.ASC));
        DataResult result = wdapTaskDao.queryData(query);
        return result.getData(WdapTask.class);
    }

    @Override
    @Transactional
    public WdapTask getWdapTaskByFileId(String fileid) {
        List<WdapTask> tasks = wdapTaskDao.find(DBRuleList.getInstance().add(new DBRule("FILEID", fileid, "=")).toList());
        if (tasks.size() > 0) {
            return tasks.get(0);
        }
        return null;
    }

    @Override
    public List<WdapTask> getWdapTasksByFileId(String fileid) {
        List<WdapTask> tasks = wdapTaskDao.find(DBRuleList.getInstance().add(new DBRule("FILEID", fileid, "=")).toList());
        return tasks;
    }
}
