package org.farm2.salary.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.luser.domain.LocalUser;
import org.farm2.salary.dao.SalaryUserUnitDao;
import org.farm2.salary.domain.SalaryUser;
import org.farm2.salary.domain.SalaryUserUnit;
import org.farm2.salary.service.SalaryUserUnitServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.time.FarmTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 用户薪资
 *
 * @author cbtg自动生成  2026-1-8 14:47:36
 */
@Service
@Slf4j
public class SalaryUserUnitServiceImpl implements SalaryUserUnitServiceInter {


    @Autowired
    private SalaryUserUnitDao salaryUserUnitDao;

    @Transactional
    @Override
    public SalaryUserUnit insertSalaryUserUnitEntity(SalaryUserUnit salaryUserUnit) {
        FarmDbFields.initInsertBean(salaryUserUnit, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryUserUnit.getType(), "1", salaryUserUnit::setType);
        salaryUserUnitDao.insert(salaryUserUnit);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return salaryUserUnit;
    }

    @Transactional
    @Override
    public SalaryUserUnit editSalaryUserUnitEntity(SalaryUserUnit salaryUserUnit) {
        SalaryUserUnit saveSalaryUserUnit = getSalaryUserUnitById(salaryUserUnit.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryUserUnit, I18n.msg("用户薪资不存在:?", salaryUserUnit.getId()));
        saveSalaryUserUnit.setId(salaryUserUnit.getId());
        saveSalaryUserUnit.setSalarytime(salaryUserUnit.getSalarytime());
        saveSalaryUserUnit.setUserkey(salaryUserUnit.getUserkey());
        saveSalaryUserUnit.setUsername(salaryUserUnit.getUsername());
        saveSalaryUserUnit.setCtime(salaryUserUnit.getCtime());
        saveSalaryUserUnit.setNote(salaryUserUnit.getNote());
        saveSalaryUserUnit.setState(salaryUserUnit.getState());
        saveSalaryUserUnit.setGrosspay(salaryUserUnit.getGrosspay());
        saveSalaryUserUnit.setNetpay(salaryUserUnit.getNetpay());
        saveSalaryUserUnit.setTaxamount(salaryUserUnit.getTaxamount());

        FarmDbFields.initUpdateBean(saveSalaryUserUnit, FarmUserContextLoader.getCurrentUser());
        salaryUserUnitDao.update(saveSalaryUserUnit);
        return saveSalaryUserUnit;
    }

    @Transactional
    @Override
    public SalaryUserUnit getSalaryUserUnitById(String id) {
        return salaryUserUnitDao.findById(id);
    }

    @Override
    public List<SalaryUserUnit> getSalaryUserUnits(DataQuery query) {
        return salaryUserUnitDao.queryData(query.setCount(false)).getData(SalaryUserUnit.class);
    }


    @Transactional
    @Override
    public DataResult searchSalaryUserUnit(DataQuery query) {
        DataResult result = salaryUserUnitDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryUserUnitNum(DataQuery query) {
        return salaryUserUnitDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryUserUnit(String id) {
        /*[tree：树形结构使用]
        if ( salaryUserUnitDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryUserUnitDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return salaryUserUnitDao.countData(query);
    }

    @Override
    public SalaryUserUnit initUnit(String loginname, String yyyymm) {
        if (yyyymm.length() != 6) {
            throw new RuntimeException("周期描述错误" + yyyymm);
        }
        SalaryUserUnit salaryUnit = salaryUserUnitDao.queryOne(DBRuleList.getInstance()
                .add(new DBRule("USERKEY", loginname, "="))
                .add(new DBRule("SALARYTIME", yyyymm, "="))
                .toList());
        if (salaryUnit == null) {
            salaryUnit = new SalaryUserUnit();
            salaryUnit.setSalarytime(yyyymm);
            salaryUnit.setUserkey(loginname);
            salaryUnit.setUsername(loginname);
            salaryUnit.setCtime(FarmTimeTool.getTimeDate14());
            salaryUnit.setState("0");
            salaryUnit.setGrosspay(BigDecimal.valueOf(0.0));
            salaryUnit.setNetpay(BigDecimal.valueOf(0.0));
            salaryUnit.setTaxamount(BigDecimal.valueOf(0.0));
            salaryUserUnitDao.insert(salaryUnit);
        }
        return salaryUnit;
    }
    
    /*[tree：树形结构使用]
    @Transactional
    @Override
    public void moveTreeNode(List<String> sourceIds, String targetId) {
        for (String sourceId : sourceIds) {
            // 移动节点
            SalaryUserUnit node = getSalaryUserUnitById(sourceId);
            if (!"NONE".equals(targetId)) {
                SalaryUserUnit target = getSalaryUserUnitById(targetId);
                if (target.getTreecode().indexOf(node.getTreecode()) >= 0) {
                    throw new RuntimeException("不能够移动到其子节点下!");
                }
            }
            node.setParentid(targetId);
            salaryUserUnitDao.update(node);
            // 构造所有树TREECODE
            List<SalaryUserUnit> list = salaryUserUnitDao.findSubNodes(sourceId);
            for (SalaryUserUnit treenode : list) {
                initTreeCode(treenode.getId());
            }
        }
    }*/

    /**[tree：树形结构使用]
     * 构造treecode字段
     * @param treeNodeId
    private void initTreeCode(String treeNodeId) {
    SalaryUserUnit node = salaryUserUnitDao.findById(treeNodeId);
    if (node.getParentid().equals("NONE")) {
    node.setTreecode(node.getId());
    } else {
    node.setTreecode(salaryUserUnitDao.findById(node.getParentid()).getTreecode() + node.getId());
    }
    salaryUserUnitDao.update(node);
    }
     */
    /* [tree：树形结构使用]
    @Transactional
    @Override
    public void autoSort(List<String> ids) {
        int sort = 0;
        for (String id : ids) {
            SalaryUserUnit node =  salaryUserUnitDao.findById(id);
            if (sort == 0) {
                sort = node.getSortcode();
            }
            node.setSortcode(sort++);
            salaryUserUnitDao.update(node);
        }
    }*/
}
