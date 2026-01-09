package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryUserItem;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**用户薪酬明细 
 * @author cbtg自动生成  2026-1-8 14:48:48 
 */
public interface SalaryUserItemServiceInter {

    public SalaryUserItem insertSalaryUserItemEntity(SalaryUserItem salaryUserItem);

    public SalaryUserItem editSalaryUserItemEntity(SalaryUserItem salaryUserItem);

    public void delSalaryUserItem(String id);

    public SalaryUserItem getSalaryUserItemById(String id);

    public List<SalaryUserItem> getSalaryUserItems(DataQuery query);

    public DataResult searchSalaryUserItem(DataQuery query);

    public int getSalaryUserItemNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
