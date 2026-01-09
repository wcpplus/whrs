package org.farm2.wdap.domain;

import lombok.Data;

/**流程节点 
 * @author cbtg自动生成  2025-1-21 22:39:41 
 */
@Data
public class WdapFlowNode {
    private String id;
    private String pcontent;
    private String model;
    private String rid;
    private String flowid;
    private int timeout;
}