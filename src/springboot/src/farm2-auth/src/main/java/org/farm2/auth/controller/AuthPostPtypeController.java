package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.AuthPtype;
import org.farm2.auth.domain.Post;
import org.farm2.auth.service.AuthPtypeServiceInter;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.auth.service.UserPostServiceInter;
import org.farm2.base.elementplus.Icons;
import org.farm2.auth.domain.AuthPostPtype;
import org.farm2.auth.dto.AuthPostPtypeDto;
import org.farm2.auth.service.AuthPostPtypeServiceInter;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.WhereInRule;
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
import java.util.stream.Collectors;

/**
 * 角色类别关系
 *
 * @author cbtg自动生成  2025-10-29 11:56:43
 */
@RestController
@RequestMapping("/api/authpostptype")
public class AuthPostPtypeController {
    @Autowired
    private AuthPostPtypeServiceInter authPostPtypeServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private UserPostServiceInter userPostServiceImpl;
    @Autowired
    private AuthPtypeServiceInter authPtypeServiceImpl;

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
        if (StringUtils.isNotBlank((String) dataQuery.getRuleValue("POSTIDS"))) {
            String postIds = ((String) dataQuery.getRuleValue("POSTIDS")).replace("-", ",");
            dataQuery.addRule(new WhereInRule("POSTID", FarmStringUtils.splitString(postIds).stream().collect(Collectors.toSet())));
            dataQuery.removeRule("POSTIDS");
        }
        dataQuery.addDefaultSort(new DBSort("POSTID", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = authPostPtypeServiceImpl.searchAuthPostPtype(dataQuery);
        result.runDataHandle((node) -> {
            String postId = (String) node.get("POSTID");
            String ptypeId = (String) node.get("PTYPEID");
            Post post = postServiceImpl.getPostById(postId);
            AuthPtype ptype = authPtypeServiceImpl.getAuthPtypeById(ptypeId);
            node.put("POST", post != null ? post.getName() : "已失效");
            node.put("PTYPE", ptype != null ? ptype.getName() : "已失效");
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return
     @PreAuthorize("@farmAction.has('authPostPtype.query')")
     @PostMapping("/query") public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     if(!dataQuery.hasRules()){
     dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = authPostPtypeServiceImpl.searchAuthPostPtype(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", authPostPtypeServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('authPostPtype.query')")
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
     node.put("NAME", "角色类别关系");
     node.put("isLeaf", authPostPtypeServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
     list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = authPostPtypeServiceImpl.searchAuthPostPtype(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", authPostPtypeServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('authPostPtype.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     authPostPtypeServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }
     */

    /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return

     @PreAuthorize("@farmAction.has('authPostPtype.autosort')")
     @PostMapping("/autosort") public FarmResponseResult autosort(@RequestBody List<String> ids) {
     authPostPtypeServiceImpl.autoSort(ids);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }*/


    /**
     * 添加数据
     *
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody IdAndIdsDto ids) {
        for (String postId : ids.getIds()) {
            authPostPtypeServiceImpl.bind(postId,ids.getId());
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param authPostPtypeDto
     * @return
     */
    @PreAuthorize("@farmAction.has('post.query')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody AuthPostPtypeDto authPostPtypeDto) {
        authPostPtypeDto.setId(id);
        authPostPtypeServiceImpl.editAuthPostPtypeEntity(FarmBeanUtils.copyProperties(authPostPtypeDto, new AuthPostPtype()));
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
        authPostPtypeServiceImpl.delAuthPostPtype(id);
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
            authPostPtypeServiceImpl.delAuthPostPtype(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('authPostPtype.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        AuthPostPtype authPostPtype = authPostPtypeServiceImpl.getAuthPostPtypeById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(authPostPtype, new AuthPostPtypeDto()));
    }
}
