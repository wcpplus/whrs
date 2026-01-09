package org.farm2.auth.controller;

import jakarta.validation.Valid;
import org.farm2.auth.domain.UserPost;
import org.farm2.auth.dto.UserPostDto;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.auth.service.UserPostServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdAndIdsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户岗位
 *
 * @author cbtg自动生成  2025-5-10 22:11:54
 */
@RestController
@RequestMapping("/api/userpost")
public class UserPostController {
    @Autowired
    private UserPostServiceInter userPostServiceImpl;
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
        // dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (USERKEY like ? or B.NAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = userPostServiceImpl.searchUserPost(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 添加数据
     *
     * @param postUserids
     * @return
     */
    @PreAuthorize("@farmAction.has('post.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody IdAndIdsDto postUserids) {
        postServiceImpl.savePostUser(postUserids.getIds(), postUserids.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param userPostDto
     * @return
     */
    @PreAuthorize("@farmAction.has('post.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody UserPostDto userPostDto) {
        userPostDto.setId(id);
        userPostServiceImpl.editUserPostEntity(FarmBeanUtils.copyProperties(userPostDto, new UserPost()));
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
        userPostServiceImpl.delUserPost(id);
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
            userPostServiceImpl.delUserPost(id);
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
        UserPost userPost = userPostServiceImpl.getUserPostById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(userPost, new UserPostDto()));
    }
}
