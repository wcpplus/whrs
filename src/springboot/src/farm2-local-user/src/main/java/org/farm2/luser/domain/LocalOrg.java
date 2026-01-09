package org.farm2.luser.domain;

import lombok.Data;

/**组织机构 
 * @author cbtg自动生成  2025-1-2 22:01:57 
 */
@Data
public class LocalOrg {
    private String id;
    private String code;
    private String name;
    private String ctime;
    private String etime;
    private String euserkey;
    private String cuserkey;
    private String state;
    private String note;
    private Integer sortcode;
    private String parentid;
    private String treecode;
}