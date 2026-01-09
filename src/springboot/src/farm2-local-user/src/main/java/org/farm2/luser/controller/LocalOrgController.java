package org.farm2.luser.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.Post;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.elementplus.Icons;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.dto.LocalOrgDto;
import org.farm2.luser.service.LocalOrgServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.ResultDataHandle;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdAndIdsDto;
import org.farm2.tools.web.dto.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织机构
 *
 * @author cbtg自动生成  2025-1-2 22:01:57
 */
@RestController
@RequestMapping("/api/localorg")
public class LocalOrgController {
    @Autowired
    private LocalOrgServiceInter localOrgServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;

    @PostMapping("/loadOrgPath")
    public FarmResponseResult loadOrgPath(@RequestBody IdDto orgid) {
        if (StringUtils.isBlank(orgid.getId())) {
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, List.of());
        }
        List<LocalOrg> orgs = localOrgServiceImpl.getOrgPath(orgid.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, orgs);
    }

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
        dataQuery.setRuleAsSql("default", "and (NAME like ?)", FarmDbLikeModel.ALL);
        if (!dataQuery.hasRules()) {
            dataQuery.addRule(new DBRule("PARENTID", "NONE", "="));
        }
        DataResult result = localOrgServiceImpl.searchLocalOrg(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("isLeaf", localOrgServiceImpl.getLocalOrgNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
                String id = (String) row.get("ID");
                List<String> orgids = localOrgServiceImpl.getSubOrgids(id);
                List<Post> posts = postServiceImpl.getPosts(DataQuery.getInstance().addRule(new WhereInRule("ORGID", new HashSet<>(orgids))));
                row.put("MAXUSER_ALT", posts.stream().mapToInt(Post::getMaxunum)
                        .sum());
                int usernum = localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                        .addRule(new WhereInRule("STATE", Set.of("1", "2")))
                        .addRule(new WhereInRule("ORGID", new HashSet<>(orgids))));
                row.put("USAERNUM_ALT", usernum);
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**
     * 查询树
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('SYS.login')")
    @PostMapping("/tree")
    public FarmResponseResult treeload(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
        dataQuery.setPageSizeAll();
        Object parentid = dataQuery.getRuleValue("PARENTID");
        if (parentid == null || StringUtils.isBlank(parentid.toString())) {
            List<Map<String, Object>> list = new ArrayList<>();
            Map<String, Object> node = new HashMap<>();
            node.put("ID", "NONE");
            node.put("value", "NONE");
            node.put("icon", Icons.HomeFilled.getSvg());
            node.put("NAME", "组织机构");
            node.put("isLeaf", localOrgServiceImpl.getLocalOrgNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
            list.add(node);
            DataResult result = new DataResult(list, 1);
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
        }
        DataResult result = localOrgServiceImpl.searchLocalOrg(dataQuery);
        result.runDataHandle(new ResultDataHandle() {
            @Override
            public void handle(Map<String, Object> row) {
                row.put("isLeaf", localOrgServiceImpl.getLocalOrgNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
                row.put("value", row.get("ID"));
            }
        });
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 移动节点结构
     *
     * @param paras
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.move')")
    @PostMapping("/move")
    public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
        localOrgServiceImpl.moveLocalOrgs(paras.getIds(), paras.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }

    /**
     * 自动设置排序
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.autosort')")
    @PostMapping("/autosort")
    public FarmResponseResult autosort(@RequestBody List<String> ids) {
        localOrgServiceImpl.autoSort(ids);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
    }


    /**
     * 添加数据
     *
     * @param localOrgDto
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody LocalOrgDto localOrgDto) {
        LocalOrg localOrg = localOrgServiceImpl.insertLocalOrgEntity(FarmBeanUtils.copyProperties(localOrgDto, new LocalOrg()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, localOrg.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param localOrgDto
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody LocalOrgDto localOrgDto) {
        localOrgDto.setId(id);
        localOrgServiceImpl.editLocalOrgEntity(FarmBeanUtils.copyProperties(localOrgDto, new LocalOrg()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        LocalOrg org = localOrgServiceImpl.getLocalOrgById(id);
        localOrgServiceImpl.delLocalOrg(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, org);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('localOrg.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            localOrgServiceImpl.delLocalOrg(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('localOrg.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        LocalOrg localOrg = localOrgServiceImpl.getLocalOrgById(id);
        return new FarmResponseResult(FarmResponseCode.SUCESS, FarmBeanUtils.copyProperties(localOrg, new LocalOrgDto()));
    }
}
