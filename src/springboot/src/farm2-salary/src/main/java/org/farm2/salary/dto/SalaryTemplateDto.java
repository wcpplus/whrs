package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**薪酬模板 
 * @author cbtg自动生成  2026-1-7 10:20:27 
 */
@Data
@NoArgsConstructor
public class SalaryTemplateDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String cuser;
   
    private String ctime;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
}