package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryTemplate;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

/**薪酬模板 
 * @author cbtg自动生成  2026-1-7 10:20:27 
 */
public interface SalaryTemplateServiceInter {

    public SalaryTemplate insertSalaryTemplateEntity(SalaryTemplate salaryTemplate);

    public SalaryTemplate editSalaryTemplateEntity(SalaryTemplate salaryTemplate);

    public void delSalaryTemplate(String id);

    public SalaryTemplate getSalaryTemplateById(String id);

    public List<SalaryTemplate> getSalaryTemplates(DataQuery query);

    public DataResult searchSalaryTemplate(DataQuery query);

    public int getSalaryTemplateNum(DataQuery query);
    
    public int getNum(DataQuery query);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
