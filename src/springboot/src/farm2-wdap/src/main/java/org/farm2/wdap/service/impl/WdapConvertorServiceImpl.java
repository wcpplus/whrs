package org.farm2.wdap.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.dao.WdapConvertorDao;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.wdap.service.WdapConvertorServiceInter;
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

/**
 * 文件转换器
 *
 * @author cbtg自动生成  2025-1-24 10:22:07
 */
@Service
@Slf4j
public class WdapConvertorServiceImpl implements WdapConvertorServiceInter {


    @Autowired
    private WdapConvertorDao wdapConvertorDao;

    @Transactional
    @Override
    public WdapConvertor insertWdapConvertorEntity(WdapConvertor wdapConvertor) {
        FarmDbFields.initInsertBean(wdapConvertor, FarmUserContextLoader.getCurrentUser());
        FileConvertorInter impl = ConvertUtils.getConvertorImpl(wdapConvertor.getClasskey());
        wdapConvertor.setTitle(impl.getTitle());
        wdapConvertor.setTfilemodel(impl.getTargetFileModel().getKey());
        wdapConvertorDao.insert(wdapConvertor);
        return wdapConvertor;
    }

    @Transactional
    @Override
    public WdapConvertor editWdapConvertorEntity(WdapConvertor wdapConvertor) {
        WdapConvertor saveWdapConvertor = getWdapConvertorById(wdapConvertor.getId());
        FarmExceptionUtils.throwNullEx(saveWdapConvertor, I18n.msg("文件转换器不存在:?", wdapConvertor.getId()));
        // saveWdapConvertor.setId(wdapConvertor.getId());
        //  saveWdapConvertor.setClasskey(wdapConvertor.getClasskey());
        // saveWdapConvertor.setSfilemodel(wdapConvertor.getSfilemodel());
        // saveWdapConvertor.setTfilemodel(wdapConvertor.getTfilemodel());
        // saveWdapConvertor.setTitle(wdapConvertor.getTitle());
        saveWdapConvertor.setTitle(wdapConvertor.getTitle());
        saveWdapConvertor.setState(wdapConvertor.getState());
        saveWdapConvertor.setNote(wdapConvertor.getNote());
        saveWdapConvertor.setParams(wdapConvertor.getParams());

        FarmDbFields.initUpdateBean(saveWdapConvertor, FarmUserContextLoader.getCurrentUser());
        wdapConvertorDao.update(saveWdapConvertor);
        return saveWdapConvertor;
    }

    @Transactional
    @Override
    public WdapConvertor getWdapConvertorById(String id) {
        return wdapConvertorDao.findById(id);
    }

    @Override
    public List<WdapConvertor> getWdapConvertors(DataQuery query) {
        return wdapConvertorDao.queryData(query.setCount(false)).getData(WdapConvertor.class);
    }


    @Transactional
    @Override
    public DataResult searchWdapConvertor(DataQuery query) {
        DataResult result = wdapConvertorDao.queryData(query);
        return result;
    }

    @Override
    public int getWdapConvertorNum(DataQuery query) {
        return wdapConvertorDao.countData(query);
    }


    @Transactional
    @Override
    public void delWdapConvertor(String id) {
        /*[tree：树形结构使用]
        if ( wdapConvertorDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        wdapConvertorDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return wdapConvertorDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            WdapConvertor node = getWdapConvertorById(sourceId);
            if (!"NONE".equals(targetId)) {
                WdapConvertor target = getWdapConvertorById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            wdapConvertorDao.update(node);
            // 构造所有树TREECODE
            List<WdapConvertor> list = wdapConvertorDao.findSubNodes(sourceId);
            for (WdapConvertor treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    WdapConvertor node = wdapConvertorDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(wdapConvertorDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    wdapConvertorDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            WdapConvertor node =  wdapConvertorDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            wdapConvertorDao.update(node);
        }
    }*/
}
