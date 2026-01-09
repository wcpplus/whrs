package org.farm2.wdap.controller;

import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.elementplus.Icons;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.web.dto.IdDto;
import org.farm2.wdap.convertor.utils.WdapFlowRunner;
import org.farm2.wdap.domain.WdapFlow;
import org.farm2.wdap.domain.WdapFlowLink;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.dto.WdapTaskDto;
import org.farm2.wdap.service.WdapFlowServiceInter;
import org.farm2.wdap.service.WdapTaskServiceInter;
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
import org.farm2.wdap.utils.WdapJsonLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 转换任务
 *
 * @author cbtg自动生成  2025-1-25 9:21:04
 */
@RestController
@RequestMapping("/api/wdaptask")
public class WdapTaskController {
    @Autowired
    private WdapTaskServiceInter wdapTaskServiceImpl;
    @Autowired
    private WdapFlowServiceInter wdapFlowServiceImpl;
    @Autowired
    private WdapFlowRunner wdapFlowRunner;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.query')")
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        dataQuery.addDefaultSort(new DBSort("ETIME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", "and (B.TITLE like ? OR A.FILEID like ?)", FarmDbLikeModel.ALL);
        DataResult result = wdapTaskServiceImpl.searchWdapTask(dataQuery);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 添加数据
     *
     * @param wdapTaskDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody WdapTaskDto wdapTaskDto) {
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 修改数据
     *
     * @param id
     * @param wdapTaskDto
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody WdapTaskDto wdapTaskDto) {
        wdapTaskDto.setId(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        wdapTaskServiceImpl.delWdapTask(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }


    /**
     * 重新生成扩展文件
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.retask')")
    @PostMapping("/retask")
    public FarmResponseResult retask(@RequestBody IdAndIdsDto ids) {
        for (String fileid : ids.getIds()) {
            //删除历史task
            for (WdapTask task : wdapTaskServiceImpl.getWdapTasksByFileId(fileid)) {
                wdapTaskServiceImpl.delWdapTask(task.getId());
            }
            //生成task
            WdapTask task = wdapTaskServiceImpl.addTask(fileid);
            wdapFlowRunner.startRunTasks();
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }


    /**
     * 启动转换任务
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.run')")
    @PostMapping("/run")
    public FarmResponseResult runTask(@RequestBody IdDto id) {
        wdapTaskServiceImpl.initStateToWait(id.getId());
        wdapFlowRunner.startRunTasks();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }

    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('wdapTask.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            wdapTaskServiceImpl.delWdapTask(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('wdapTask.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        WdapTask wdapTask = wdapTaskServiceImpl.getWdapTaskById(id);

        WdapTaskDto dto = new WdapTaskDto();
        dto = FarmBeanUtils.copyProperties(wdapTask, new WdapTaskDto());
        dto.setExPath(resourceFileServiceImpl.getExDirPath(resourceFileServiceImpl.getResourceFileById(wdapTask.getFileid(),false)).getPath());
        return new FarmResponseResult(FarmResponseCode.SUCESS, dto);
    }
}
