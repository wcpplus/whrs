package org.farm2.salary.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.salary.domain.SalaryUserUnit;
import org.farm2.salary.dto.SalaryUserUnitDto;
import org.farm2.salary.service.SalaryUserUnitServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**用户薪资 
 * @author cbtg自动生成  2026-1-8 14:47:36 
 */
@RestController
@RequestMapping("/api/salaryuserunit")
public class SalaryUserUnitController {
    @Autowired
    private SalaryUserUnitServiceInter salaryUserUnitServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUserUnit.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = salaryUserUnitServiceImpl.searchSalaryUserUnit(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }
    
    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return 
    @PreAuthorize("@farmAction.has('salaryUserUnit.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
        if(!dataQuery.hasRules()){
            dataQuery.addRule(new DBRule("PARENTID","NONE","="));
        }
        DataResult result = salaryUserUnitServiceImpl.searchSalaryUserUnit(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("isLeaf", salaryUserUnitServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }    
    */
    
    
    /**[tree：树形结构使用]
     * 查询树
     *
     * @param query
     * @return
     
    @PreAuthorize("@farmAction.has('salaryUserUnit.query')")
    @PostMapping("/tree")
    public FarmResponseResult treeload(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
        dataQuery.setPageSizeAll();
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
        Object parentid = dataQuery.getRuleValue("PARENTID");
        if (parentid == null || StringUtils.isBlank(parentid.toString())) {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> node = new HashMap<>();
            node.put("ID", "NONE");
            node.put("value", "NONE");
            node.put("ICON", Icons.HomeFilled.getSvg());
            node.put("NAME", "用户薪资");
            node.put("isLeaf", salaryUserUnitServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
            list.add(node);
            DataResult result = new DataResult(list, 1);
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
        }
         DataResult result = salaryUserUnitServiceImpl.searchSalaryUserUnit(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("isLeaf", salaryUserUnitServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
                row.put("value", row.get("ID"));
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }
    */
    
    
        /**[tree：树形结构使用]
     * 移动节点结构
     *
     * @param paras
     * @return
  
    @PreAuthorize("@farmAction.has('salaryUserUnit.move')")
    @PostMapping("/move")
    public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
        salaryUserUnitServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }
       */

      /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return
     
    @PreAuthorize("@farmAction.has('salaryUserUnit.autosort')")
    @PostMapping("/autosort")
    public FarmResponseResult autosort(@RequestBody List<String> ids) {
        salaryUserUnitServiceImpl.autoSort(ids);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }*/



    /**
     * 添加数据
     *
     * @param salaryUserUnitDto
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUserUnit.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody SalaryUserUnitDto salaryUserUnitDto) {
        SalaryUserUnit salaryUserUnit = salaryUserUnitServiceImpl.insertSalaryUserUnitEntity(FarmBeanUtils.copyProperties(salaryUserUnitDto, new SalaryUserUnit()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, salaryUserUnit.getId());
    }

    /**修改数据
     * @param id
     * @param salaryUserUnitDto
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUserUnit.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody SalaryUserUnitDto salaryUserUnitDto) {
        salaryUserUnitDto.setId(id);
        salaryUserUnitServiceImpl.editSalaryUserUnitEntity(FarmBeanUtils.copyProperties(salaryUserUnitDto, new SalaryUserUnit()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**删除数据
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUserUnit.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        salaryUserUnitServiceImpl.delSalaryUserUnit(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('salaryUserUnit.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            salaryUserUnitServiceImpl.delSalaryUserUnit(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**数据浏览
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('salaryUserUnit.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        SalaryUserUnit salaryUserUnit = salaryUserUnitServiceImpl.getSalaryUserUnitById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(salaryUserUnit, new SalaryUserUnitDto()));
    }
}
