package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.FarmElTreeUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.auth.domain.Actions;
import org.farm2.auth.dto.ActionsDto;
import org.farm2.auth.service.ActionsServiceInter;
import org.farm2.base.elementplus.domain.TreeNode;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.enums.FarmDbLikeModel;
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

/**
 * 系统权限
 *
 * @author cbtg自动生成  2025-1-6 11:01:26
 */
@RestController
@RequestMapping("/api/actions")
public class ActionsController {
    @Autowired
    private ActionsServiceInter actionsServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
//    @PreAuthorize("@farmAction.has('actions.query')")
//    @PostMapping("/query")
//    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
//        DataQuery dataQuery = DataQuery.getInstance(query);
//        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
//        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
//        DataResult result = actionsServiceImpl.searchActions(dataQuery);
//        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
//    }

    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return */
     @PreAuthorize("@farmAction.has('actions.query')")
     @PostMapping("/query")
     public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
         dataQuery.setRuleAsSql("default", "and (NAME like ?)", FarmDbLikeModel.ALL);
     if(!dataQuery.hasRules()){
     //dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = actionsServiceImpl.searchActions(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", actionsServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
     }
     });
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }


    /**[tree：树形结构使用,异步加载]
     * 查询树
     *
     * @param query
     * @return
     */
     @PreAuthorize("@farmAction.has('actions.query')")
     @PostMapping("/tree")
     public FarmResponseResult treeload(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setPageSizeAll();
     dataQuery.setRuleAsSql("default", "and (NAME like ?)", FarmDbLikeModel.ALL);
     Object parentid = dataQuery.getRuleValue("PARENTID");
     if (parentid == null || StringUtils.isBlank(parentid.toString())) {
     List<Map<String, Object>> list = new ArrayList<>();
         Map<String, Object> farmeNode = new HashMap<>();
         farmeNode.put("ID", "FRAME");
         farmeNode.put("value", "FRAME");
         farmeNode.put("icon", Icons.HomeFilled.getSvg());
         farmeNode.put("NAME", "后台");
         farmeNode.put("isLeaf", actionsServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "FRAME", "="))) <= 0);
         list.add(farmeNode);
         Map<String, Object> node = new HashMap<>();
         node.put("ID", "APP");
         node.put("value", "APP");
         node.put("icon", Icons.HomeFilled.getSvg());
         node.put("NAME", "应用");
         node.put("isLeaf", actionsServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "APP", "="))) <= 0);
         list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = actionsServiceImpl.searchActions(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", actionsServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
     row.put("value", row.get("ID"));
     }
     });
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }


    /**
     * [tree：树形结构使用,异步加载]
     * 查询树
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.alltree')")
    @PostMapping("/alltree")
    public FarmResponseResult alltree(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addRule(new DBRule("A.STATE", "1", "="));
        dataQuery.setPageSizeAll();
        DataResult result = actionsServiceImpl.searchActions(dataQuery);
        Map<String, Object> farmeNode = new HashMap<>();
        farmeNode.put("ID", "FRAME");
        farmeNode.put("PARENTID", "NONE");
        farmeNode.put("icon", Icons.HomeFilled.getSvg());
        farmeNode.put("NAME", "后台");
        farmeNode.put("SORTCODE", 1);
        farmeNode.put("disabled", true);
        result.getData().add(farmeNode);
        Map<String, Object> node = new HashMap<>();
        node.put("ID", "APP");
        node.put("PARENTID", "NONE");
        node.put("icon", Icons.HomeFilled.getSvg());
        node.put("NAME", "应用");
        node.put("SORTCODE", 2);
        node.put("disabled", true);
        result.getData().add(node);
        List<TreeNode> tree = FarmElTreeUtils.getElTrees(result.getData(), "ID", "PARENTID", "NAME", "SORTCODE", "NONE");
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, tree);
    }



    /**[tree：树形结构使用]
     * 移动节点结构
     *
     * @param paras
     * @return
     */
     @PreAuthorize("@farmAction.has('actions.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     actionsServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }


    /**
     * 初始化treecode
     *
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.init')")
    @PostMapping("/init")
    public FarmResponseResult init() {
        actionsServiceImpl.initTreeCode();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }



    /**
     * [tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.autosort')")
    @PostMapping("/autosort")
    public FarmResponseResult autosort(@RequestBody List<String> ids) {
        actionsServiceImpl.autoSort(ids);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }


    /**
     * 添加数据
     *
     * @param actionsDto
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody ActionsDto actionsDto) {
        Actions actions = actionsServiceImpl.insertActionsEntity(FarmBeanUtils.copyProperties(actionsDto, new Actions()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, actions.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param actionsDto
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody ActionsDto actionsDto) {
        actionsDto.setId(id);
        actionsServiceImpl.editActionsEntity(FarmBeanUtils.copyProperties(actionsDto, new Actions()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        actionsServiceImpl.delActions(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('actions.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            actionsServiceImpl.delActions(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('actions.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        Actions actions = actionsServiceImpl.getActionsById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(actions, new ActionsDto()));
    }
}
