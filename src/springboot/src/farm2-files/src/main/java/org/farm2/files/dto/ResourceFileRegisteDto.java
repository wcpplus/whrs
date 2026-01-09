package org.farm2.files.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**附件注册 
 * @author cbtg自动生成  2025-2-4 10:42:08 
 */
@Data
@NoArgsConstructor
public class ResourceFileRegisteDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String ctime;
   
    private String appid;
   
    private String type;
   
    private String fileid;
   
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String note;
}