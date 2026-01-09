package org.farm2.files.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

/**
 * 封装附件状态修改参数
 */
@Data
public class IdsUpdateStateDto {
    @NotBlank(message = "{farm2.validate.template.notnull}")
    private List<String> ids;//附件ids
    @NotBlank(message = "{farm2.validate.template.notnull}")
    private Boolean isSubmit;//true 提交注册，false注销临时
}
