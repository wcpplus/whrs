package org.farm2.wdap.service;

import org.farm2.files.domain.ResourceFile;
import org.farm2.tools.web.dto.IdDto;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.wdap.utils.WdapJsonLogs;

import java.util.List;

/**
 * 转换任务
 *
 * @author cbtg自动生成  2025-1-25 9:21:04
 */
public interface WdapTaskServiceInter {

    public void delWdapTask(String id);

    public WdapTask getWdapTaskById(String id);

    public List<WdapTask> getWdapTasks(DataQuery query);

    public DataResult searchWdapTask(DataQuery query);

    public int getWdapTaskNum(DataQuery query);

    public int getNum(DataQuery query);

    /**
     * 添加转换任务
     *
     * @param fileid
     */
    public WdapTask addTask(String fileid);

    /**
     * 删除转换任务
     *
     * @param fileid
     */
    public void delTaskByFileid(String fileid);

    /**
     * 查找转换任务(并设置任务状态为 等待或执行中)
     *
     * @param taskid
     */
    public List<WdapFlow> findTaskFlowsAndUpdateState(String taskid);

    /**
     * 获得资源附件id
     *
     * @param taskid
     * @return
     */
    public ResourceFile getResourceFileIdByTaskId(String taskid);


    /**
     * 更新任务状态和日志
     *
     * @param task
     */
    public void updateState(WdapTask task);

    /**
     * 初始化任务为等待状态
     *
     * @param taskid
     */
    public void initStateToWait(String taskid);

    /**
     * 获得等待中的任务
     *
     * @param num
     * @return
     */
    public List<WdapTask> getWaitTask(int num);

    /**
     * 获得附件的转换任务
     *
     * @param fileid
     * @return
     */
    public WdapTask getWdapTaskByFileId(String fileid);

    /**
     * 获得附件的转换任务
     *
     * @param fileid
     * @return
     */
    public List<WdapTask> getWdapTasksByFileId(String fileid);
}
