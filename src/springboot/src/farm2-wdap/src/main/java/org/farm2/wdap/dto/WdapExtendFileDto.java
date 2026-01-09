package org.farm2.wdap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**扩展附件 
 * @author cbtg自动生成  2025-1-25 18:24:40 
 */
@Data
@NoArgsConstructor
public class WdapExtendFileDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String taskid;
   
    private String fileid;
   
    private String filemodel;
   
    private String viewis;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
   
    private String resourcekey;
   
    private String serverid;

    private String dirpath;
}