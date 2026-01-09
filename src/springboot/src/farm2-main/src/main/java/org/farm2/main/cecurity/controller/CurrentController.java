package org.farm2.main.cecurity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.farm2.auth.dto.MenusDto;
import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.service.ActionsServiceInter;
import org.farm2.auth.service.EventLogServiceInter;
import org.farm2.base.db.resource.Farm2SystemResource;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.base.web.Farm2WebVisitListenner;
import org.farm2.files.domain.ex.FarmFilePath;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.luser.dto.ResetPasswordDto;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.main.cecurity.dto.KeyDto;
import org.farm2.main.cecurity.dto.UserInfoDto;
import org.farm2.main.cecurity.service.FarmUserOnlines;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.structure.ResourceInfo;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 当前用户状态
 */
@RestController
@Slf4j
@Tag(name = "当前用户相关", description = "登录相关操作的API")
@RequestMapping("/api/current")
public class CurrentController {
    @Autowired
    private FarmParameterInter farmParameter;
    @Autowired
    private FarmUserServiceInter farmUserService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ActionsServiceInter actionsServiceImpl;
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private EventLogServiceInter eventLogServiceImpl;

    @Operation(summary = "当前用戶信息(用户和菜单)获取", description = "")
    @PostMapping(value = "/user")
    @ResponseBody
    public FarmResponseResult user(HttpServletRequest request) {
        FarmUserContext user = new FarmUserContext();
        FarmBeanUtils.copyProperties(FarmUserContextLoader.getCurrentUser(), user);
        if (user.getLoginname() == null) {
            return new FarmResponseResult<Map>(FarmResponseCode.SUCESS);
        }
        user.setPassword("");
        List<MenusDto> frameMenus = farmUserService.getUserMenus(user, "FRAME");
        List<MenusDto> appMenus = farmUserService.getUserMenus(user, "APP");
        Map<String, Object> map = new HashMap<>();
        user.setFrameMenuNum(frameMenus.size());
        map.put("user", user);
        map.put("frameMenus", frameMenus);
        map.put("appMenus", appMenus);
        return new FarmResponseResult<Map>(FarmResponseCode.SUCESS, map);
    }


    @PostMapping(value = "/eidt")
    @ResponseBody
    public FarmResponseResult eidt(@RequestBody UserInfoDto user) {
        farmUserService.editUser(user.getName(), user.getPhotoid());
        FarmCaches.getInstance().removeCacheData(FarmUserContextLoader.getCurrentUserKey(), FarmCacheKeys.LOGIN_USER);
        return new FarmResponseResult<Map>(FarmResponseCode.SUCESS);
    }


    @Operation(summary = "当前系统资源信息", description = "")
    @PostMapping(value = "/resource")
    @ResponseBody
    public FarmResponseResult resource(HttpServletRequest request) {
        FarmFilePath filePath = resourceFileServiceImpl.getBasePath(null);
        Map<String, Object> map = new HashMap<>();
        //磁盘百分比
        {
            ResourceInfo info = Farm2SystemResource.getDiskPercentage(filePath.getBasepath());
            map.put("disk", info);
        }
        //用户百分比
        {
            //附件数量
            int allnum = resourceFileServiceImpl.getAllNum();
            //临时附件数量
            int freenum = resourceFileServiceImpl.getFreeNum();
            ResourceInfo info = new ResourceInfo("附件量", allnum, freenum);
            map.put("files", info);
        }
        //内存百分比
        {
            ResourceInfo info = Farm2SystemResource.getRamPercentage();
            map.put("ram", info);
        }
        //缓存数量
        {
            ResourceInfo info = FarmCaches.getInstance().getAllSize();
            map.put("cache", info);
        }
        //在线用户
        {
            ResourceInfo info = FarmUserOnlines.getOnlineNum();
            map.put("online", info);
        }
        //事件日志
        {
            ResourceInfo info = eventLogServiceImpl.getAllNum();
            map.put("elog", info);
        }
        //处理器百分比
        {
            ResourceInfo info = Farm2SystemResource.getCpuPercentage();
            map.put("cpu", info);
        }
        //访问量
        {
            ResourceInfo info = Farm2WebVisitListenner.getCurentNum();
            map.put("visit", info);
        }
        //事件池
        {
            ResourceInfo info = Farm2Events.getResourceInfo();
            map.put("event", info);
        }
        return new FarmResponseResult<Map>(FarmResponseCode.SUCESS, map);
    }


    /**
     * 重置当前用户密码（指定密码）
     *
     * @param data
     * @return
     */
    @Operation(summary = "当前用戶密码修改", description = "")
    @PostMapping("/reset.password")
    public FarmResponseResult resetPassword(@RequestBody ResetPasswordDto data) {
        FarmPasswordEncoder passwordEncoder = new FarmPasswordEncoder();
        FarmUserContext currentUser = farmUserService.getUserByLoginName(FarmUserContextLoader.getCurrentUser().getLoginname());
        if (passwordEncoder.matches(data.getWebPassword(), passwordEncoder.getSaltByCode(data.getCode()), currentUser.getPassword())) {
            for (String loginname : data.getUserkeys()) {
                if (loginname.equals(currentUser.getLoginname())) {
                    localUserServiceImpl.resetPasswordByDbPassword(loginname, data.getNewSysPassword());
                } else {
                    throw new RuntimeException(I18n.msg("仅可修改当前用户"));
                }
            }
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
        } else {
            return FarmResponseResult.getInstance(FarmResponseCode.ERROR, FarmPasswordEncoder.MATCHES_FALSEMSG, null);
        }
    }


    @Operation(summary = "读取当前用户配置参数", description = "")
    @PostMapping("/config")
    public FarmResponseResult readConfig(@RequestBody KeyDto key) {
        Set<String> ableKeys = new HashSet<>();
        {
            ableKeys.add("farm2.config.file.length.max");
            ableKeys.add("farm2.config.file.exnames");
            ableKeys.add("farm2.config.img.exnames");
            ableKeys.add("farm2.config.img.length.max");
            ableKeys.add("farm2.config.media.exnames");
            ableKeys.add("farm2.config.media.length.max");
        }
        {//栏目的隐藏展示
            ableKeys.add("farm2.config.column.head.show.task");
            ableKeys.add("farm2.config.column.head.show.course");
            ableKeys.add("farm2.config.column.head.show.wts");
            ableKeys.add("farm2.config.column.head.show.knowtypes");
            ableKeys.add("farm2.config.column.head.show.searchview");
        }
        ableKeys.add("farm2.config.sys.home.url");
        ableKeys.add("farm2.config.sys.title.head");
        ableKeys.add("farm2.config.sys.title.foot");
        ableKeys.add("farm2.conf.server.version");
        ableKeys.add("farm2.config.ai.embedding.able");
        ableKeys.add("farm2.config.permission.login.need");
        ableKeys.add("farm2.compute.ai.able");
        ableKeys.add("farm2.compute.hardkey");
        ableKeys.add("farm2.config.permission.know.type.num.max");
        ableKeys.add("farm2.compute.lckey");
        ableKeys.add("farm2.config.sys.help.url");
        ableKeys.add("farm2.config.ai.max.def.bylib.able");
        ableKeys.add("farm2.config.sys.tip.login.html");
        Map<String, Object> backPara = new HashMap<>();
        for (String keystr : key.getKeys()) {
            if (ableKeys.contains(keystr)) {
                //计算参数加载
                if (keystr.equals("farm2.compute.ai.able")) {
                    backPara.put(keystr, true);
                } else if (keystr.equals("farm2.compute.hardkey")) {
                    backPara.put(keystr, FarmParameter.SYS_KEY.toUpperCase());
                } else if (keystr.equals("farm2.compute.lckey")) {
                    backPara.put(keystr, FarmParameter.LC_KEY.toUpperCase());
                } else {
                    //可配置参数加载
                    Object value = Farm2ConfigUtils.getInstance(Farm2ConfigEnum.FARM2_PROPERTIES).getData(keystr);
                    if (value == null) {
                        value = Farm2ConfigUtils.getInstance(Farm2ConfigEnum.INFO_PROPERTIES).getData(keystr);
                    }
                    if (value == null) {
                        value = farmParameter.getObjectParameter(keystr);
                    }
                    if (value == null) {
                        value = "NO_VALUE";
                    }
                    backPara.put(keystr, value);
                }
            }
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, backPara);
    }

    @Operation(summary = "刷新系统缓存", description = "")
    @PostMapping("/clearcache")
    public FarmResponseResult clearCache() {
        FarmCaches.getInstance().clearAllCache();
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }
}
