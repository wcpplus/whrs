package org.farm2.wdap.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.farm2.wdap.utils.FlowNodeAddModel;

/**
 * 添加节点参数封装
 */
@Data
@NoArgsConstructor
public class WdapAddNodeDto {
    String flowid;
    String baseNodeId;
    FlowNodeAddModel flowModel;
}
