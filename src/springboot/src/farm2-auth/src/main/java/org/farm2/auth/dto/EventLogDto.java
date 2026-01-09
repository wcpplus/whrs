package org.farm2.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**事件日志 
 * @author cbtg自动生成  2025-1-8 22:28:50 
 */
@Data
@NoArgsConstructor
public class EventLogDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String ctime;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String actiontype;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String objtype;
   
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String objid;
   
    private String userip;
   
    @Size(min = 0, max = 64, message = "{farm2.validate.template.size}")
    private String userkey;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String note;

    private String ouserkey;
}