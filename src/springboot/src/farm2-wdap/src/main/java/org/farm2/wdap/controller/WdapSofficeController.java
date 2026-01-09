package org.farm2.wdap.controller;

import org.farm2.base.process.FarmProcessUtils;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.wdap.convertor.utils.OpenOffceUtils;
import org.farm2.wdap.convertor.utils.OsUtils;
import org.farm2.wdap.dto.SofficeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 流程连线
 *
 * @author cbtg自动生成  2025-1-21 22:42:51
 */
@RestController
@RequestMapping("/api/soffice")
public class WdapSofficeController {
    @Autowired
    private FarmParameterInter farmParameter;

    @PreAuthorize("@farmAction.has('soffice.info')")
    @PostMapping("/info")
    public FarmResponseResult info() {
        FarmProcessUtils.sleep(2000);
        SofficeDto info = new SofficeDto();
        info.setIp(farmParameter.getStringParameter("farm2.config.openoffice.host"));
        info.setPort(farmParameter.getIntParameter("farm2.config..openoffice.port"));
        if (OsUtils.isWindows()) {
            info.setOs("windows");
            info.setStartCmd(farmParameter.getStringParameter("farm2.config.openoffice.start.windows.cmd"));
            info.setStopCmd(farmParameter.getStringParameter("farm2.config.openoffice.kill.windows.cmd"));
        } else {
            info.setOs("linux");
            info.setStartCmd(farmParameter.getStringParameter("farm2.config.openoffice.start.linux.cmd"));
            info.setStopCmd(farmParameter.getStringParameter("farm2.config.openoffice.kill.linux.cmd"));
        }
        info.setLive(OpenOffceUtils.isStart());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, info);
    }

    @PreAuthorize("@farmAction.has('soffice.start')")
    @PostMapping("/start")
    public FarmResponseResult start() {
        OpenOffceUtils.reStart();
        FarmProcessUtils.sleep(2000);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    @PreAuthorize("@farmAction.has('soffice.stop')")
    @PostMapping("/stop")
    public FarmResponseResult stop() {
        OpenOffceUtils.shutdown();
        FarmProcessUtils.sleep(2000);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }
}
