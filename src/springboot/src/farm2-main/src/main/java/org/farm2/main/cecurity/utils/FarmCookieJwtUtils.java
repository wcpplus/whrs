package org.farm2.main.cecurity.utils;

import jakarta.servlet.http.HttpServletResponse;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;

public class FarmCookieJwtUtils {
    public static String cookieSecure = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.auth.cookie.secure");
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";

    public static void addTokenToCookie(HttpServletResponse response, String token, int maxAgeSeconds) {
        StringBuilder sb = new StringBuilder();
        sb.append(ACCESS_TOKEN_COOKIE_NAME).append("=").append(token);
        sb.append("; Path=/");
        sb.append("; HttpOnly");
        if (cookieSecure.equals("true")) {
            sb.append("; Secure");
        }
        sb.append("; SameSite=Lax");
        if (maxAgeSeconds > 0) {
            sb.append("; Max-Age=").append(maxAgeSeconds);
        }
        response.addHeader("Set-Cookie", sb.toString());
    }

    public static void clearCookie(HttpServletResponse response, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=; Path=/; Max-Age=0; HttpOnly");
        if (cookieSecure.equals("true")) {
            sb.append("; Secure");
        }
        sb.append("; SameSite=Lax"); // 建议保持一致
        response.addHeader("Set-Cookie", sb.toString());
    }

    public static void addRefreshTokenToCookie(HttpServletResponse response, String token, int maxAgeSeconds) {
        StringBuilder sb = new StringBuilder();
        sb.append(REFRESH_TOKEN_COOKIE_NAME).append("=").append(token);
        sb.append("; Path=/");
        sb.append("; HttpOnly");
        if ("true".equals(cookieSecure)) {
            sb.append("; Secure");
        }
        sb.append("; SameSite=Lax");
        if (maxAgeSeconds > 0) {
            sb.append("; Max-Age=").append(maxAgeSeconds);
        }
        response.addHeader("Set-Cookie", sb.toString());
    }
}
