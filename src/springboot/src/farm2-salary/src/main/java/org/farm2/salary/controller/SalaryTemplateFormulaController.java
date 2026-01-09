package org.farm2.salary.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.dto.KeyValueDto;
import org.farm2.base.elementplus.Icons;
import org.farm2.salary.dao.SalaryTemplateItemDao;
import org.farm2.salary.domain.SalaryTemplateFormula;
import org.farm2.salary.domain.SalaryTemplateItem;
import org.farm2.salary.dto.SalaryTemplateFormulaDto;
import org.farm2.salary.service.SalaryTemplateFormulaServiceInter;
import org.farm2.salary.service.SalaryTemplateItemServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdAndIdsDto;
import org.farm2.tools.db.ResultDataHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算规则
 *
 * @author cbtg自动生成  2026-1-7 13:08:45
 */
@RestController
@RequestMapping("/api/salarytemplateformula")
public class SalaryTemplateFormulaController {
    @Autowired
    private SalaryTemplateFormulaServiceInter salaryTemplateFormulaServiceImpl;
    @Autowired
    private SalaryTemplateItemServiceInter salaryTemplateItemServiceImpl;
    @Autowired
    private SalaryTemplateItemDao salaryTemplateItemDao;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryTemplateFormula.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("STEPCODE", DBSort.SORT_TYPE.ASC));
        dataQuery.setRuleAsSql("default", "and (NAME like ? OR RULEVAL like ? OR VALCODE like ?)", FarmDbLikeModel.ALL);
        Map<String, String> envTitle = new HashMap<>();
        if (dataQuery.getRuleValue("TEMPLATEID") != null) {
            List<SalaryTemplateItem> items = salaryTemplateItemDao.find(DBRuleList.getInstance()
                    .add(new DBRule("TEMPLATEID", dataQuery.getRuleValue("TEMPLATEID"), "="))
                    .toList());
            for (SalaryTemplateItem item : items) {
                envTitle.put(item.getKeycode(), item.getName());
            }
        }
        DataResult result = salaryTemplateFormulaServiceImpl.searchSalaryTemplateFormula(dataQuery);
        result.runDataHandle(row -> {
            String valcode = (String) row.get("VALCODE");
            row.put("VALCODE_ALT", envTitle.getOrDefault(valcode, valcode));
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    @PostMapping("/run")
    public FarmResponseResult run(@RequestBody KeyValueDto formula) {
        String formulaStr = formula.getVal();
        String templateId = formula.getKey();
        List<SalaryTemplateItem> items = salaryTemplateItemServiceImpl.getSalaryTemplateItems(DataQuery.getInstance()
                .setPageSize(100)
                .addRule(new DBRule("TEMPLATEID", templateId, "=")));
        Map<String, Object> vals = new HashMap<>();
        for (SalaryTemplateItem item : items) {
            vals.put(item.getKeycode(), item.getDefaultval());
        }
        BigDecimal result = salaryTemplateFormulaServiceImpl.runFormula(vals, formulaStr);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    @PostMapping("/runAll")
    public FarmResponseResult runAll(@RequestBody KeyValueDto formula) {
        String templateId = formula.getKey();
        Map<String, Object> vals = salaryTemplateFormulaServiceImpl.runAll(templateId, new HashMap<>());
        if (vals.get("GROSS_SALARY_SYS") == null) {
            throw new RuntimeException("未生成【应得收入:GROSS_SALARY_SYS】");
        }
        if (vals.get("NET_SALARY_SYS") == null) {
            throw new RuntimeException("未生成【实际所得:NET_SALARY_SYS】");
        }
        if (vals.get("INCOME_TAX_SYS") == null) {
            throw new RuntimeException("未生成【个人所得税:INCOME_TAX_SYS】");
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, "", vals.toString());
    }


    /**
     * 添加数据
     *
     * @param salaryTemplateFormulaDto
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryTemplateFormula.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody SalaryTemplateFormulaDto salaryTemplateFormulaDto) {
        SalaryTemplateFormula salaryTemplateFormula = salaryTemplateFormulaServiceImpl.insertSalaryTemplateFormulaEntity(FarmBeanUtils.copyProperties(salaryTemplateFormulaDto, new SalaryTemplateFormula()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, salaryTemplateFormula.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param salaryTemplateFormulaDto
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryTemplateFormula.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody SalaryTemplateFormulaDto salaryTemplateFormulaDto) {
        salaryTemplateFormulaDto.setId(id);
        salaryTemplateFormulaServiceImpl.editSalaryTemplateFormulaEntity(FarmBeanUtils.copyProperties(salaryTemplateFormulaDto, new SalaryTemplateFormula()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryTemplateFormula.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        salaryTemplateFormulaServiceImpl.delSalaryTemplateFormula(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('salaryTemplateFormula.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            salaryTemplateFormulaServiceImpl.delSalaryTemplateFormula(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryTemplateFormula.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        SalaryTemplateFormula salaryTemplateFormula = salaryTemplateFormulaServiceImpl.getSalaryTemplateFormulaById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(salaryTemplateFormula, new SalaryTemplateFormulaDto()));
    }
}
