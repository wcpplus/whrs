package org.farm2.luser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class ResetPasswordDto {
    /**
     * 当前登录名
     */
    private String loginname;
    /**
     * 当前密码
     */
    private String webPassword;

    /**
     * 保存验证码和盐兑换CODE
     */
    private String code;

    //------------------------------------------------------
    /**
     * 修改新密码
     */
    private String newSysPassword;
    /**
     * 修改用户
     */
    private List<String> userkeys;

}
