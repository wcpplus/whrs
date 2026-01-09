package org.farm2.wdap.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.time.FarmTimeTool;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertResult;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.dao.*;
import org.farm2.wdap.domain.*;
import org.farm2.wdap.dto.flow.EchartsTaskData;
import org.farm2.wdap.dto.flow.EchartsTaskLink;
import org.farm2.wdap.dto.flow.EchartsTaskNode;
import org.farm2.wdap.dto.flow.EchartsTaskNodeStyle;
import org.farm2.wdap.service.WdapFlowServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.farm2.wdap.utils.FileModelUtils;
import org.farm2.wdap.utils.FlowNodeAddModel;
import org.farm2.wdap.utils.FlowNodeModel;
import org.farm2.wdap.utils.WdapJsonLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.nodes.NodeId;

import java.util.*;
import java.util.concurrent.*;

/**转换流程 
 * @author cbtg自动生成  2025-1-21 18:42:38 
 */
@Service
@Slf4j
public class WdapFlowServiceImpl implements WdapFlowServiceInter {
    @Autowired
    private WdapFlowLinkDao wdapFlowLinkDao;
    @Autowired
    private WdapFlowNodeDao wdapFlowNodeDao;
    @Autowired
    private WdapFlowDao wdapFlowDao;
    @Autowired
    private WdapConvertorDao wdapConvertorDao;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private WdapExtendFileDao wdapExtendFileDao;
    @Autowired
    private FarmParameterInter farmParameter;
    public static final String START_NODE_ID = "START";//开始节点ID
    public static final String END_NODE_ID = "END";//结束节点ID

    @Transactional
    @Override
    public WdapFlow insertWdapFlowEntity(WdapFlow wdapFlow) {
        FarmDbFields.initInsertBean(wdapFlow, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(wdapFlow.getType(), "1", wdapFlow::setType);
        wdapFlowDao.insert(wdapFlow);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return wdapFlow;
    }

    @Transactional
    @Override
    public WdapFlow editWdapFlowEntity(WdapFlow wdapFlow) {
        WdapFlow saveWdapFlow = getWdapFlowById(wdapFlow.getId());
        FarmExceptionUtils.throwNullEx(saveWdapFlow, I18n.msg("转换流程不存在:?", wdapFlow.getId()));
        saveWdapFlow.setId(wdapFlow.getId());
        saveWdapFlow.setCtime(wdapFlow.getCtime());
        saveWdapFlow.setEtime(wdapFlow.getEtime());
        saveWdapFlow.setEuserkey(wdapFlow.getEuserkey());
        saveWdapFlow.setCuserkey(wdapFlow.getCuserkey());
        saveWdapFlow.setState(wdapFlow.getState());
        saveWdapFlow.setNote(wdapFlow.getNote());
        saveWdapFlow.setName(wdapFlow.getName());
        saveWdapFlow.setModelkeys(wdapFlow.getModelkeys());
        saveWdapFlow.setSizemin(wdapFlow.getSizemin());
        saveWdapFlow.setSizemax(wdapFlow.getSizemax());
        saveWdapFlow.setExname(wdapFlow.getExname());
         
        FarmDbFields.initUpdateBean(saveWdapFlow, FarmUserContextLoader.getCurrentUser());
        wdapFlowDao.update(saveWdapFlow);
        return saveWdapFlow;
    }

    @Transactional
    @Override
    public WdapFlow getWdapFlowById(String id) {
        return wdapFlowDao.findById(id);
    }

    @Override
    public List<WdapFlow> getWdapFlows(DataQuery query) {
        return wdapFlowDao.queryData(query.setCount(false)).getData(WdapFlow.class);
    }



    @Transactional
    @Override
    public DataResult searchWdapFlow(DataQuery query) {
        DataResult result = wdapFlowDao.queryData(query);
        return result;
    }

    @Override
    public int getWdapFlowNum(DataQuery query) {
        return wdapFlowDao.countData(query);
    }


    @Transactional
    @Override
    public void delWdapFlow(String id) {
        /*[tree：树形结构使用]
        if ( wdapFlowDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        wdapFlowDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  wdapFlowDao.countData(query);
    }

    @Override
    @Transactional
    public EchartsTaskData getDatas(String flowid) {
        EchartsTaskData data = new EchartsTaskData();
        data.getNodes().add(new EchartsTaskNode(START_NODE_ID, "开始", 10, 300, new EchartsTaskNodeStyle("#666666")));
        int n = 1;
        Map<String, EchartsTaskNode> dic = new LinkedHashMap<String, EchartsTaskNode>();
        for (WdapFlowNode node : wdapFlowNodeDao.find(DBRuleList.getInstance().add(new DBRule("FLOWID", flowid, "=")).toList())) {
            String name = "未绑定业务";
            String color = "#666666";
            if (node.getModel().equals(FlowNodeModel.convertor.name())) {
                WdapConvertor convertor = wdapConvertorDao.findById(node.getRid());
                if (convertor != null) {
                    color = "#b23f59";
                    name = convertor.getTitle();
                }
            }
            EchartsTaskNode enode = new EchartsTaskNode(node.getId(), name, 10 + (n * 10), 300,
                    new EchartsTaskNodeStyle(color));
            dic.put(enode.getName(), enode);
            data.getNodes().add(enode);
            n++;
        }
        for (WdapFlowLink node : wdapFlowLinkDao
                .find(DBRuleList.getInstance().add(new DBRule("FLOWID", flowid, "=")).toList())) {
            String content = node.getPcontent();
            if (StringUtils.isBlank(content)) {
                if (!node.getExpression().equals("false")) {
                    content = node.getExpression();
                }
            }
            if (content != null && content.length() > 32) {
                content = content.substring(0, 29) + "...";
            }
            data.getLinks().add(new EchartsTaskLink(node.getSnodeid(), node.getTnodeid(), content));
        }

        EchartsTaskNode endNode = new EchartsTaskNode(END_NODE_ID, "结束", 10 + (n * 10), 300,
                new EchartsTaskNodeStyle("#666666"));

        data.getNodes().add(endNode);
        if (data.getNodes().size() == 2) {
            data.getLinks().add(new EchartsTaskLink(START_NODE_ID, END_NODE_ID));
        }
        loadNodePosition(dic, data, endNode);
        return data;
    }

    @Override
    @Transactional
    public void addTaskNode(String flowid, String baseNodeId, FlowNodeAddModel addModel) {
        if (getWdapFlowById(flowid) == null) {
            throw new RuntimeException("流程不存在" + flowid);
        }
        WdapFlowNode node = new WdapFlowNode();
        node.setFlowid(flowid);
        node.setModel(FlowNodeModel.none.name());
        wdapFlowNodeDao.insert(node);
        // 所有节点数量
        int allNodeNum = wdapFlowNodeDao.countData(DBRuleList.getInstance().add(new DBRule("flowid", flowid, "=")).toList());
        // 基准下级节点
        List<WdapFlowLink> parentSublink = wdapFlowLinkDao.find(DBRuleList.getInstance().add(new DBRule("SNODEID", baseNodeId, "=")).add(new DBRule("flowid", flowid, "=")).toList());

        // 基准上级节点
        List<WdapFlowLink> parentFrontlink = wdapFlowLinkDao.find(DBRuleList.getInstance().add(new DBRule("TNODEID", baseNodeId, "=")).add(new DBRule("flowid", flowid, "=")).toList());

        // 同级添加分支
        if (allNodeNum > 0 && addModel.equals(FlowNodeAddModel.parallel_brother)) {
            // 上级关系
            WdapFlowLink link1 = new WdapFlowLink();
            link1.setSnodeid(parentFrontlink.get(0).getSnodeid());
            link1.setTnodeid(node.getId());
            link1.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link1);
            // 下级关系
            WdapFlowLink link2 = new WdapFlowLink();
            link2.setSnodeid(node.getId());
            link2.setTnodeid(parentSublink.get(0).getTnodeid());
            link2.setFlowid(flowid);
            FarmDbFields.initField(link2, "expression", "true");
            wdapFlowLinkDao.insert(link2);
        }
        // 上級插入
        if (allNodeNum > 0 && addModel.equals(FlowNodeAddModel.insert_before)) {
            for (WdapFlowLink link : parentFrontlink) {
                link.setTnodeid(node.getId());
                wdapFlowLinkDao.update(link);
            }
            WdapFlowLink link1 = new WdapFlowLink();
            link1.setSnodeid(node.getId());
            link1.setTnodeid(baseNodeId);
            link1.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link1);
        }

        // 下級插入
        if (allNodeNum > 0 && addModel.equals(FlowNodeAddModel.insert_next)) {
            for (WdapFlowLink link : parentSublink) {
                link.setSnodeid(node.getId());
                wdapFlowLinkDao.update(link);
            }
            WdapFlowLink link1 = new WdapFlowLink();
            link1.setSnodeid(baseNodeId);
            link1.setTnodeid(node.getId());
            link1.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link1);
        }
        //如果没有自定义节点，就将开始链接到结束
        if (wdapFlowLinkDao.countData(DBRuleList.getInstance().add(new DBRule("FLOWID", flowid, "=")).toList()) == 0) {
            WdapFlowLink link1 = new WdapFlowLink();
            link1.setSnodeid(START_NODE_ID);
            link1.setTnodeid(node.getId());
            link1.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link1);
            WdapFlowLink link2 = new WdapFlowLink();
            link2.setSnodeid(node.getId());
            link2.setTnodeid(END_NODE_ID);
            link2.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link2);
        }
        // 基准下级节点
        if (wdapFlowLinkDao.countData(DBRuleList.getInstance().add(new DBRule("SNODEID", node.getId(), "=")).add(new DBRule("flowid", flowid, "=")).toList()) <= 0) {
            WdapFlowLink link1 = new WdapFlowLink();
            link1.setSnodeid(node.getId());
            link1.setTnodeid(END_NODE_ID);
            link1.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link1);
        }
        // 基准下级节点
        if (wdapFlowLinkDao.countData(DBRuleList.getInstance().add(new DBRule("TNODEID", node.getId(), "=")).add(new DBRule("flowid", flowid, "=")).toList()) <= 0) {
            WdapFlowLink link1 = new WdapFlowLink();
            link1.setSnodeid(START_NODE_ID);
            link1.setTnodeid(node.getId());
            link1.setFlowid(flowid);
            FarmDbFields.initField(link1, "expression", "true");
            wdapFlowLinkDao.insert(link1);
        }
    }

    @Override
    @Transactional
    public void delTaskNode(String id) {
        WdapFlowNode node = wdapFlowNodeDao.findById(id);
        if (node == null) {
            throw new RuntimeException("节点不存在" + id);
        }
        wdapFlowNodeDao.deleteById(id);

        List<WdapFlowLink> frontLinks = wdapFlowLinkDao.find(DBRuleList.getInstance()
                .add(new DBRule("flowid", node.getFlowid(), "=")).add(new DBRule("TNODEID", id, "=")).toList());
        List<WdapFlowLink> behindLinks = wdapFlowLinkDao.find(DBRuleList.getInstance()
                .add(new DBRule("flowid", node.getFlowid(), "=")).add(new DBRule("SNODEID", id, "=")).toList());
        Set<String> frontNodes = new HashSet<String>();
        Set<String> behinNodes = new HashSet<String>();
        for (WdapFlowLink link : frontLinks) {
            // 刪除旧连线
            String nodeid = link.getSnodeid();
            wdapFlowLinkDao.deleteById(link.getId());
            frontNodes.add(nodeid);
        }
        for (WdapFlowLink link : behindLinks) {
            // 刪除旧连线
            String nodeid = link.getTnodeid();
            wdapFlowLinkDao.deleteById(link.getId());
            behinNodes.add(nodeid);
        }
        for (String sid : frontNodes) {
            for (String tid : behinNodes) {
                // 添加新连线
                wdapFlowLinkDao
                        .delete(DBRuleList.getInstance().add(new DBRule("FLOWID", node.getFlowid(), "="))
                                .add(new DBRule("SNODEID", sid, "=")).add(new DBRule("TNODEID", tid, "=")).toList());
                WdapFlowLink link1 = new WdapFlowLink();
                link1.setSnodeid(sid);
                link1.setTnodeid(tid);
                link1.setExpression("true");
                link1.setFlowid(node.getFlowid());
                if (!(sid.equals(START_NODE_ID) && tid.equals(END_NODE_ID))) {
                    wdapFlowLinkDao.insert(link1);
                }
            }
        }
    }

    @Override
    public void delAllTaskNode(String flowid) {
        wdapFlowLinkDao
                .delete(DBRuleList.getInstance().add(new DBRule("FLOWID", flowid, "="))
                        .toList());
        wdapFlowNodeDao
                .delete(DBRuleList.getInstance().add(new DBRule("FLOWID", flowid, "="))
                        .toList());
    }

    @Override
    @Transactional
    public void bindNode(String nodeid, String convertorId) {
        WdapFlowNode node = wdapFlowNodeDao.findById(nodeid);
        if (null == wdapConvertorDao.findById(convertorId)) {
            throw new RuntimeException("转换器未发现：" + convertorId);
        }
        node.setModel(FlowNodeModel.convertor.name());
        node.setRid(convertorId);
        wdapFlowNodeDao.update(node);
    }

    @Override
    @Transactional
    public List<WdapFlow> getFlows(ResourceFile rfile) {
        List<WdapFlow> ableFlows = new ArrayList<>();
        List<WdapFlow> flows = wdapFlowDao.find(DBRuleList.getInstance().add(new DBRule("EXNAME", rfile.getExname(), "like")).toList());
        for (WdapFlow flow : flows) {
            List<String> exNames = Arrays.stream(flow.getExname().toUpperCase().split(",")).toList();
            if (exNames.contains(rfile.getExname().toUpperCase())) {
                if (flow.getSizemin() <= rfile.getFilesize() && flow.getSizemax() >= rfile.getFilesize()) {
                    ableFlows.add(flow);
                }
            }
        }
        return ableFlows;
    }

    @Override
    public String runFlow(String flowid, WdapTask task, String nodeId, ResourceFile rfile, Map<String, Object> context) {
        if (nodeId == null || nodeId.isEmpty()) {
            throw new IllegalArgumentException("Node ID cannot be null or empty");
        }

        // If starting node, get the next node
        if (nodeId.equals(START_NODE_ID)) {
            nodeId = getNextNode(flowid, nodeId);
        }

        WdapFlowNode node = wdapFlowNodeDao.findById(nodeId);
        if (node == null) {
            throw new RuntimeException("Node with ID " + nodeId + " not found");
        }

        WdapConvertor convertor = wdapConvertorDao.findById(node.getRid());
        if (convertor == null) {
            throw new RuntimeException("Converter with RID " + node.getRid() + " not found for node " + nodeId);
        }
        task = WdapJsonLogs.addLog(task, WdapJsonLogs.LogType.INFO, "开始执行node-" + convertor.getTitle());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<ConvertResult> future = null;
        long timeout = farmParameter.getLongParameter("farm2.config.wdap.task.timeout.default"); // Timeout in seconds
        if (node.getTimeout() > 0) {
            timeout = node.getTimeout();
        }
        try {
            FileConvertorInter convertorImpl = ConvertUtils.getConvertorImpl(convertor.getClasskey());
            context.put("nodeId", nodeId);
            Map<String, ConvertorParam> paramMap = ConvertUtils.getParams(convertor.getParams(), convertorImpl);
            // Submit the conversion task to the executor service
            future = executor.submit(() -> convertorImpl.runConvert(rfile, FileModelUtils.getModel(convertor.getSfilemodel()), convertor, paramMap, context, resourceFileServiceImpl));
            // Set a timeout for the conversion task

            ConvertResult result = future.get(timeout, TimeUnit.SECONDS);
            // Execute conversion task
            for (String modelkey : result.getFileModels()) {
                WdapExtendFile exFile = new WdapExtendFile();
                exFile.setTaskid(task.getId());
                exFile.setFileid(rfile.getId());
                exFile.setFilemodel(modelkey);
                exFile.setViewis("1");
                exFile.setCtime(FarmTimeTool.getTimeDate14());
                exFile.setState("1");
                exFile.setResourcekey(resourceFileServiceImpl.getExDirBasePath().getResourceKey());
                exFile.setServerid(Farm2ConfigUtils.getServiceId());
                wdapExtendFileDao.insert(exFile);
            }
        } catch (TimeoutException e) {
            String errorMessage = "转换器：" + convertor.getTitle() + ": 超时，超时时间为" + timeout + "秒";
            throw new RuntimeException(errorMessage, e);
        } catch (InterruptedException | ExecutionException e) {
            String errorMessage = "转换器：" + convertor.getTitle() + ":" + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        } finally {
            if (future != null && !future.isDone()) {
                future.cancel(true); // Cancel the future if it's still running
            }
            executor.shutdown(); // Shutdown the executor service
        }

        String nextNodeId = getNextNode(flowid, nodeId);
        if (nodeId.equals(nextNodeId)) {
            throw new RuntimeException("下一个节点不能与当前节点相同: " + nodeId);
        }

        return nextNodeId;
    }

    /**
     * 查找下一个节点
     *
     * @param nodeId
     * @return
     */
    private String getNextNode(String flowid, String nodeId) {
        List<WdapFlowLink> links = wdapFlowLinkDao.find(DBRuleList.getInstance().add(new DBRule("FLOWID", flowid, "=")).add(new DBRule("SNODEID", nodeId, "=")).toList());
        //TO DO 此处判断流程表达式
        if (links.size() <= 0) {
            throw new RuntimeException("未找到下级节点:" + nodeId);
        }
        return links.get(0).getTnodeid();
    }

    /**
     * 构造节点位置
     *
     * @param dic
     * @param data
     * @param endNode
     */
    private void loadNodePosition(Map<String, EchartsTaskNode> dic, EchartsTaskData data, EchartsTaskNode endNode) {
        int level = 1;
        int max = 0;
        // 每个leve中的全部节点
        List<EchartsTaskNode> tasks = new ArrayList<EchartsTaskNode>();

        tasks.add(new EchartsTaskNode(START_NODE_ID, START_NODE_ID, 0, 0, null));

        while (true) {
            List<EchartsTaskNode> lasttasks = new ArrayList<EchartsTaskNode>();
            for (EchartsTaskNode node : tasks) {
                // 当前任务
                String sid = node.getName();
                for (EchartsTaskLink link : data.getLinks()) {
                    if (sid.equals(link.getSource())) {
                        // 下一层任务列表
                        EchartsTaskNode cnode = dic.get(link.getTarget());
                        if (cnode != null) {
                            cnode.setX(10 + (level * 10));
                            lasttasks.add(cnode);
                        }
                    }
                }

            }
            level++;
            max++;
            tasks = lasttasks;
            // 計算高度
            {
                int all = lasttasks.size();

                int min = 300 + (level * 5);

                if (all > 1) {
                    if (level % 2 == 0) {
                        min = 300 + (level % 2 * 2);
                    } else {
                        min = 300 - (level % 2 * 2);
                    }
                } else {
                    min = 300;
                }

                if (all > 1) {
                    min = min - (all - 1) * 10 / 2;
                }
                int num = 0;
                for (EchartsTaskNode node : lasttasks) {
                    node.setY(min + (num * 10));
                    num++;
                }
            }

            if (max >= 100 || tasks.size() <= 0) {
                break;
            }
        }
        endNode.setX(10 + ((level - 1) * 10));
    }
}
