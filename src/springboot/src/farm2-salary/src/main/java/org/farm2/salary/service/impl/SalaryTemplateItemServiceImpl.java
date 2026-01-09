package org.farm2.salary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.salary.dao.SalaryTemplateItemDao;
import org.farm2.salary.domain.SalaryTemplateItem;
import org.farm2.salary.service.SalaryTemplateItemServiceInter;
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

/**薪酬项 
 * @author cbtg自动生成  2026-1-7 10:48:10 
 */
@Service
@Slf4j
public class SalaryTemplateItemServiceImpl implements SalaryTemplateItemServiceInter {


    @Autowired
    private SalaryTemplateItemDao salaryTemplateItemDao;

    @Transactional
    @Override
    public SalaryTemplateItem insertSalaryTemplateItemEntity(SalaryTemplateItem salaryTemplateItem) {
        FarmDbFields.initInsertBean(salaryTemplateItem, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryTemplateItem.getType(), "1", salaryTemplateItem::setType);
        salaryTemplateItemDao.insert(salaryTemplateItem);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return salaryTemplateItem;
    }

    @Transactional
    @Override
    public SalaryTemplateItem editSalaryTemplateItemEntity(SalaryTemplateItem salaryTemplateItem) {
        SalaryTemplateItem saveSalaryTemplateItem = getSalaryTemplateItemById(salaryTemplateItem.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryTemplateItem, I18n.msg("薪酬项不存在:?", salaryTemplateItem.getId()));
        saveSalaryTemplateItem.setId(salaryTemplateItem.getId());
        saveSalaryTemplateItem.setNote(salaryTemplateItem.getNote());
        saveSalaryTemplateItem.setName(salaryTemplateItem.getName());
        saveSalaryTemplateItem.setDefaultval(salaryTemplateItem.getDefaultval());
        saveSalaryTemplateItem.setKeycode(salaryTemplateItem.getKeycode());
        saveSalaryTemplateItem.setComponenttype(salaryTemplateItem.getComponenttype());
        saveSalaryTemplateItem.setSourcemodel(salaryTemplateItem.getSourcemodel());
        saveSalaryTemplateItem.setUserover(salaryTemplateItem.getUserover());
        saveSalaryTemplateItem.setSortcode(salaryTemplateItem.getSortcode());
        saveSalaryTemplateItem.setTemplateid(salaryTemplateItem.getTemplateid());
        saveSalaryTemplateItem.setShowmodel(salaryTemplateItem.getShowmodel());
         
        FarmDbFields.initUpdateBean(saveSalaryTemplateItem, FarmUserContextLoader.getCurrentUser());
        salaryTemplateItemDao.update(saveSalaryTemplateItem);
        return saveSalaryTemplateItem;
    }

    @Transactional
    @Override
    public SalaryTemplateItem getSalaryTemplateItemById(String id) {
        return salaryTemplateItemDao.findById(id);
    }

    @Override
    public List<SalaryTemplateItem> getSalaryTemplateItems(DataQuery query) {
        return salaryTemplateItemDao.queryData(query.setCount(false)).getData(SalaryTemplateItem.class);
    }



    @Transactional
    @Override
    public DataResult searchSalaryTemplateItem(DataQuery query) {
        DataResult result = salaryTemplateItemDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryTemplateItemNum(DataQuery query) {
        return salaryTemplateItemDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryTemplateItem(String id) {
        /*[tree：树形结构使用]
        if ( salaryTemplateItemDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryTemplateItemDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  salaryTemplateItemDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            SalaryTemplateItem node = getSalaryTemplateItemById(sourceId);
            if (!"NONE".equals(targetId)) {
                SalaryTemplateItem target = getSalaryTemplateItemById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            salaryTemplateItemDao.update(node);
            // 构造所有树TREECODE
            List<SalaryTemplateItem> list = salaryTemplateItemDao.findSubNodes(sourceId);
            for (SalaryTemplateItem treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        SalaryTemplateItem node = salaryTemplateItemDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(salaryTemplateItemDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        salaryTemplateItemDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            SalaryTemplateItem node =  salaryTemplateItemDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            salaryTemplateItemDao.update(node);
        }
    }*/
}
