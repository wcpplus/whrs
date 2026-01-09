package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryUser;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**
 * 用户薪酬WHRS_SALARY_USER
 *
 * @author cbtg自动生成  2026-1-8 11:14:26
 */
public interface SalaryUserServiceInter {

    public SalaryUser insertSalaryUserEntity(SalaryUser salaryUser);

    public SalaryUser editSalaryUserEntity(SalaryUser salaryUser);

    public void delSalaryUser(String id);

    public SalaryUser getSalaryUserById(String id);

    public List<SalaryUser> getSalaryUsers(DataQuery query);

    public DataResult searchSalaryUser(DataQuery query);

    public int getSalaryUserNum(DataQuery query);

    public int getNum(DataQuery query);

    public SalaryUser initSalaryUser(String loginname);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
