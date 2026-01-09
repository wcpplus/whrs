package org.farm2.main.cecurity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserDto {
    @NotNull(message = "{farm2.validate.template.notnull}")
    @Size(min = 2, max = 32, message = "{farm2.validate.template.size}")
    @Schema(description = "登录名", example = "zhangsan")
    private String loginname;
    @NotNull(message = "{farm2.validate.template.notnull}")
    @Size(min = 2, max = 32, message = "{farm2.validate.template.size}")
    @Schema(description = "登录密码", example = "123456")
    private String password;

    /**
     * 保存验证码和盐兑换CODE
     */
    private String code;
    /**
     * 是否保持登录状态
     */
    private Boolean saveLogin;

    /**
     * 图形验证码
     */
    private String imgcode;
}
