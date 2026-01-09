package org.farm2.wdap.domain;

import lombok.Data;

/**转换流程 
 * @author cbtg自动生成  2025-1-21 18:42:38 
 */
@Data
public class WdapFlow {
    private String id;
    private String ctime;
    private String etime;
    private String euserkey;
    private String cuserkey;
    private String state;
    private String note;
    private String name;
    private String modelkeys;
    private Integer sizemin;
    private Integer sizemax;
    private String exname;
}