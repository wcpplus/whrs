package org.farm2.base.validation.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.farm2.base.validation.RegExValidator;
import org.farm2.base.validation.ValidType;
import org.springframework.scheduling.support.CronExpression;

/**
 * 自定义的验证类，可以接受正则表达式对表单验证  ValidType中封装了正则表达式和错误提示
 */
public class RegExValidatorImpl implements ConstraintValidator<RegExValidator, String> {
    private ValidType type;

    @Override
    public void initialize(RegExValidator constraintAnnotation) {
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {
            if (value == null || value.isEmpty()) {
                return true;
            }
            if (value.matches(type.getRegEx())) {
                return true;
            } else {
                context.buildConstraintViolationWithTemplate(type.getMessage())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }

        } catch (Exception e) {
            context.buildConstraintViolationWithTemplate(type.getMessage())
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            return false;
        }
    }
}
