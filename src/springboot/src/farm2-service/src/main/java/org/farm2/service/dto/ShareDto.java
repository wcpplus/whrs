package org.farm2.service.dto;

import lombok.Data;

/**
 * 封装链接分享信息
 */
@Data
public class ShareDto {
    private String appId;
    private String appModel;
    private int time;
    private String type;
    private String link;
    private String code;
    private String title;
    private Boolean shareIs;
}
