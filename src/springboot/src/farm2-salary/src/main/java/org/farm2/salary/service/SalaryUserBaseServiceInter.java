package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryUserBase;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 用户薪酬定义
 *
 * @author cbtg自动生成  2026-1-8 14:45:42
 */
public interface SalaryUserBaseServiceInter {

    public SalaryUserBase insertSalaryUserBaseEntity(SalaryUserBase salaryUserBase);

    public SalaryUserBase editSalaryUserBaseEntity(SalaryUserBase salaryUserBase);

    public void delSalaryUserBase(String id);

    public SalaryUserBase getSalaryUserBaseById(String id);

    public List<SalaryUserBase> getSalaryUserBases(DataQuery query);

    public DataResult searchSalaryUserBase(DataQuery query);

    public int getSalaryUserBaseNum(DataQuery query);

    public int getNum(DataQuery query);

    /**
     * 从模板中加载所有薪资参数
     *
     * @param salarytime
     * @param userkey
     */
    public void initUserBaseParas(String salarytime, String userkey);

    /**计算用户薪资
     * @param userSalaryUnitId
     */
    public void runSalary(String userSalaryUnitId);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
