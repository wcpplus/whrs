package org.farm2.luser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**组织机构 
 * @author cbtg自动生成  2025-1-2 22:01:57 
 */
@Data
@NoArgsConstructor
public class LocalOrgDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
    private String id;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 64, message = "{farm2.validate.template.size}")
    private String name;
   
    private String ctime;

    private String code;
   
    private String etime;
   
    private String euserkey;
   
    private String cuserkey;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
   
    private Integer sortcode;
   
    private String parentid;
    private String treecode;
}