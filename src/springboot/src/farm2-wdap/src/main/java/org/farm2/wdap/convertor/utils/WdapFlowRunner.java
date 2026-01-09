package org.farm2.wdap.convertor.utils;

import lombok.Synchronized;
import org.farm2.base.process.FarmProcessTypeEnum;
import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.files.domain.ResourceFile;
import org.farm2.tools.files.Farm2ProcessState;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.wdap.service.WdapFlowServiceInter;
import org.farm2.wdap.service.WdapTaskServiceInter;
import org.farm2.wdap.service.impl.WdapFlowServiceImpl;
import org.farm2.wdap.utils.WdapJsonLogs;
import org.farm2.wdap.utils.WdapTaskStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来执行转换流程得类
 */
@Component
public class WdapFlowRunner {
    @Autowired
    private WdapTaskServiceInter wdapTaskServiceImpl;
    @Autowired
    private WdapFlowServiceInter wdapFlowServiceImpl;
    @Autowired
    private WdapExtendFileServiceInter wdapExtendFileServiceImpl;

    /**
     * 当前是否有任务在转换
     */
    private static boolean isTaskRunner = false;

    @Synchronized
    public void startRunTasks() {
        if (isTaskRunner == false) {
            isTaskRunner = true;
            new Thread(() -> {
                try {
                    while (true) {
                        Farm2ProcessState.sleep(3000);
                        List<WdapTask> tasks = wdapTaskServiceImpl.getWaitTask(1000);
                        if (tasks.size() <= 0) {
                            //没有可执行任务就退出
                            break;
                        }
                        for (WdapTask taskNode : tasks) {
                            try {
                                String taskId = taskNode.getId();
                                WdapTask task = wdapTaskServiceImpl.getWdapTaskById(taskId);
                                List<WdapFlow> flows = wdapTaskServiceImpl.findTaskFlowsAndUpdateState(taskId);
                                ResourceFile rfile = wdapTaskServiceImpl.getResourceFileIdByTaskId(taskId);
                                run(flows, rfile, task);
                            } catch (Exception e) {
                                taskNode.setPstate(WdapTaskStateEnum.ERROR.code);
                                wdapTaskServiceImpl.updateState(WdapJsonLogs.addLog(taskNode, WdapJsonLogs.LogType.ERROR, e.getMessage()));
                            } finally {
                                //结束进度
                                FarmProcessUtils.setProcessEnd(taskNode.getFileid(), FarmProcessTypeEnum.CONVERT_EXFILE, "完成");
                            }
                            {
                                //兜底,无论如何不能让取出来的任务再变为等待状态
                                WdapTask finaltask = wdapTaskServiceImpl.getWdapTaskById(taskNode.getId());
                                if (finaltask.getPstate().equals("0")) {
                                    finaltask.setPstate(WdapTaskStateEnum.ERROR.code);
                                    wdapTaskServiceImpl.updateState(finaltask);
                                }
                            }
                        }
                    }
                } finally {
                    isTaskRunner = false;
                }
            }).start();
        }
    }

    /**
     * 同步执行[指定的]转换流程
     *
     * @param flows
     * @param rfile
     * @param task
     */
    private void run(List<WdapFlow> flows, ResourceFile rfile, WdapTask task) {
        try {
            //清理任务对应的所有扩展文件
            wdapExtendFileServiceImpl.clearExtendFileByTask(task.getId());
            task.setLogjson(WdapJsonLogs.getInstance().toJson());
            task.setPstate("1");
            for (WdapFlow flow : flows) {
                wdapTaskServiceImpl.updateState(WdapJsonLogs.addLog(task, WdapJsonLogs.LogType.INFO, "开始流程-" + flow.getName()));
                //监控当前流程中节点执行数量，超量时抛出异常：避免死循环
                int runNodeNum = 0;
                //通过START节点启动流程
                String nodeId = WdapFlowServiceImpl.START_NODE_ID;
                Map<String, Object> context = new HashMap<>();
                while (!nodeId.equals("END")) {
                    //查找流程下一个任务，并执行
                    runNodeNum++;
                    if (runNodeNum > 10) {
                        throw new RuntimeException("流程执行超长：" + runNodeNum);
                    }
                    nodeId = wdapFlowServiceImpl.runFlow(flow.getId(), task, nodeId, rfile, context);
                }
                wdapTaskServiceImpl.updateState(WdapJsonLogs.addLog(task, WdapJsonLogs.LogType.INFO, "完成流程-" + flow.getName()));
            }
            task.setPstate(WdapTaskStateEnum.COMPLETE.code);
            wdapTaskServiceImpl.updateState(WdapJsonLogs.addLog(task, WdapJsonLogs.LogType.INFO, "全部转换完成"));
            Farm2Events.emit(F2EObjectT.FILE, task.getFileid(), F2EActionT.EXTEND_FILE, FarmUserContextLoader.getCurrentUser());
        } catch (Exception e) {
            task.setPstate(WdapTaskStateEnum.ERROR.code);
            wdapTaskServiceImpl.updateState(WdapJsonLogs.addLog(task, WdapJsonLogs.LogType.ERROR, e.getMessage()));
        }
    }
}
