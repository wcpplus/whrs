package org.farm2.wdap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farm2.wdap.convertor.utils.ConvertorParam;

import java.util.List;

/**
 * 文件转换器
 *
 * @author cbtg自动生成  2025-1-24 10:22:07
 */
@Data
@NoArgsConstructor
public class WdapConvertorDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法

    private String id;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String classkey;

    private String sfilemodel;

    private String tfilemodel;
    private String title;

    private String state;

    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;

    private List<ConvertorParam> params;
}