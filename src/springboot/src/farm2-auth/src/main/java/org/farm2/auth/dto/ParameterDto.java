package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**系统参数 
 * @author cbtg自动生成  2025-1-8 10:39:16 
 */
@Data
@NoArgsConstructor
public class ParameterDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String etime;
   
    private String euserkey;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 64, message = "{farm2.validate.template.size}")
    private String pkey;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 2048, message = "{farm2.validate.template.size}")
    private String pvalue;
   
    @Size(min = 0, max = 2048, message = "{farm2.validate.template.size}")
    private String describes;
   
    @Size(min = 0, max = 64, message = "{farm2.validate.template.size}")
    private String gname;
   
    private String gkey;
   
    private String vtype;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String version;
   
    private String userable;
   
    private String sourcetype;
}