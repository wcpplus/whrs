package org.farm2.files.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.files.domain.ResourceFileRegiste;
import org.farm2.files.dto.ResourceFileRegisteDto;
import org.farm2.files.service.ResourceFileRegisteServiceInter;
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

/**附件注册 
 * @author cbtg自动生成  2025-2-4 10:42:08 
 */
@RestController
@RequestMapping("/api/resourcefileregiste")
public class ResourceFileRegisteController {
    @Autowired
    private ResourceFileRegisteServiceInter resourceFileRegisteServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFileRegiste.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("CTIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = resourceFileRegisteServiceImpl.searchResourceFileRegiste(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }
    
    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return 
    @PreAuthorize("@farmAction.has('resourceFileRegiste.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
        if(!dataQuery.hasRules()){
            dataQuery.addRule(new DBRule("PARENTID","NONE","="));
        }
        DataResult result = resourceFileRegisteServiceImpl.searchResourceFileRegiste(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("isLeaf", resourceFileRegisteServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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
     
    @PreAuthorize("@farmAction.has('resourceFileRegiste.query')")
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
            node.put("NAME", "附件注册");
            node.put("isLeaf", resourceFileRegisteServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
            list.add(node);
            DataResult result = new DataResult(list, 1);
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
        }
         DataResult result = resourceFileRegisteServiceImpl.searchResourceFileRegiste(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("isLeaf", resourceFileRegisteServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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
  
    @PreAuthorize("@farmAction.has('resourceFileRegiste.move')")
    @PostMapping("/move")
    public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
        resourceFileRegisteServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }
       */

      /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return
     
    @PreAuthorize("@farmAction.has('resourceFileRegiste.autosort')")
    @PostMapping("/autosort")
    public FarmResponseResult autosort(@RequestBody List<String> ids) {
        resourceFileRegisteServiceImpl.autoSort(ids);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }*/



    /**
     * 添加数据
     *
     * @param resourceFileRegisteDto
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFileRegiste.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody ResourceFileRegisteDto resourceFileRegisteDto) {
        ResourceFileRegiste resourceFileRegiste = resourceFileRegisteServiceImpl.insertResourceFileRegisteEntity(FarmBeanUtils.copyProperties(resourceFileRegisteDto, new ResourceFileRegiste()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, resourceFileRegiste.getId());
    }

    /**修改数据
     * @param id
     * @param resourceFileRegisteDto
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFileRegiste.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody ResourceFileRegisteDto resourceFileRegisteDto) {
        resourceFileRegisteDto.setId(id);
        resourceFileRegisteServiceImpl.editResourceFileRegisteEntity(FarmBeanUtils.copyProperties(resourceFileRegisteDto, new ResourceFileRegiste()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**删除数据
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFileRegiste.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        resourceFileRegisteServiceImpl.delResourceFileRegiste(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('resourceFileRegiste.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            resourceFileRegisteServiceImpl.delResourceFileRegiste(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**数据浏览
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFileRegiste.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        ResourceFileRegiste resourceFileRegiste = resourceFileRegisteServiceImpl.getResourceFileRegisteById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(resourceFileRegiste, new ResourceFileRegisteDto()));
    }
}
