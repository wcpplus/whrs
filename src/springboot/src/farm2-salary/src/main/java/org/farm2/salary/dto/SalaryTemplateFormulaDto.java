package org.farm2.salary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**计算规则 
 * @author cbtg自动生成  2026-1-7 13:08:45 
 */
@Data
@NoArgsConstructor
public class SalaryTemplateFormulaDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
    private String name;
    private String ruleval;
    private String valname;
    private String valcode;
    private String templateid;
    private String showmodel;
    private Integer sortcode;
    private Integer stepcode;
    private String note;
}