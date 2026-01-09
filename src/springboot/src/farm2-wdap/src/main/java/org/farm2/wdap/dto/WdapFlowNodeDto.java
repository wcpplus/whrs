package org.farm2.wdap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**流程节点 
 * @author cbtg自动生成  2025-1-21 22:39:41 
 */
@Data
@NoArgsConstructor
public class WdapFlowNodeDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String pcontent;
    @NotBlank(message = "{farm2.validate.template.notnull}")
    @Size(min = 0, max = 128, message = "{farm2.validate.template.size}")
    private String model;
   
    private String rid;
   
    private String flowid;
    private int timeout;
}