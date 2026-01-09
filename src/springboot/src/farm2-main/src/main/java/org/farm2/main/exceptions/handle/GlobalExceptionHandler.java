package org.farm2.main.exceptions.handle;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常处理
 * 处理系统非安全异常（security中的异常此处不做处理）
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public FarmResponseResult<Object> handleException(Exception ex, WebRequest request) {
        String msg = I18n.msg(ex.getMessage());
        log.error(msg, ex);
        return new FarmResponseResult<Object>(FarmResponseCode.ERROR, msg);
    }


    @ExceptionHandler(IOException.class)
    public FarmResponseResult<Object> handleIOException(Exception ex, WebRequest request) {
        String msg = I18n.msg(ex.getMessage());
        log.error(msg, ex);
        return new FarmResponseResult<Object>(FarmResponseCode.ERROR, msg);
    }


    @ExceptionHandler(RuntimeException.class)
    public FarmResponseResult<Object> handleRuntimeException(Exception ex, WebRequest request) {
        String msg = I18n.msg(ex.getMessage()) + ":" + (FarmUserContextLoader.getCurrentUserKey() == null ? "未登录" : FarmUserContextLoader.getCurrentUserKey());
        if (msg != null && msg.contains("请求没有足够的权限")) {
            log.error(msg, ex.toString());
        } else if (msg != null && msg.contains("com.mysql.cj")) {
            msg = "当前数据库操作异常，请通过日志查看具体信息";
            log.error(msg, ex);
        } else if (msg != null && msg.contains("SQLSyntaxErrorException")) {
            msg = "当前数据库操作异常，请通过日志查看具体信息";
            log.error(msg, ex);
        } else if (msg != null && msg.contains("违反SQL注入风险约束")) {
            msg = "检索中勿使用特殊字符";
            log.error(msg, ex.toString());
        } else {
            log.error(msg, ex);
        }
        return new FarmResponseResult<Object>(FarmResponseCode.ERROR, msg);
    }

    /**
     * 处理表单验证事件
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public FarmResponseResult<Object> handleValidException(MethodArgumentNotValidException ex) {
        try {
            List<String> errors = new ArrayList<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(fieldName + I18n.msgByExpression(errorMessage));
            });
            String msg = StringUtils.join(errors, ",");
            log.error(I18n.msg(msg), ex);
            return new FarmResponseResult<Object>(FarmResponseCode.ERROR, I18n.msg(msg));
        } catch (Exception ex2) {
            log.error(I18n.msg(ex2.getMessage()), ex2);
            return new FarmResponseResult<Object>(FarmResponseCode.ERROR, I18n.msg("未知异常"));
        }
    }
}
