package org.farm2.files.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**附件 
 * @author cbtg自动生成  2025-1-13 14:50:11 
 */
@Data
@NoArgsConstructor
public class ResourceFileDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String ctime;
   
    private String cuserkey;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
   
    private String exname;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 1024, message = "{farm2.validate.template.size}")
    private String relativepath;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String filename;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String title;
   
    private Integer filesize;
   
    private String resourcekey;
   
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String appid;

    private String fullpath;
}