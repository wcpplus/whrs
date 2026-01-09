package org.farm2.salary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.salary.dao.SalaryTemplateDao;
import org.farm2.salary.domain.SalaryTemplate;
import org.farm2.salary.service.SalaryTemplateServiceInter;
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

/**薪酬模板 
 * @author cbtg自动生成  2026-1-7 10:20:27 
 */
@Service
@Slf4j
public class SalaryTemplateServiceImpl implements SalaryTemplateServiceInter {


    @Autowired
    private SalaryTemplateDao salaryTemplateDao;

    @Transactional
    @Override
    public SalaryTemplate insertSalaryTemplateEntity(SalaryTemplate salaryTemplate) {
        FarmDbFields.initInsertBean(salaryTemplate, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryTemplate.getType(), "1", salaryTemplate::setType);
        salaryTemplateDao.insert(salaryTemplate);
       //[tree：树形结构使用]
       //initTreeCode(actions.getId());
        return salaryTemplate;
    }

    @Transactional
    @Override
    public SalaryTemplate editSalaryTemplateEntity(SalaryTemplate salaryTemplate) {
        SalaryTemplate saveSalaryTemplate = getSalaryTemplateById(salaryTemplate.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryTemplate, I18n.msg("薪酬模板不存在:?", salaryTemplate.getId()));
        saveSalaryTemplate.setId(salaryTemplate.getId());
        saveSalaryTemplate.setCuser(salaryTemplate.getCuser());
        saveSalaryTemplate.setCtime(salaryTemplate.getCtime());
        saveSalaryTemplate.setState(salaryTemplate.getState());
        saveSalaryTemplate.setNote(salaryTemplate.getNote());
        saveSalaryTemplate.setName(salaryTemplate.getName());
         
        FarmDbFields.initUpdateBean(saveSalaryTemplate, FarmUserContextLoader.getCurrentUser());
        salaryTemplateDao.update(saveSalaryTemplate);
        return saveSalaryTemplate;
    }

    @Transactional
    @Override
    public SalaryTemplate getSalaryTemplateById(String id) {
        return salaryTemplateDao.findById(id);
    }

    @Override
    public List<SalaryTemplate> getSalaryTemplates(DataQuery query) {
        return salaryTemplateDao.queryData(query.setCount(false)).getData(SalaryTemplate.class);
    }



    @Transactional
    @Override
    public DataResult searchSalaryTemplate(DataQuery query) {
        DataResult result = salaryTemplateDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryTemplateNum(DataQuery query) {
        return salaryTemplateDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryTemplate(String id) {
        /*[tree：树形结构使用]
        if ( salaryTemplateDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryTemplateDao.deleteById(id);
    }
    
    @Override
    public int getNum(DataQuery query) {
        return  salaryTemplateDao.countData(query);
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            SalaryTemplate node = getSalaryTemplateById(sourceId);
            if (!"NONE".equals(targetId)) {
                SalaryTemplate target = getSalaryTemplateById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            salaryTemplateDao.update(node);
            // 构造所有树TREECODE
            List<SalaryTemplate> list = salaryTemplateDao.findSubNodes(sourceId);
            for (SalaryTemplate treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/
    
     /**[tree：树形结构使用]
      * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
        SalaryTemplate node = salaryTemplateDao.findById(treeNodeId);
        if (node.getParentid().equals("NONE")) {
            node.setTreecode(node.getId());
        } else {
            node.setTreecode(salaryTemplateDao.findById(node.getParentid()).getTreecode() + node.getId());
        }
        salaryTemplateDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            SalaryTemplate node =  salaryTemplateDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            salaryTemplateDao.update(node);
        }
    }*/
}
