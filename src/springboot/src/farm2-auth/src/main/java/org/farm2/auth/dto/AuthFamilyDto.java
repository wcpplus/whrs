package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**岗位族 
 * @author cbtg自动生成  2026-1-5 10:12:10 
 */
@Data
@NoArgsConstructor
public class AuthFamilyDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String keyid;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
}