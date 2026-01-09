package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**用户薪资 
 * @author cbtg自动生成  2026-1-8 14:47:36 
 */
@Data
@NoArgsConstructor
public class SalaryUserUnitDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String salarytime;
   
    private String userkey;
   
    private String username;
   
    private String ctime;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String note;
   
    private String state;
   
    private BigDecimal grosspay;
   
    private BigDecimal netpay;
   
    private BigDecimal taxamount;
}