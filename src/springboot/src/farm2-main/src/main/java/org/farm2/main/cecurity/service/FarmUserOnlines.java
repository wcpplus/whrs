package org.farm2.main.cecurity.service;

import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.main.cecurity.service.domain.OnlineUser;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.structure.ResourceInfo;

import java.util.Date;

/**
 * 管理用户的在线状态（用于执行登录用户注销，登录，信息缓存）
 */
public class FarmUserOnlines {

    /**
     * 用户登录
     *
     * @param userContext
     */
    public static void login(FarmUserContext userContext) {
        FarmCaches.getInstance().putCacheData(userContext.getLoginname(), userContext, FarmCacheKeys.LOGIN_USER);
        Farm2Events.emit(F2EObjectT.USER, userContext.getLoginname(), F2EActionT.LOGIN, userContext);
    }

    /**
     * 用戶登出
     *
     * @param user
     */
    public static void logout(FarmUserContext user) {
        FarmCaches.getInstance().removeCacheData(user.getLoginname(), FarmCacheKeys.LOGIN_USER);
        Farm2Events.emit(F2EObjectT.USER, user.getLoginname(), F2EActionT.LOGOUT, user);
    }

    /**
     * JwtAuthenticationTokenFilter中使用获取缓存用户信息(记录在线状态)
     *
     * @param loginname
     * @param loginid
     * @param farmUserService
     * @return
     */
    public static FarmUserContext getUser(String loginname, String loginid, String ip, FarmUserServiceInter farmUserService) {
        FarmUserContext userContext = FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.LOGIN_USER);
        if (userContext == null) {
            userContext = farmUserService.getUserByLoginName(loginname);
            userContext.setLoginid(loginid);
            FarmCaches.getInstance().putCacheData(loginname, userContext, FarmCacheKeys.LOGIN_USER);
        }
        OnlineUser ouser = FarmCaches.getInstance().getCacheData(loginname + ip, FarmCacheKeys.ONLINE_USER);
        if (ouser == null) {
            FarmCaches.getInstance().putCacheData(loginname + ip, new OnlineUser(ip, userContext.getName(), loginname, loginid), FarmCacheKeys.ONLINE_USER);
        } else {
            //缓存对象更新影响缓存
            ouser.setEndTime((new Date()).getTime());
            // FarmCaches.getInstance().putCacheData(loginid , ouser, FarmCacheKeys.ONLINE_USER);
        }
        return userContext;
    }

    /**
     * 获得当前在线用户
     *
     * @return
     */
    public static ResourceInfo getOnlineNum() {
        int onlineNum = FarmCaches.getInstance().getCacheSize(FarmCacheKeys.ONLINE_USER, true);
        return new ResourceInfo("在线用户", onlineNum);
    }
}
