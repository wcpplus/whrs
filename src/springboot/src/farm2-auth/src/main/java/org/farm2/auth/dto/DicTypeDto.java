package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**字典类型 
 * @author cbtg自动生成  2025-1-7 15:27:42 
 */
@Data
@NoArgsConstructor
public class DicTypeDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String ctime;
   
    private String etime;
   
    private String euserkey;
   
    private String cuserkey;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String keyid;
   
    private Integer sortcode;
   
    private String entityid;
}