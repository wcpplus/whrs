package org.farm2.social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**用户消息 
 * @author cbtg自动生成  2025-4-23 10:29:47 
 */
@Data
@NoArgsConstructor
public class UserMessageDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String ctime;
   
    private String cuserkey;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
   
    private String readuserkey;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 1024, message = "{farm2.validate.template.size}")
    private String content;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String title;
   
    private String readstate;
}