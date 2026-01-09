package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.AuthFamily;
import org.farm2.auth.domain.AuthGrade;
import org.farm2.auth.domain.DicType;
import org.farm2.auth.service.*;
import org.farm2.base.elementplus.Icons;
import org.farm2.auth.domain.Post;
import org.farm2.auth.dto.PostDto;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
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
import org.farm2.tools.web.dto.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 角色
 *
 * @author cbtg自动生成  2025-1-6 15:10:13
 */
@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private UserPostServiceInter userPostServiceImpl;
    @Autowired
    private AuthPtypeServiceInter authPtypeServiceImpl;
    @Autowired
    private AuthPostPtypeServiceInter authPostPtypeServiceImpl;
    @Autowired
    private AuthFamilyServiceInter authFamilyServiceImpl;
    @Autowired
    private DicEntityServiceInter dicEntityServiceImpl;
    @Autowired
    private AuthGradeServiceInter authGradeServiceImpl;

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
        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like ? or KEYID like ?)", FarmDbLikeModel.ALL);

        if (StringUtils.isNotBlank((String) dataQuery.getRuleValue("CATEGORYID"))) {
            String ptypeId = (String) dataQuery.getRuleValue("CATEGORYID");
            dataQuery.removeRule("CATEGORYID");
            Set<String> tagids = authPtypeServiceImpl.getPostIds(ptypeId, 100);
            dataQuery.addRule(new WhereInRule("ID", tagids));
        }
        Map<String, DicType> dics = dicEntityServiceImpl.getTypes("TRACK_TYPES");
        DataResult result = postServiceImpl.searchPost(dataQuery);

        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                String postId = (String) row.get("ID");
                int usernum = postServiceImpl.getPostUserNum(postId);
                row.put("USERNUM", usernum);
                row.put("PTYPENUM", authPtypeServiceImpl.getPtypeNum(postId));
                if (StringUtils.isNotBlank((String) row.get("FAMILYID"))) {
                    AuthFamily family = authFamilyServiceImpl.getAuthFamilyById((String) row.get("FAMILYID"));
                    if (family != null) {
                        row.put("FAMILYID_ALT", family.getName());
                    }
                }
                String trackType = (String) row.get("TRACKTYPE");
                if (dics.containsKey(trackType)) {
                    row.put("TRACKTYPE_ALT", dics.get(trackType).getName());
                }
                String gradeId = (String) row.get("GRADEID");
                if (StringUtils.isNotBlank(gradeId)) {
                    AuthGrade grade = authGradeServiceImpl.getAuthGradeById(gradeId);
                    if (grade != null) {
                        row.put("GRADEID_ALT", grade.getName()+grade.getKeyid());
                    }
                }

            }
        });
        //刷新权限相关缓存
        {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_USER_KNOW_POPS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_POSTS);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    @PostMapping("/posts")
    public FarmResponseResult posts(@RequestBody DataQueryDto query) {
        List<Post> posts = postServiceImpl.getPosts(DataQuery.getInstance().setPageSize(100));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, posts);
    }

    /**
     * 获得可用角色
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('post.ables')")
    @PostMapping("/ables")
    public FarmResponseResult queryAbles(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.setPageSizeAll();
        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.addRule(new DBRule("A.STATE", "1", "="));
        DataResult result = postServiceImpl.searchPost(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result.getData());
    }

    /**
     * 保存岗位权限
     *
     * @param idAndIds id为岗位id，ids为设置的权限ids
     * @return
     */
    @PreAuthorize("@farmAction.has('post.setactions')")
    @PostMapping("/setactions")
    public FarmResponseResult saveAction(@Valid @RequestBody IdAndIdsDto idAndIds) {
        postServiceImpl.savePostActions(idAndIds.getId(), idAndIds.getIds());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 获得岗位权限
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('post.getactions')")
    @PostMapping("/getactions")
    public FarmResponseResult getActions(@RequestBody IdDto id) {
        List<String> actions = postServiceImpl.findActions(id.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, actions);
    }


    /**
     * 添加数据
     *
     * @param postDto
     * @return
     */
    @PreAuthorize("@farmAction.has('post.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody PostDto postDto) {
        Post post = postServiceImpl.insertPostEntity(FarmBeanUtils.copyProperties(postDto, new Post()));
        if (StringUtils.isNotBlank(postDto.getCategoryId())) {
            authPostPtypeServiceImpl.bind(post.getId(), postDto.getCategoryId());
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, post.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param postDto
     * @return
     */
    @PreAuthorize("@farmAction.has('post.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody PostDto postDto) {
        postDto.setId(id);
        postServiceImpl.editPostEntity(FarmBeanUtils.copyProperties(postDto, new Post()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('post.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        postServiceImpl.delPost(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('post.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            postServiceImpl.delPost(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('post.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        Post post = postServiceImpl.getPostById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(post, new PostDto()));
    }
}
