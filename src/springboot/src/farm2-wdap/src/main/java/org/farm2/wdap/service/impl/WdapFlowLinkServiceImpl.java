package org.farm2.wdap.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.wdap.dao.WdapFlowLinkDao;
import org.farm2.wdap.domain.WdapFlowLink;
import org.farm2.wdap.service.WdapFlowLinkServiceInter;
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

/**流程连线 
 * @author cbtg自动生成  2025-1-21 22:42:51 
 */
@Service
@Slf4j
public class WdapFlowLinkServiceImpl implements WdapFlowLinkServiceInter {


    @Autowired
    private WdapFlowLinkDao wdapFlowLinkDao;

    @Transactional
    @Override
    public WdapFlowLink insertWdapFlowLinkEntity(WdapFlowLink wdapFlowLink) {
        FarmDbFields.initInsertBean(wdapFlowLink, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(wdapFlowLink.getType(), "1", wdapFlowLink::setType);
        wdapFlowLinkDao.insert(wdapFlowLink);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return wdapFlowLink;
    }

    @Transactional
    @Override
    public WdapFlowLink editWdapFlowLinkEntity(WdapFlowLink wdapFlowLink) {
        WdapFlowLink saveWdapFlowLink = getWdapFlowLinkById(wdapFlowLink.getId());
        FarmExceptionUtils.throwNullEx(saveWdapFlowLink, I18n.msg("流程连线不存在:?", wdapFlowLink.getId()));
        saveWdapFlowLink.setId(wdapFlowLink.getId());
        saveWdapFlowLink.setPcontent(wdapFlowLink.getPcontent());
        saveWdapFlowLink.setSnodeid(wdapFlowLink.getSnodeid());
        saveWdapFlowLink.setTnodeid(wdapFlowLink.getTnodeid());
        saveWdapFlowLink.setFlowid(wdapFlowLink.getFlowid());
        saveWdapFlowLink.setExpression(wdapFlowLink.getExpression());
         
        FarmDbFields.initUpdateBean(saveWdapFlowLink, FarmUserContextLoader.getCurrentUser());
        wdapFlowLinkDao.update(saveWdapFlowLink);
        return saveWdapFlowLink;
    }

    @Transactional
    @Override
    public WdapFlowLink getWdapFlowLinkById(String id) {
        return wdapFlowLinkDao.findById(id);
    }

    @Override
    public List<WdapFlowLink> getWdapFlowLinks(DataQuery query) {
        return wdapFlowLinkDao.queryData(query.setCount(false)).getData(WdapFlowLink.class);
    }



    @Transactional
    @Override
    public DataResult searchWdapFlowLink(DataQuery query) {
        DataResult result = wdapFlowLinkDao.queryData(query);
        return result;
    }

    @Override
    public int getWdapFlowLinkNum(DataQuery query) {
        return wdapFlowLinkDao.countData(query);
    }


    @Transactional
    @Override
    public void delWdapFlowLink(String id) {
        /*[tree：树形结构使用]
        if ( wdapFlowLinkDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        wdapFlowLinkDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  wdapFlowLinkDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            WdapFlowLink node = getWdapFlowLinkById(sourceId);
            if (!"NONE".equals(targetId)) {
                WdapFlowLink target = getWdapFlowLinkById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            wdapFlowLinkDao.update(node);
            // 构造所有树TREECODE
            List<WdapFlowLink> list = wdapFlowLinkDao.findSubNodes(sourceId);
            for (WdapFlowLink treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        WdapFlowLink node = wdapFlowLinkDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(wdapFlowLinkDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        wdapFlowLinkDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            WdapFlowLink node =  wdapFlowLinkDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            wdapFlowLinkDao.update(node);
        }
    }*/
}
