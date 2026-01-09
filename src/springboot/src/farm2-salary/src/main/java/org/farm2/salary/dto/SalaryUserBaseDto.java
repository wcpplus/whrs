package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 用户薪酬定义
 *
 * @author cbtg自动生成  2026-1-8 14:45:42
 */
@Data
@NoArgsConstructor
public class SalaryUserBaseDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法

    private String id;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;

    private String keycode;

    private BigDecimal val;

    private String showmodel;
    private String userover;
    private String userkey;

    private String username;

    private String salarytime;
}