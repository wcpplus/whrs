package org.farm2.wdap.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.wdap.convertor.filemode.inter.FileModel;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.wdap.dto.WdapExtendFileDto;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdAndIdsDto;
import org.farm2.tools.db.ResultDataHandle;
import org.farm2.wdap.utils.FileModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 扩展附件
 *
 * @author cbtg自动生成  2025-1-25 18:24:40
 */
@RestController
@RequestMapping("/api/wdapextendfile")
public class WdapExtendFileController {
    @Autowired
    private WdapExtendFileServiceInter wdapExtendFileServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapExtendFile.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("CTIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (FILEID like ? or b.title like ?)", FarmDbLikeModel.ALL);
        DataResult result = wdapExtendFileServiceImpl.searchWdapExtendFile(dataQuery);
        result.runDictionary(ConvertUtils.getFileModelDic(), "MODELTITLE");
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 添加数据
     *
     * @param wdapExtendFileDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapExtendFile.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody WdapExtendFileDto wdapExtendFileDto) {
        WdapExtendFile wdapExtendFile = wdapExtendFileServiceImpl.insertWdapExtendFileEntity(FarmBeanUtils.copyProperties(wdapExtendFileDto, new WdapExtendFile()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, wdapExtendFile.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param wdapExtendFileDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapExtendFile.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody WdapExtendFileDto wdapExtendFileDto) {
        wdapExtendFileDto.setId(id);
        wdapExtendFileServiceImpl.editWdapExtendFileEntity(FarmBeanUtils.copyProperties(wdapExtendFileDto, new WdapExtendFile()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapExtendFile.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        wdapExtendFileServiceImpl.delWdapExtendFile(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('wdapExtendFile.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            wdapExtendFileServiceImpl.delWdapExtendFile(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapExtendFile.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        WdapExtendFile wdapExtendFile = wdapExtendFileServiceImpl.getWdapExtendFileById(id);
        FileModel filemodel = FileModelUtils.getModel(wdapExtendFile.getFilemodel());
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(wdapExtendFile.getFileid(),false);
        File dir = filemodel.getDir(rfile, resourceFileServiceImpl);
        WdapExtendFileDto dto = FarmBeanUtils.copyProperties(wdapExtendFile, new WdapExtendFileDto());
        dto.setDirpath(dir.getPath());
        return new FarmResponseResult(FarmResponseCode.SUCESS, dto);
    }
}
