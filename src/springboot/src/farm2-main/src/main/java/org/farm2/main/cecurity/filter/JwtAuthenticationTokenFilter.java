package org.farm2.main.cecurity.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.jwt.FarmJwtClaims;
import org.farm2.base.jwt.FarmJwtConfig;
import org.farm2.base.jwt.FarmJwtUtils;
import org.farm2.base.web.IpUtils;
import org.farm2.main.cecurity.service.FarmUserOnlines;
import org.farm2.main.cecurity.utils.FarmCookieJwtUtils;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

/**
 * 在security检查用户权限前，验证JWT并进行用户授权
 * OncePerRequestFilter保证过滤器只执行一次
 */
@Component
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private FarmUserServiceInter farmUserService;

    /**
     * jwt存储模式：
     */
    public static String jwtModel = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.auth.jwt.model");
    /**
     * 是否在无授权时自动添加sysadmin为当前用户
     */
    @Value("${farm2.test.auto.auth.able}")
    private boolean autoAuth;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = null;
        if (jwtModel.equals("local")) {
            token = request.getHeader("token");
        } else {
            //通过cookie获取jwt
            token = getJwtByCookie(request, response);
        }
        if (autoAuth) {
            //自動添加授權信息验证成功
            if (StringUtils.isBlank(token)) {
                token = "sysadmin";
            }
            setSuccessUser(request, response, filterChain, farmUserService.getUserByLoginName(token));
            return;
        }
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        //校验token
        FarmJwtClaims claims = FarmJwtUtils.parseJWT(token);
        if (claims == null) {
            filterChain.doFilter(request, response);
            return;
        }
        String loginId = (String) claims.get(FarmJwtConfig.ClaimsKey.LoginId);
        String loginname = (String) claims.get(FarmJwtConfig.ClaimsKey.LoginName);
        if (StringUtils.isBlank(loginname)) {
            filterChain.doFilter(request, response);
            return;
        }
        //加载用户信息（从缓存或数据库）
        FarmUserContext userDetail = FarmUserOnlines.getUser(loginname, loginId, IpUtils.getClientIp(request), farmUserService);
        {
            //验证成功
            setSuccessUser(request, response, filterChain, userDetail);
        }
    }

    private void setSuccessUser(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, FarmUserContext userDetail) throws ServletException, IOException {
        userDetail.setIp(IpUtils.getClientIp(request));
        FarmUserContextLoader.put(userDetail);
        filterChain.doFilter(request, response);
    }

    private String getJwtByCookie(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = getCookieValue(request, FarmCookieJwtUtils.ACCESS_TOKEN_COOKIE_NAME);
        String refreshToken = getCookieValue(request, FarmCookieJwtUtils.REFRESH_TOKEN_COOKIE_NAME);

        if (StringUtils.isNotBlank(accessToken)) {
            FarmJwtClaims accessClaims = safeParseJWT(accessToken);
            if (isValidAccessClaims(accessClaims, request)) {
                return accessToken;
            }
        }

        if (StringUtils.isNotBlank(refreshToken)) {
            FarmJwtClaims refreshClaims = safeParseJWT(refreshToken);
            if (isValidRefreshClaims(refreshClaims, request)) {
                Map<String, Object> newClaims = FarmJwtClaims.getInstance()
                        .addUserKey((String) refreshClaims.get(FarmJwtConfig.ClaimsKey.LoginName))
                        .addFeild(FarmJwtConfig.ClaimsKey.LoginId, (String) refreshClaims.get(FarmJwtConfig.ClaimsKey.LoginId))
                        .addFeild(FarmJwtConfig.ClaimsKey.IP, IpUtils.getClientIp(request))
                        .getClaims();

                String newAccessToken = FarmJwtUtils.createAuthJwt(newClaims);
                FarmCookieJwtUtils.addTokenToCookie(response, newAccessToken, 30 * 60);
                return newAccessToken;
            } else {
                // 可选：清除无效 refresh token
                FarmCookieJwtUtils.clearCookie(response, FarmCookieJwtUtils.REFRESH_TOKEN_COOKIE_NAME);
            }
        }
        return null;
    }



    private FarmJwtClaims safeParseJWT(String token) {
        try {
            return FarmJwtUtils.parseJWT(token);
        } catch (Exception e) {
            log.warn("Invalid JWT token: {}", token, e);
            return null;
        }
    }


    private boolean isValidAccessClaims(FarmJwtClaims claims, HttpServletRequest request) {
        if (claims == null) return false;
        String tokenIp = (String) claims.get(FarmJwtConfig.ClaimsKey.IP);
        return IpUtils.getClientIp(request).equals(tokenIp);
    }

    private boolean isValidRefreshClaims(FarmJwtClaims claims, HttpServletRequest request) {
        if (claims == null) return false;
        // 必须是 refresh token 类型
        if (!"2".equals(claims.get(FarmJwtConfig.ClaimsKey.TYPE))) return false;
        String tokenIp = (String) claims.get(FarmJwtConfig.ClaimsKey.IP);
        return IpUtils.getClientIp(request).equals(tokenIp);
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
