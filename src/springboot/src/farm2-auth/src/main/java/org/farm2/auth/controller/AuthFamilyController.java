package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.elementplus.Icons;
import org.farm2.auth.domain.AuthFamily;
import org.farm2.auth.dto.AuthFamilyDto;
import org.farm2.auth.service.AuthFamilyServiceInter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 岗位族
 *
 * @author cbtg自动生成  2026-1-5 10:12:10
 */
@RestController
@RequestMapping("/api/authfamily")
public class AuthFamilyController {
    @Autowired
    private AuthFamilyServiceInter authFamilyServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("NAME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')", FarmDbLikeModel.ALL);
        DataResult result = authFamilyServiceImpl.searchAuthFamily(dataQuery);
        result.runDataHandle(row -> {
            String id = (String) row.get("ID");
            int num = postServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("FAMILYID", id, "=")));
            row.put("POSTNUM", num);
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    @PreAuthorize("@farmAction.has('post.query')")
    @PostMapping("/lists")
    public FarmResponseResult lists(@RequestBody DataQueryDto query) {
        List<AuthFamily> familis = authFamilyServiceImpl.getAuthFamilys(DataQuery.getInstance().setCount(false));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, familis);
    }

    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return
     @PreAuthorize("@farmAction.has('authFamily.query')")
     @PostMapping("/query") public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     if(!dataQuery.hasRules()){
     dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = authFamilyServiceImpl.searchAuthFamily(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", authFamilyServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('authFamily.query')")
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
     node.put("NAME", "岗位族");
     node.put("isLeaf", authFamilyServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
     list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = authFamilyServiceImpl.searchAuthFamily(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", authFamilyServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('authFamily.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     authFamilyServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }
     */

    /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return

     @PreAuthorize("@farmAction.has('authFamily.autosort')")
     @PostMapping("/autosort") public FarmResponseResult autosort(@RequestBody List<String> ids) {
     authFamilyServiceImpl.autoSort(ids);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }*/


    /**
     * 添加数据
     *
     * @param authFamilyDto
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody AuthFamilyDto authFamilyDto) {
        AuthFamily authFamily = authFamilyServiceImpl.insertAuthFamilyEntity(FarmBeanUtils.copyProperties(authFamilyDto, new AuthFamily()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, authFamily.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param authFamilyDto
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody AuthFamilyDto authFamilyDto) {
        authFamilyDto.setId(id);
        authFamilyServiceImpl.editAuthFamilyEntity(FarmBeanUtils.copyProperties(authFamilyDto, new AuthFamily()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        authFamilyServiceImpl.delAuthFamily(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            authFamilyServiceImpl.delAuthFamily(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        AuthFamily authFamily = authFamilyServiceImpl.getAuthFamilyById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(authFamily, new AuthFamilyDto()));
    }
}
