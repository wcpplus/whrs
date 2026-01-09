package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**职级 
 * @author cbtg自动生成  2026-1-5 11:41:21 
 */
@Data
@NoArgsConstructor
public class AuthGradeDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String keyid;
   
    private String tracktype;
   
    private Integer sortcode;
   
    private Integer minsalary;
   
    private Integer maxsalary;
}