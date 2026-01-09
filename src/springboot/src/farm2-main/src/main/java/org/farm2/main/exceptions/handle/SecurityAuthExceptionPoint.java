package org.farm2.main.exceptions.handle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 异常处理
 * AuthenticationEntryPoint 用于处理未认证的请求，即当用户尝试访问需要认证的资源但没有提供有效的认证信息时，Spring Security 会调用这个入口点来处理。
 */
@Component
@Slf4j
public class SecurityAuthExceptionPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        log.error(I18n.msg(authException.getMessage()) +":"+
                (FarmUserContextLoader.getCurrentUserKey() == null ? "未登录" : FarmUserContextLoader.getCurrentUserKey())
                + request.toString());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new FarmResponseResult(FarmResponseCode.ERROR, I18n.msg(authException.getMessage())).toJson());
    }

}