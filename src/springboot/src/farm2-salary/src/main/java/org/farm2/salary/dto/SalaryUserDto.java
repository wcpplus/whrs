package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**用户薪酬WHRS_SALARY_USER 
 * @author cbtg自动生成  2026-1-8 11:14:26 
 */
@Data
@NoArgsConstructor
public class SalaryUserDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String userkey;
   
    private String username;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String note;
   
    private String state;
   
    private String templateid;
}