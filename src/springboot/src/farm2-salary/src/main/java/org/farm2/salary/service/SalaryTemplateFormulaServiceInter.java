package org.farm2.salary.service;

import org.farm2.salary.domain.SalaryTemplateFormula;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 计算规则
 *
 * @author cbtg自动生成  2026-1-7 13:08:45
 */
public interface SalaryTemplateFormulaServiceInter {

    public SalaryTemplateFormula insertSalaryTemplateFormulaEntity(SalaryTemplateFormula salaryTemplateFormula);

    public SalaryTemplateFormula editSalaryTemplateFormulaEntity(SalaryTemplateFormula salaryTemplateFormula);

    public void delSalaryTemplateFormula(String id);

    public SalaryTemplateFormula getSalaryTemplateFormulaById(String id);

    public List<SalaryTemplateFormula> getSalaryTemplateFormulas(DataQuery query);

    public DataResult searchSalaryTemplateFormula(DataQuery query);

    public int getSalaryTemplateFormulaNum(DataQuery query);

    public int getNum(DataQuery query);

    BigDecimal runFormula(Map<String, Object> env, String formula);

    public Map<String, Object> runAll(String templateId, Map<String, Object> env);
    /*[tree：树形结构使用]
    public void moveTreeNode(List<String> ids, String id);
    
    void autoSort(List<String> ids);
    */
}
