package org.farm2.files.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.dto.IdsUpdateStateDto;
import org.farm2.files.dto.ResourceFileDto;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.Farm2RegisteTypeEnum;
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

/**附件 
 * @author cbtg自动生成  2025-1-13 14:50:11 
 */
@RestController
@RequestMapping("/api/resourcefile")
public class ResourceFileController {
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFile.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("CTIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (TITLE like ? or id = ?)", FarmDbLikeModel.ALL);
        DataResult result = resourceFileServiceImpl.searchResourceFile(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 变更附件状态
     *
     * @param paras
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFile.update.state')")
    @PostMapping("/state")
    public FarmResponseResult updateState(@RequestBody IdsUpdateStateDto paras) {
        for (String id : paras.getIds()) {
            if (paras.getIsSubmit()) {
                resourceFileServiceImpl.submit(id, Farm2RegisteTypeEnum.TEST, "TEST");
            } else {
                resourceFileServiceImpl.cancel(id, Farm2RegisteTypeEnum.TEST, "TEST");
            }
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 添加数据
     *
     * @param resourceFileDto
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFile.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody ResourceFileDto resourceFileDto) {
        ResourceFile resourceFile = resourceFileServiceImpl.insertResourceFileEntity(FarmBeanUtils.copyProperties(resourceFileDto, new ResourceFile()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, resourceFile.getId());
    }

    /**修改数据
     * @param id
     * @param resourceFileDto
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFile.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody ResourceFileDto resourceFileDto) {
        resourceFileDto.setId(id);
        resourceFileServiceImpl.editResourceFileEntity(FarmBeanUtils.copyProperties(resourceFileDto, new ResourceFile()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**删除数据
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFile.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        resourceFileServiceImpl.delResourceFile(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('resourceFile.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            resourceFileServiceImpl.delResourceFile(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**数据浏览
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('resourceFile.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        ResourceFile resourceFile = resourceFileServiceImpl.getResourceFileById(id,false);
        ResourceFileDto dto = FarmBeanUtils.copyProperties(resourceFile, new ResourceFileDto());
        dto.setFullpath(resourceFileServiceImpl.getFilePath(resourceFile).getFullPath().toString());
        return new FarmResponseResult(FarmResponseCode.SUCESS, dto);
    }
}
