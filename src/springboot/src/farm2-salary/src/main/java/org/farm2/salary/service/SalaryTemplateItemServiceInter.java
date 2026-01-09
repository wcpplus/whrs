package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryTemplateItem;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**薪酬项 
 * @author cbtg自动生成  2026-1-7 10:48:10 
 */
public interface SalaryTemplateItemServiceInter {

    public SalaryTemplateItem insertSalaryTemplateItemEntity(SalaryTemplateItem salaryTemplateItem);

    public SalaryTemplateItem editSalaryTemplateItemEntity(SalaryTemplateItem salaryTemplateItem);

    public void delSalaryTemplateItem(String id);

    public SalaryTemplateItem getSalaryTemplateItemById(String id);

    public List<SalaryTemplateItem> getSalaryTemplateItems(DataQuery query);

    public DataResult searchSalaryTemplateItem(DataQuery query);

    public int getSalaryTemplateItemNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
