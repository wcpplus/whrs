package org.farm2.wdap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**转换任务 
 * @author cbtg自动生成  2025-1-25 9:21:04 
 */
@Data
@NoArgsConstructor
public class WdapTaskDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String fileid;
   
    private String pstate;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 65535, message = "{farm2.validate.template.size}")
    private String logjson;
   
    private String etime;
   
    private String ctime;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
   
    private String serverid;

    private String exPath;//拓展文件夹
}