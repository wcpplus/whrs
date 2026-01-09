package org.farm2.wdap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**流程连线 
 * @author cbtg自动生成  2025-1-21 22:42:51 
 */
@Data
@NoArgsConstructor
public class WdapFlowLinkDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String pcontent;
   
    private String snodeid;
   
    private String tnodeid;
   
    private String flowid;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 1024, message = "{farm2.validate.template.size}")
    private String expression;
}