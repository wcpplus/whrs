package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**薪酬项 
 * @author cbtg自动生成  2026-1-7 10:48:10 
 */
@Data
@NoArgsConstructor
public class SalaryTemplateItemDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
   
    private BigDecimal defaultval;
   
    private String keycode;
   
    private String componenttype;
   
    private String sourcemodel;
   
    private String userover;
   
    private Integer sortcode;
   
    private String templateid;
   
    private String showmodel;
}