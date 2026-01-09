package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户岗位
 *
 * @author cbtg自动生成  2025-5-10 22:11:54
 */
@Data
@NoArgsConstructor
public class UserPostDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
    private String id;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 64, message = "{farm2.validate.template.size}")
    private String userkey;

    private String postid;
}