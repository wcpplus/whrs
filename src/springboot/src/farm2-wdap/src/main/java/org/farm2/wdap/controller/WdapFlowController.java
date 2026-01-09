package org.farm2.wdap.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.web.dto.IdAndIdDto;
import org.farm2.tools.web.dto.IdDto;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.wdap.dto.WdapAddNodeDto;
import org.farm2.wdap.dto.WdapFlowDto;
import org.farm2.wdap.dto.flow.EchartsTaskData;
import org.farm2.wdap.service.WdapFlowServiceInter;
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
import org.farm2.wdap.utils.ConvertFileExnameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**转换流程 
 * @author cbtg自动生成  2025-1-21 18:42:38 
 */
@RestController
@RequestMapping("/api/wdapflow")
public class WdapFlowController {
    @Autowired
    private WdapFlowServiceInter wdapFlowServiceImpl;

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (NAME like ? or EXNAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = wdapFlowServiceImpl.searchWdapFlow(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }


    /**
     * 获得流程支持的附件扩展名
     *
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.fileexnames')")
    @PostMapping("/fileexnames")
    public FarmResponseResult fileexnames() {
        List<String> enumNames = Arrays.stream(ConvertFileExnameEnum.values())
                .map(Enum::name)
                .collect(Collectors.toList());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, enumNames);
    }
    
    /**
     * 添加数据
     *
     * @param wdapFlowDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody WdapFlowDto wdapFlowDto) {
        String exnames = String.join(",", wdapFlowDto.getExname());
        WdapFlow flow = FarmBeanUtils.copyProperties(wdapFlowDto, new WdapFlow());
        flow.setExname(exnames);
        WdapFlow wdapFlow = wdapFlowServiceImpl.insertWdapFlowEntity(flow);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, wdapFlow.getId());
    }

    /**修改数据
     * @param id
     * @param wdapFlowDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody WdapFlowDto wdapFlowDto) {
        wdapFlowDto.setId(id);
        WdapFlow flow = FarmBeanUtils.copyProperties(wdapFlowDto, new WdapFlow());
        String exnames = String.join(",", wdapFlowDto.getExname());
        flow.setExname(exnames);
        wdapFlowServiceImpl.editWdapFlowEntity(flow);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**删除数据
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        wdapFlowServiceImpl.delWdapFlow(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('wdapFlow.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            wdapFlowServiceImpl.delWdapFlow(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**数据浏览
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        WdapFlow wdapFlow = wdapFlowServiceImpl.getWdapFlowById(id);
        WdapFlowDto dto = FarmBeanUtils.copyProperties(wdapFlow, new WdapFlowDto());
        dto.setExname(Arrays.stream(wdapFlow.getExname().split(",")).toList());
        return new FarmResponseResult(FarmResponseCode.SUCESS, dto);
    }


    /**
     * 加载流程图数据，节点和连线
     *
     * @param flowId
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.info')")
    @PostMapping("/loadflow")
    public FarmResponseResult loadFlow(@Valid @RequestBody IdDto flowId) {
        EchartsTaskData data = wdapFlowServiceImpl.getDatas(flowId.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, data);
    }


    /**添加流程图节点
     * @param addNode
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.addnode')")
    @PostMapping("/addnode")
    public FarmResponseResult addNode(@Valid @RequestBody WdapAddNodeDto addNode) {
        wdapFlowServiceImpl.addTaskNode(addNode.getFlowid(), addNode.getBaseNodeId(), addNode.getFlowModel());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除选中节点
     *
     * @param id 节点id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.delnode')")
    @PostMapping("/delnode")
    public FarmResponseResult delNode(@Valid @RequestBody IdDto id) {
        wdapFlowServiceImpl.delTaskNode(id.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除所有节点
     *
     * @param id flowID 流程id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.delallnode')")
    @PostMapping("/delallnode")
    public FarmResponseResult delallnode(@Valid @RequestBody IdDto id) {
        wdapFlowServiceImpl.delAllTaskNode(id.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }


    /**
     * 绑定一个转换器到节点上
     *
     * @param ids flowID 转换器id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapFlow.bindconvertor')")
    @PostMapping("/bindconvertor")
    public FarmResponseResult bindConvertor(@Valid @RequestBody IdAndIdDto ids) {
        wdapFlowServiceImpl.bindNode(ids.getId1(), ids.getId2());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }
}
