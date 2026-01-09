package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.DicType;
import org.farm2.auth.service.DicEntityServiceInter;
import org.farm2.base.elementplus.Icons;
import org.farm2.auth.domain.AuthGrade;
import org.farm2.auth.dto.AuthGradeDto;
import org.farm2.auth.service.AuthGradeServiceInter;
import org.farm2.tools.base.FarmStringUtils;
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

/**
 * 职级
 *
 * @author cbtg自动生成  2026-1-5 11:41:21
 */
@RestController
@RequestMapping("/api/authgrade")
public class AuthGradeController {
    @Autowired
    private AuthGradeServiceInter authGradeServiceImpl;
    @Autowired
    private DicEntityServiceInter dicEntityServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('authGrade.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("TRACKTYPE", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')", FarmDbLikeModel.ALL);
        DataResult result = authGradeServiceImpl.searchAuthGrade(dataQuery);
        Map<String, DicType> dics = dicEntityServiceImpl.getTypes("TRACK_TYPES");
        result.runDataHandle(node -> {
            String trackType = (String) node.get("TRACKTYPE");
            if (dics.containsKey(trackType)) {
                node.put("TRACKTYPE_ALT", dics.get(trackType).getName());
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PostMapping("/lists")
    public FarmResponseResult lists(@RequestBody DataQueryDto query) {
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, authGradeServiceImpl.getAuthGrades(DataQuery.getInstance()
                .addSort(new DBSort("KEYID", DBSort.SORT_TYPE.ASC))
                .setPageSize(100).setCount(false)));
    }
    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return
     @PreAuthorize("@farmAction.has('authGrade.query')")
     @PostMapping("/query") public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     if(!dataQuery.hasRules()){
     dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = authGradeServiceImpl.searchAuthGrade(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", authGradeServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('authGrade.query')")
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
     node.put("NAME", "职级");
     node.put("isLeaf", authGradeServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
     list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = authGradeServiceImpl.searchAuthGrade(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", authGradeServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('authGrade.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     authGradeServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }
     */

    /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return

     @PreAuthorize("@farmAction.has('authGrade.autosort')")
     @PostMapping("/autosort") public FarmResponseResult autosort(@RequestBody List<String> ids) {
     authGradeServiceImpl.autoSort(ids);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }*/


    /**
     * 添加数据
     *
     * @param authGradeDto
     * @return
     */
    @PreAuthorize("@farmAction.has('authGrade.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody AuthGradeDto authGradeDto) {
        int n = 1;
        for (String name : FarmStringUtils.splitString(authGradeDto.getName())) {
            AuthGrade authGrade = FarmBeanUtils.copyProperties(authGradeDto, new AuthGrade());
            authGrade.setName(name);
            if (FarmStringUtils.splitString(authGradeDto.getName()).size() > 1) {
                authGrade.setSortcode(n);
                authGrade.setKeyid(authGrade.getKeyid() + n);
            }
            n++;
            authGradeServiceImpl.insertAuthGradeEntity(authGrade);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param authGradeDto
     * @return
     */
    @PreAuthorize("@farmAction.has('authGrade.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody AuthGradeDto authGradeDto) {
        authGradeDto.setId(id);
        authGradeServiceImpl.editAuthGradeEntity(FarmBeanUtils.copyProperties(authGradeDto, new AuthGrade()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('authGrade.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        authGradeServiceImpl.delAuthGrade(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('authGrade.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            authGradeServiceImpl.delAuthGrade(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('authGrade.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        AuthGrade authGrade = authGradeServiceImpl.getAuthGradeById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(authGrade, new AuthGradeDto()));
    }
}
