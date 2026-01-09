package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryUserUnit;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 用户薪资
 *
 * @author cbtg自动生成  2026-1-8 14:47:36
 */
public interface SalaryUserUnitServiceInter {

    public SalaryUserUnit insertSalaryUserUnitEntity(SalaryUserUnit salaryUserUnit);

    public SalaryUserUnit editSalaryUserUnitEntity(SalaryUserUnit salaryUserUnit);

    public void delSalaryUserUnit(String id);

    public SalaryUserUnit getSalaryUserUnitById(String id);

    public List<SalaryUserUnit> getSalaryUserUnits(DataQuery query);

    public DataResult searchSalaryUserUnit(DataQuery query);

    public int getSalaryUserUnitNum(DataQuery query);

    public int getNum(DataQuery query);

    /**
     * 初始化用户当月薪资
     *
     * @param loginname
     * @param yyyymm
     * @return
     */
    public SalaryUserUnit initUnit(String loginname, String yyyymm);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
