package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**用户薪酬明细 
 * @author cbtg自动生成  2026-1-8 14:48:48 
 */
@Data
@NoArgsConstructor
public class SalaryUserItemDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
   
    private String keycode;
   
    private String ctime;
   
    private BigDecimal val;
   
    private String showmodel;
   
    private String salarytime;
   
    private String userkey;
   
    private String username;
}