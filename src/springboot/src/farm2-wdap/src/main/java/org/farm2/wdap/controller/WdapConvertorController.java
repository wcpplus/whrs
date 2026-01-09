package org.farm2.wdap.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.json.FarmJsons;
import org.farm2.tools.web.dto.IdDto;
import org.farm2.wdap.convertor.inter.FileConvertorInter;
import org.farm2.wdap.convertor.utils.ConvertUtils;
import org.farm2.wdap.convertor.utils.ConvertorParam;
import org.farm2.wdap.domain.WdapConvertor;
import org.farm2.wdap.dto.ConvertorImplDto;
import org.farm2.wdap.dto.WdapConvertorDto;
import org.farm2.wdap.service.WdapConvertorServiceInter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件转换器
 *
 * @author cbtg自动生成  2025-1-24 10:22:07
 */
@RestController
@RequestMapping("/api/wdapconvertor")
public class WdapConvertorController {
    @Autowired
    private WdapConvertorServiceInter wdapConvertorServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapConvertor.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("SFILEMODEL", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (TITLE like ?)", FarmDbLikeModel.ALL);
        DataResult result = wdapConvertorServiceImpl.searchWdapConvertor(dataQuery);
        result.runDictionary(ConvertUtils.getFileModelDic(), "SFILEMODEL");
        result.runDictionary(ConvertUtils.getFileModelDic(), "TFILEMODEL");
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    @PreAuthorize("@farmAction.has('wdapConvertor.impls')")
    @PostMapping("/impls")
    public FarmResponseResult loadConvertor() {
        List<ConvertorImplDto> impls = ConvertUtils.getImpls();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, impls);
    }

    @PreAuthorize("@farmAction.has('wdapConvertor.params')")
    @PostMapping("/params")
    public FarmResponseResult loadParams(@RequestBody IdDto id) {
        WdapConvertor convertor = wdapConvertorServiceImpl.getWdapConvertorById(id.getId());
        FileConvertorInter convertorImpl = ConvertUtils.getConvertorImpl(convertor.getClasskey());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, convertorImpl.getParams());
    }

    /**
     * 添加数据
     *
     * @param wdapConvertorDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapConvertor.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody WdapConvertorDto wdapConvertorDto) {
        wdapConvertorDto.setParams(null);
        WdapConvertor wdapConvertor = wdapConvertorServiceImpl.insertWdapConvertorEntity(FarmBeanUtils.copyProperties(wdapConvertorDto, new WdapConvertor()));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, wdapConvertor.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param wdapConvertorDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapConvertor.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody WdapConvertorDto wdapConvertorDto) {
        wdapConvertorDto.setId(id);
        FileConvertorInter convertorImpl = ConvertUtils.getConvertorImpl(wdapConvertorDto.getClasskey());
        convertorImpl.valideParams(wdapConvertorDto.getParams());
        WdapConvertor convertor = FarmBeanUtils.copyProperties(wdapConvertorDto, new WdapConvertor());
        convertor.setParams(FarmJsons.toJson(wdapConvertorDto.getParams()));
        wdapConvertorServiceImpl.editWdapConvertorEntity(convertor);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapConvertor.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        wdapConvertorServiceImpl.delWdapConvertor(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('wdapConvertor.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            wdapConvertorServiceImpl.delWdapConvertor(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapConvertor.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        WdapConvertor wdapConvertor = wdapConvertorServiceImpl.getWdapConvertorById(id);
        FileConvertorInter convertorImpl = ConvertUtils.getConvertorImpl(wdapConvertor.getClasskey());
        WdapConvertorDto dto = FarmBeanUtils.copyProperties(wdapConvertor, new WdapConvertorDto());
        if (StringUtils.isNotBlank(wdapConvertor.getParams())) {
            dto.setParams(FarmJsons.toBean(wdapConvertor.getParams(), (new ArrayList<>()).getClass()));
        } else {
            dto.setParams(convertorImpl.getParams());
        }
        return new FarmResponseResult(FarmResponseCode.SUCESS, dto);
    }


}
