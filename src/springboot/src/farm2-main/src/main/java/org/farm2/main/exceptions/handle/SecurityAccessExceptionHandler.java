package org.farm2.main.exceptions.handle;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**异常处理
 * AccessDeniedHandler 用于处理未授权的请求，即当用户已经通过了认证，但没有足够的权限访问某个资源时，Spring Security 会调用这个处理器来处理。
 */
@Component
@Slf4j
public class SecurityAccessExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        log.error(I18n.msg(accessDeniedException.getMessage()), accessDeniedException);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(new FarmResponseResult(FarmResponseCode.ERROR, I18n.msg(accessDeniedException.getMessage())).toJson());
    }

}