package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统权限
 *
 * @author cbtg自动生成  2025-1-6 11:01:26
 */
@Data
@NoArgsConstructor
public class ActionsDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
    private String id;
    private String ctime;
    private String etime;
    private String euserkey;
    private String cuserkey;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    private String state;
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
    private Integer sortcode;
    private String parentid;
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String menukey;
    private String treecode;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    private String type;
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String actions;
    private String domain;
}