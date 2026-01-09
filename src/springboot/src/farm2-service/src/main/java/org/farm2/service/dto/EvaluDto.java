package org.farm2.service.dto;

import lombok.Data;

/**
 * 封装评价信息
 */
@Data
public class EvaluDto {
    private String appId;
    private String appModel;
    private int point;
}
