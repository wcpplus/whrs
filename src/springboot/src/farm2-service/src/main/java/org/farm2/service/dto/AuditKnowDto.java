package org.farm2.service.dto;

import lombok.Data;

/**
 * 知识审核信息封装
 */
@Data
public class AuditKnowDto {
    private String id;
    private boolean audit;
    private String versionId;
}
