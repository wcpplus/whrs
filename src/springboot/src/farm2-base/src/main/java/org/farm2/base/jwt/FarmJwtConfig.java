package org.farm2.base.jwt;

import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;

public class FarmJwtConfig {
    //认证 Token（Access Token）过期时间	分钟
    public static String expiresMinute = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.jwt.auth.expires.minute");
    //刷新 Token（Refresh Token）过期时间
    public static String expiresHours = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.jwt.refresh.expires.hours");

    public enum ClaimsKey {
        LoginName("loginname"),
        //存储用户登录id用于注销登录
        LoginId("loginid"),
        //登录ip
        IP("ip"),
        //类型：1验证，2刷新
        TYPE("type");

        private String key;

        ClaimsKey(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public static long getRefreshTtlMillis() {
        return (60 * 1000) * 60 * Integer.valueOf(expiresHours);
    }


    public static long getAuthTtlMillis() {
        return (60 * 1000) * Integer.valueOf(expiresMinute);
    }
}
