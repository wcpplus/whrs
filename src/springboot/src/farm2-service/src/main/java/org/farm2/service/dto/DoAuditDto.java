package org.farm2.service.dto;

import lombok.Data;

/**
 * 知识审核信息封装
 */
@Data
public class DoAuditDto {
    private String taskId;
    private boolean pass;
    private String note;
}