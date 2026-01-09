package org.farm2.salary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.salary.dao.SalaryUserItemDao;
import org.farm2.salary.domain.SalaryUserItem;
import org.farm2.salary.service.SalaryUserItemServiceInter;
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

/**用户薪酬明细 
 * @author cbtg自动生成  2026-1-8 14:48:48 
 */
@Service
@Slf4j
public class SalaryUserItemServiceImpl implements SalaryUserItemServiceInter {


    @Autowired
    private SalaryUserItemDao salaryUserItemDao;

    @Transactional
    @Override
    public SalaryUserItem insertSalaryUserItemEntity(SalaryUserItem salaryUserItem) {
        FarmDbFields.initInsertBean(salaryUserItem, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryUserItem.getType(), "1", salaryUserItem::setType);
        salaryUserItemDao.insert(salaryUserItem);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return salaryUserItem;
    }

    @Transactional
    @Override
    public SalaryUserItem editSalaryUserItemEntity(SalaryUserItem salaryUserItem) {
        SalaryUserItem saveSalaryUserItem = getSalaryUserItemById(salaryUserItem.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryUserItem, I18n.msg("用户薪酬明细不存在:?", salaryUserItem.getId()));
        saveSalaryUserItem.setId(salaryUserItem.getId());
        saveSalaryUserItem.setName(salaryUserItem.getName());
        saveSalaryUserItem.setKeycode(salaryUserItem.getKeycode());
        saveSalaryUserItem.setCtime(salaryUserItem.getCtime());
        saveSalaryUserItem.setVal(salaryUserItem.getVal());
        saveSalaryUserItem.setShowmodel(salaryUserItem.getShowmodel());
        saveSalaryUserItem.setSalarytime(salaryUserItem.getSalarytime());
        saveSalaryUserItem.setUserkey(salaryUserItem.getUserkey());
        saveSalaryUserItem.setUsername(salaryUserItem.getUsername());
         
        FarmDbFields.initUpdateBean(saveSalaryUserItem, FarmUserContextLoader.getCurrentUser());
        salaryUserItemDao.update(saveSalaryUserItem);
        return saveSalaryUserItem;
    }

    @Transactional
    @Override
    public SalaryUserItem getSalaryUserItemById(String id) {
        return salaryUserItemDao.findById(id);
    }

    @Override
    public List<SalaryUserItem> getSalaryUserItems(DataQuery query) {
        return salaryUserItemDao.queryData(query.setCount(false)).getData(SalaryUserItem.class);
    }



    @Transactional
    @Override
    public DataResult searchSalaryUserItem(DataQuery query) {
        DataResult result = salaryUserItemDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryUserItemNum(DataQuery query) {
        return salaryUserItemDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryUserItem(String id) {
        /*[tree：树形结构使用]
        if ( salaryUserItemDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryUserItemDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  salaryUserItemDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            SalaryUserItem node = getSalaryUserItemById(sourceId);
            if (!"NONE".equals(targetId)) {
                SalaryUserItem target = getSalaryUserItemById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            salaryUserItemDao.update(node);
            // 构造所有树TREECODE
            List<SalaryUserItem> list = salaryUserItemDao.findSubNodes(sourceId);
            for (SalaryUserItem treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        SalaryUserItem node = salaryUserItemDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(salaryUserItemDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        salaryUserItemDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            SalaryUserItem node =  salaryUserItemDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            salaryUserItemDao.update(node);
        }
    }*/
}
