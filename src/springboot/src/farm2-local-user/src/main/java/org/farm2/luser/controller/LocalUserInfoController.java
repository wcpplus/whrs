package org.farm2.luser.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.luser.domain.LocalUserInfo;
import org.farm2.luser.dto.LocalUserInfoDto;
import org.farm2.luser.service.LocalUserInfoServiceInter;
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
 * 用户信息
 *
 * @author cbtg自动生成  2026-1-5 21:07:40
 */
@RestController
@RequestMapping("/api/localuserinfo")
public class LocalUserInfoController {
    @Autowired
    private LocalUserInfoServiceInter localUserInfoServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('localUserInfo.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like '%?%')", FarmDbLikeModel.ALL);
        DataResult result = localUserInfoServiceImpl.searchLocalUserInfo(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**[tree：树形结构使用]
     * 条件查询(树形结构)
     *
     * @param query
     * @return
     @PreAuthorize("@farmAction.has('localUserInfo.query')")
     @PostMapping("/query") public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
     DataQuery dataQuery = DataQuery.getInstance(query);
     dataQuery.addDefaultSort(new DBSort("SORTCODE", DBSort.SORT_TYPE.ASC));
     dataQuery.setRuleAsSql("default", "and (NAME like '%?%')");
     if(!dataQuery.hasRules()){
     dataQuery.addRule(new DBRule("PARENTID","NONE","="));
     }
     DataResult result = localUserInfoServiceImpl.searchLocalUserInfo(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", localUserInfoServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('localUserInfo.query')")
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
     node.put("NAME", "用户信息");
     node.put("isLeaf", localUserInfoServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", "NONE", "="))) <= 0);
     list.add(node);
     DataResult result = new DataResult(list, 1);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
     }
     DataResult result = localUserInfoServiceImpl.searchLocalUserInfo(dataQuery);
     result.runDataHandle(new ResultDataHandle() {
     @Override public void handle(Map<String, Object> row) {
     row.put("isLeaf", localUserInfoServiceImpl.getNum(DataQuery.getInstance().addRule(new DBRule("PARENTID", row.get("ID"), "="))) <= 0);
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

     @PreAuthorize("@farmAction.has('localUserInfo.move')")
     @PostMapping("/move") public FarmResponseResult move(@RequestBody IdAndIdsDto paras) {
     localUserInfoServiceImpl.moveTreeNode(paras.getIds(), paras.getId());
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }
     */

    /**[tree：树形结构使用]
     * 自动设置排序
     *
     * @param ids
     * @return

     @PreAuthorize("@farmAction.has('localUserInfo.autosort')")
     @PostMapping("/autosort") public FarmResponseResult autosort(@RequestBody List<String> ids) {
     localUserInfoServiceImpl.autoSort(ids);
     return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, null);
     }*/


    /**
     * 添加数据
     *
     * @param localUserInfoDto
     * @return
     */
    @PreAuthorize("@farmAction.has('localUserInfo.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody LocalUserInfoDto localUserInfoDto) {
        LocalUserInfo localUserInfo = localUserInfoServiceImpl.insertLocalUserInfoEntity(FarmBeanUtils.copyProperties(localUserInfoDto, new LocalUserInfo()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, localUserInfo.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param localUserInfoDto
     * @return
     */
    @PreAuthorize("@farmAction.has('localUserInfo.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody LocalUserInfoDto localUserInfoDto) {
        localUserInfoDto.setId(id);
        localUserInfoServiceImpl.editLocalUserInfoEntity(FarmBeanUtils.copyProperties(localUserInfoDto, new LocalUserInfo()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('localUserInfo.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        localUserInfoServiceImpl.delLocalUserInfo(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('localUserInfo.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            localUserInfoServiceImpl.delLocalUserInfo(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('localUserInfo.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        LocalUserInfo localUserInfo = localUserInfoServiceImpl.initUserInfoByUserKey(id);

        LocalUserInfoDto userinfo = FarmBeanUtils.copyProperties(localUserInfo, new LocalUserInfoDto());
        if (StringUtils.isNotBlank(userinfo.getContractfid())) {
            userinfo.setContractfFile(resourceFileServiceImpl.getResourceFileById(userinfo.getContractfid(), false));
        }
        if (StringUtils.isNotBlank(userinfo.getEqfid())) {
            userinfo.setEqfFile(resourceFileServiceImpl.getResourceFileById(userinfo.getEqfid(), false));
        }
        if (StringUtils.isNotBlank(userinfo.getDegfid())) {
            userinfo.setDegfFile(resourceFileServiceImpl.getResourceFileById(userinfo.getDegfid(), false));
        }
        if (StringUtils.isNotBlank(userinfo.getMerfid())) {
            userinfo.setMerfFile(resourceFileServiceImpl.getResourceFileById(userinfo.getMerfid(), false));
        }
        if (StringUtils.isNotBlank(userinfo.getIcardfid())) {
            userinfo.setIcardfFile(resourceFileServiceImpl.getResourceFileById(userinfo.getIcardfid(), false));
        }
        return new FarmResponseResult(FarmResponseCode.SUCESS, userinfo);
    }
}
