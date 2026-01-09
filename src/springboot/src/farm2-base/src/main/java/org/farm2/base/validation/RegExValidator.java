package org.farm2.base.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.farm2.base.validation.impl.RegExValidatorImpl;

import java.lang.annotation.*;

/**
 * 表单验证的注解，对应正则表达式校验，实现类是RegExValidatorImpl
 */
@Constraint(validatedBy = {RegExValidatorImpl.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RegExValidator {

    String message() default "未设置错题提示";

    ValidType type();

    //分组校验
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}