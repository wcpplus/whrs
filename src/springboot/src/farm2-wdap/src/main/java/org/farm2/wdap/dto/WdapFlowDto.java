package org.farm2.wdap.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**转换流程 
 * @author cbtg自动生成  2025-1-21 18:42:38 
 */
@Data
@NoArgsConstructor
public class WdapFlowDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法
   
    private String id;
   
    private String ctime;
   
    private String etime;
   
    private String euserkey;
   
    private String cuserkey;
   
    private String state;
   
    @Size(min = 0, max = 512, message = "{farm2.validate.template.size}")
    private String note;
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String name;
    private String modelkeys;
   
    private Integer sizemin;
   
    private Integer sizemax;

    private List<String> exname;
}