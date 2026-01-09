package org.farm2.wdap.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.wdap.dao.WdapFlowNodeDao;
import org.farm2.wdap.domain.WdapFlowNode;
import org.farm2.wdap.service.WdapFlowNodeServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**流程节点 
 * @author cbtg自动生成  2025-1-21 22:39:41 
 */
@Service
@Slf4j
public class WdapFlowNodeServiceImpl implements WdapFlowNodeServiceInter {


    @Autowired
    private WdapFlowNodeDao wdapFlowNodeDao;

    @Transactional
    @Override
    public WdapFlowNode insertWdapFlowNodeEntity(WdapFlowNode wdapFlowNode) {
        FarmDbFields.initInsertBean(wdapFlowNode, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(wdapFlowNode.getType(), "1", wdapFlowNode::setType);
        wdapFlowNodeDao.insert(wdapFlowNode);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return wdapFlowNode;
    }

    @Transactional
    @Override
    public WdapFlowNode editWdapFlowNodeEntity(WdapFlowNode wdapFlowNode) {
        WdapFlowNode saveWdapFlowNode = getWdapFlowNodeById(wdapFlowNode.getId());
        FarmExceptionUtils.throwNullEx(saveWdapFlowNode, I18n.msg("流程节点不存在:?", wdapFlowNode.getId()));
        saveWdapFlowNode.setId(wdapFlowNode.getId());
        saveWdapFlowNode.setPcontent(wdapFlowNode.getPcontent());
        saveWdapFlowNode.setModel(wdapFlowNode.getModel());
        saveWdapFlowNode.setRid(wdapFlowNode.getRid());
        saveWdapFlowNode.setFlowid(wdapFlowNode.getFlowid());
        saveWdapFlowNode.setTimeout(wdapFlowNode.getTimeout());
        FarmDbFields.initUpdateBean(saveWdapFlowNode, FarmUserContextLoader.getCurrentUser());
        wdapFlowNodeDao.update(saveWdapFlowNode);
        return saveWdapFlowNode;
    }

    @Transactional
    @Override
    public WdapFlowNode getWdapFlowNodeById(String id) {
        return wdapFlowNodeDao.findById(id);
    }

    @Override
    public List<WdapFlowNode> getWdapFlowNodes(DataQuery query) {
        return wdapFlowNodeDao.queryData(query.setCount(false)).getData(WdapFlowNode.class);
    }



    @Transactional
    @Override
    public DataResult searchWdapFlowNode(DataQuery query) {
        DataResult result = wdapFlowNodeDao.queryData(query);
        return result;
    }

    @Override
    public int getWdapFlowNodeNum(DataQuery query) {
        return wdapFlowNodeDao.countData(query);
    }


    @Transactional
    @Override
    public void delWdapFlowNode(String id) {
        /*[tree：树形结构使用]
        if ( wdapFlowNodeDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        wdapFlowNodeDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  wdapFlowNodeDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            WdapFlowNode node = getWdapFlowNodeById(sourceId);
            if (!"NONE".equals(targetId)) {
                WdapFlowNode target = getWdapFlowNodeById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            wdapFlowNodeDao.update(node);
            // 构造所有树TREECODE
            List<WdapFlowNode> list = wdapFlowNodeDao.findSubNodes(sourceId);
            for (WdapFlowNode treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        WdapFlowNode node = wdapFlowNodeDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(wdapFlowNodeDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        wdapFlowNodeDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            WdapFlowNode node =  wdapFlowNodeDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            wdapFlowNodeDao.update(node);
        }
    }*/
}
