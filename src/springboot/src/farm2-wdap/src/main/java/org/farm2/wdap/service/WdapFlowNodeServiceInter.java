package org.farm2.wdap.service;

import org.farm2.wdap.domain.WdapFlowNode;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**流程节点 
 * @author cbtg自动生成  2025-1-21 22:39:41 
 */
public interface WdapFlowNodeServiceInter {

    public WdapFlowNode insertWdapFlowNodeEntity(WdapFlowNode wdapFlowNode);

    public WdapFlowNode editWdapFlowNodeEntity(WdapFlowNode wdapFlowNode);

    public void delWdapFlowNode(String id);

    public WdapFlowNode getWdapFlowNodeById(String id);

    public List<WdapFlowNode> getWdapFlowNodes(DataQuery query);

    public DataResult searchWdapFlowNode(DataQuery query);

    public int getWdapFlowNodeNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
