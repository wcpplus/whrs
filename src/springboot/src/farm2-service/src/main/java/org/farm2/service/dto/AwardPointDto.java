package org.farm2.service.dto;

import lombok.Data;

/**
 * 奖励积分属性封装
 */
@Data
public class AwardPointDto {
    private String appid;
    private String appModle;
    private int point;
}
