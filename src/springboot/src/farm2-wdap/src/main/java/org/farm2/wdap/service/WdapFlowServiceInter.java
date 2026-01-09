package org.farm2.wdap.service;

import org.farm2.files.domain.ResourceFile;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.dto.flow.EchartsTaskData;
import org.farm2.wdap.utils.FlowNodeAddModel;

import java.util.List;
import java.util.Map;

/**转换流程 
 * @author cbtg自动生成  2025-1-21 18:42:38 
 */
public interface WdapFlowServiceInter {

    public WdapFlow insertWdapFlowEntity(WdapFlow wdapFlow);

    public WdapFlow editWdapFlowEntity(WdapFlow wdapFlow);

    public void delWdapFlow(String id);

    public WdapFlow getWdapFlowById(String id);

    public List<WdapFlow> getWdapFlows(DataQuery query);

    public DataResult searchWdapFlow(DataQuery query);

    public int getWdapFlowNum(DataQuery query);
    
    public int getNum(DataQuery query);

    /**
     * 加载流程图节点
     *
     * @param flowid
     * @return
     */
    public EchartsTaskData getDatas(String flowid);

    /**
     * 在流程上添加节点
     *
     * @param flowid
     * @param baseNodeId
     * @param flowModel
     */
    public void addTaskNode(String flowid, String baseNodeId, FlowNodeAddModel flowModel);

    /**
     * 删除节点
     *
     * @param id
     */
    public void delTaskNode(String id);

    /**
     * 删除全部节点
     */
    public void delAllTaskNode(String flowid);

    /**
     * 绑定处理器到节点上
     *
     * @param nodeid
     * @param convertorId
     */
    public void bindNode(String nodeid, String convertorId);


    /**
     * 通过附件信息获取可执行流程
     *
     * @param rfile
     * @return
     */
    public List<WdapFlow> getFlows(ResourceFile rfile);

    /**
     * 执行一个转换流程
     *
     * @param flowid 流程id
     * @param nodeId 执行任务节点
     * @param rfile  资源附件
     * @return
     */
    public String runFlow(String flowid, WdapTask task, String nodeId, ResourceFile rfile, Map<String, Object> context);
}
