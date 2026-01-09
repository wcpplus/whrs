package org.farm2.base.web;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.structure.ResourceInfo;
import org.farm2.tools.time.FarmTimeTool;

import java.util.Date;
import java.util.List;

public class Farm2WebVisitListenner {

    /**
     * 获得统计key
     *
     * @param userkey
     * @param ip
     * @return
     */
    public static String getCountKey(String userkey, String ip) {
        if (ip == null) {
            ip = "";
        }
        return ip + (userkey == null ? "" : (":" + userkey));
    }

    /**
     * 记录一个访问
     *
     * @param servletRequest
     */
    public static void visit(ServletRequest servletRequest) {
        FarmUserContext user = FarmUserContextLoader.getCurrentUser();
        String ip = IpUtils.getClientIp((HttpServletRequest) servletRequest);
        String key = getCountKey(null, ip);
        if (user != null) {
            key = getCountKey(user.getLoginname(), ip);
        }
        FarmCaches.getInstance().putCacheData(((Long) ((new Date()).getTime())).toString(), key, FarmCacheKeys.CLIENT_VISIT_COUNTER);
    }

    /**
     * 最近1分钟内的访问次数
     *
     * @return
     */
    public static ResourceInfo getCurentNum() {
        long num = FarmCaches.getInstance().getCacheSize(FarmCacheKeys.CLIENT_VISIT_COUNTER, true);
        if (MAX_NUM < num) {
            MAX_NUM = num;
        }
        if (!MAX_NUM_REFRESH_DAY.equals(FarmTimeTool.getCurrentDay())) {
            MAX_NUM_REFRESH_DAY = FarmTimeTool.getCurrentDay();
            MAX_NUM = 0;
        }
        //MAX_NUM峰值访问量，每日刷新，num当前访问量
        return new ResourceInfo("访问量/分", MAX_NUM, num);
    }


    public static ResourceInfo getCurentNum(String userkey, String ip) {
        FarmCaches.getInstance().refresh(FarmCacheKeys.CLIENT_VISIT_COUNTER);
        List<String> keys = FarmCaches.getInstance().getAll(FarmCacheKeys.CLIENT_VISIT_COUNTER);
        long num = 0;
        for (String key : keys) {
            if (getCountKey(userkey, ip).equals(key)) {
                num++;
            }
        }
        //MAX_NUM峰值访问量，每日刷新，num当前访问量
        return new ResourceInfo("访问量/分", MAX_NUM, num);
    }

    private static long MAX_NUM;
    private static String MAX_NUM_REFRESH_DAY = FarmTimeTool.getCurrentDay();
}
