package org.farm2.salary.service.impl;

import com.googlecode.aviator.AviatorEvaluator;
import lombok.extern.slf4j.Slf4j;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.salary.dao.SalaryTemplateFormulaDao;
import org.farm2.salary.domain.SalaryTemplateFormula;
import org.farm2.salary.domain.SalaryTemplateItem;
import org.farm2.salary.service.SalaryTemplateFormulaServiceInter;
import org.farm2.salary.service.SalaryTemplateItemServiceInter;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算规则
 *
 * @author cbtg自动生成  2026-1-7 13:08:45
 */
@Service
@Slf4j
public class SalaryTemplateFormulaServiceImpl implements SalaryTemplateFormulaServiceInter {
    @Autowired
    private SalaryTemplateItemServiceInter salaryTemplateItemServiceImpl;

    @Autowired
    private SalaryTemplateFormulaDao salaryTemplateFormulaDao;

    @Transactional
    @Override
    public SalaryTemplateFormula insertSalaryTemplateFormulaEntity(SalaryTemplateFormula salaryTemplateFormula) {
        FarmDbFields.initInsertBean(salaryTemplateFormula, FarmUserContextLoader.getCurrentUser());
        //FarmBeanUtils.runFunctionByBlank(salaryTemplateFormula.getType(), "1", salaryTemplateFormula::setType);
        salaryTemplateFormulaDao.insert(salaryTemplateFormula);
        //[tree：树形结构使用]
        //initTreeCode(actions.getId());
        return salaryTemplateFormula;
    }

    @Transactional
    @Override
    public SalaryTemplateFormula editSalaryTemplateFormulaEntity(SalaryTemplateFormula salaryTemplateFormula) {
        SalaryTemplateFormula saveSalaryTemplateFormula = getSalaryTemplateFormulaById(salaryTemplateFormula.getId());
        FarmExceptionUtils.throwNullEx(saveSalaryTemplateFormula, I18n.msg("计算规则不存在:?", salaryTemplateFormula.getId()));
        saveSalaryTemplateFormula.setId(salaryTemplateFormula.getId());
        saveSalaryTemplateFormula.setName(salaryTemplateFormula.getName());
        saveSalaryTemplateFormula.setRuleval(salaryTemplateFormula.getRuleval());
        saveSalaryTemplateFormula.setValname(salaryTemplateFormula.getValname());
        saveSalaryTemplateFormula.setValcode(salaryTemplateFormula.getValcode());
        saveSalaryTemplateFormula.setTemplateid(salaryTemplateFormula.getTemplateid());
        saveSalaryTemplateFormula.setShowmodel(salaryTemplateFormula.getShowmodel());
        saveSalaryTemplateFormula.setSortcode(salaryTemplateFormula.getSortcode());
        saveSalaryTemplateFormula.setStepcode(salaryTemplateFormula.getStepcode());
        saveSalaryTemplateFormula.setNote(salaryTemplateFormula.getNote());

        FarmDbFields.initUpdateBean(saveSalaryTemplateFormula, FarmUserContextLoader.getCurrentUser());
        salaryTemplateFormulaDao.update(saveSalaryTemplateFormula);
        return saveSalaryTemplateFormula;
    }

    @Transactional
    @Override
    public SalaryTemplateFormula getSalaryTemplateFormulaById(String id) {
        return salaryTemplateFormulaDao.findById(id);
    }

    @Override
    public List<SalaryTemplateFormula> getSalaryTemplateFormulas(DataQuery query) {
        return salaryTemplateFormulaDao.queryData(query.setCount(false)).getData(SalaryTemplateFormula.class);
    }


    @Transactional
    @Override
    public DataResult searchSalaryTemplateFormula(DataQuery query) {
        DataResult result = salaryTemplateFormulaDao.queryData(query);
        return result;
    }

    @Override
    public int getSalaryTemplateFormulaNum(DataQuery query) {
        return salaryTemplateFormulaDao.countData(query);
    }


    @Transactional
    @Override
    public void delSalaryTemplateFormula(String id) {
        /*[tree：树形结构使用]
        if ( salaryTemplateFormulaDao.findByParentId(id).size() > 0) {
            throw new RuntimeException("不能删除该节点，请先删除其子节点");
        }
        */
        salaryTemplateFormulaDao.deleteById(id);
    }

    @Override
    public int getNum(DataQuery query) {
        return salaryTemplateFormulaDao.countData(query);
    }

    /**
     * Map<String, Object> env = new HashMap<>();
     * env.put("baseSalary", ctx.getBaseSalary());
     * env.put("performance", ctx.getPerformance());
     * env.put("overtimeHours", ctx.getOvertimeHours());
     * env.put("overtimeRate", ctx.getOvertimeRate());
     * env.put("allowance", ctx.getAllowance());
     *
     * @param env
     * @param formula
     * @return
     */
    @Override
    public BigDecimal runFormula(Map<String, Object> env, String formula) {
        // 注册 BigDecimal 支持（Aviator 默认用 Double，需转换）
        // 执行公式
        Object result = AviatorEvaluator.execute(formula, env);
        // 转为 BigDecimal（Aviator 返回 Number）
        if (result instanceof Number) {
            return new BigDecimal(result.toString());
        }
        throw new IllegalArgumentException("公式计算结果非数字");
    }

    @Override
    public Map<String, Object> runAll(String templateId, Map<String, Object> env) {
        List<SalaryTemplateItem> items = salaryTemplateItemServiceImpl.getSalaryTemplateItems(DataQuery.getInstance()
                .setPageSize(100)
                .addRule(new DBRule("TEMPLATEID", templateId, "=")));
        Map<String, Object> vals = new HashMap<>();
        for (SalaryTemplateItem item : items) {
            vals.put(item.getKeycode(), item.getDefaultval());
        }
        for (String key : env.keySet()) {
            vals.put(key, env.get(key));
        }
        List<SalaryTemplateFormula> formulas = getSalaryTemplateFormulas(DataQuery.getInstance()
                .addSort(new DBSort("STEPCODE", DBSort.SORT_TYPE.ASC))
                .addRule(new DBRule("TEMPLATEID", templateId, "=")));
        for (SalaryTemplateFormula formula : formulas) {
            try {
                BigDecimal result = runFormula(vals, formula.getRuleval());
                vals.put(formula.getValcode(), result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return vals;
    }
}
