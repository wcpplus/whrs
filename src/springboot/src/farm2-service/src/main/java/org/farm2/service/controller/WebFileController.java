package org.farm2.service.controller;

import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.service.dto.FileViewInfoDto;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdDto;
import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.wdap.service.WdapTaskServiceInter;
import org.farm2.wdap.utils.FileModelUtils;
import org.farm2.wdap.utils.WdapTaskStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 知识
 *
 * @author cbtg自动生成  2025-2-4 12:13:51
 */
@RestController
@RequestMapping("/api/wfile")
public class WebFileController {
    @Autowired
    private WdapExtendFileServiceInter wdapExtendFileServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private WdapTaskServiceInter wdapTaskServiceImpl;

    /**
     * 加载知识内容
     *
     * @param id
     * @return
     */
    @PostMapping("/info")
    public FarmResponseResult queryAll(@RequestBody IdDto id) {
        FileViewInfoDto viewInfo = new FileViewInfoDto();
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(id.getId(), true);
        if (rfile == null) {
            return FarmResponseResult.getInstance(FarmResponseCode.ERROR, "附件不存在:" + id.getId());
        }
        viewInfo.setTitle(rfile.getTitle());
        viewInfo.setSize(rfile.getFilesize());
        viewInfo.setState("3");
        viewInfo.setStateTitle("暂未生成预览任务");
        //获得附件任务信息     0等待，1转换中，2完成，3无需转换，9失败
        WdapTask task = wdapTaskServiceImpl.getWdapTaskByFileId(rfile.getId());
        if (task != null) {
            viewInfo.setState(task.getPstate());
            viewInfo.setStateTitle(WdapTaskStateEnum.getDic().get(task.getPstate()));
        }
        //获得附件扩展文件
        List<WdapExtendFile> exfiles = wdapExtendFileServiceImpl.getWdapExtendFileByResourceFileId(rfile.getId());
        WdapExtendFile extendFile = FileModelUtils.getBestView(exfiles);
        if (extendFile != null) {
            viewInfo.setExtendFile(extendFile);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, viewInfo);
    }
}
