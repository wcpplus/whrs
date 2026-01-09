package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**角色类别关系 
 * @author cbtg自动生成  2025-10-29 11:56:43 
 */
@Data
@NoArgsConstructor
public class AuthPostPtypeDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String postid;
   
    private String ptypeid;
}