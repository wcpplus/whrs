package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.farm2.auth.dto.KeyValueDto;
import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.domain.Parameter;
import org.farm2.auth.dto.ParameterDto;
import org.farm2.auth.service.ParameterServiceInter;
import org.farm2.base.parameter.ParameterGroupDomain;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.ResultDataHandle;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统参数
 *
 * @author cbtg自动生成  2025-1-8 10:39:16
 */
@RestController
@RequestMapping("/api/parameter")
public class ParameterController {
    @Autowired
    private ParameterServiceInter parameterServiceImpl;
    @Autowired
    private FarmParameter farmParameter;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('parameter.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("PKEY", DBSort.SORT_TYPE.ASC));
        dataQuery.setRuleAsSql("default", "and (NAME like ? or PKEY like ?)", FarmDbLikeModel.ALL);
        DataResult result = parameterServiceImpl.searchParameter(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                String value = farmParameter.getStringParameter((String) row.get("PKEY"));
                row.put("PVALUE", value);
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    @PreAuthorize("@farmAction.has('parameter.querygroups')")
    @PostMapping("/groups")
    public FarmResponseResult queryGroups() {
        List<ParameterGroupDomain> groups = farmParameter.getGroups();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, groups);
    }


    /**
     * 设置参数值
     *
     * @param keyAndValue
     * @return
     */
    @PreAuthorize("@farmAction.has('parameter.val')")
    @PostMapping("/val")
    public FarmResponseResult valset(@RequestBody KeyValueDto keyAndValue) {
        parameterServiceImpl.editParameterVal(keyAndValue.getKey(), keyAndValue.getVal(),keyAndValue.getNote());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }


    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return
     @PreAuthorize("@farmAction.has('parameter.query')")
     @PostMapping("/query") public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     if(!dataQuery.hasRules()){
     dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = parameterServiceImpl.searchParameter(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", parameterServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('parameter.query')")
     @PostMapping("/tree") public FarmResponseResult treeload(@RequestBody DataQueryDto query) {
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
     node.put("NAME", "系统参数");
     node.put("isLeaf", parameterServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
     list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = parameterServiceImpl.searchParameter(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", parameterServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('parameter.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     parameterServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }
     */

    /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return

     @PreAuthorize("@farmAction.has('parameter.autosort')")
     @PostMapping("/autosort") public FarmResponseResult autosort(@RequestBody List<String> ids) {
     parameterServiceImpl.autoSort(ids);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }*/


    /**
     * 添加数据
     *
     * @param parameterDto
     * @return
     */
    @PreAuthorize("@farmAction.has('parameter.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody ParameterDto parameterDto) {
        Parameter parameter = parameterServiceImpl.insertParameterEntity(FarmBeanUtils.copyProperties(parameterDto, new Parameter()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, parameter.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param parameterDto
     * @return
     */
    @PreAuthorize("@farmAction.has('parameter.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody ParameterDto parameterDto) {
        parameterDto.setId(id);
        parameterServiceImpl.editParameterEntity(FarmBeanUtils.copyProperties(parameterDto, new Parameter()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }


    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('parameter.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        parameterServiceImpl.delParameter(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('parameter.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            parameterServiceImpl.delParameter(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('parameter.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        Parameter parameter = parameterServiceImpl.getParameterById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(parameter, new ParameterDto()));
    }
}
