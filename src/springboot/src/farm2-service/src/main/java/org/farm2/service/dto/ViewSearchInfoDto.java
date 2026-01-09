package org.farm2.service.dto;

import lombok.Data;

/**
 * 封装检索视图
 */
@Data
public class ViewSearchInfoDto {
    private String id;
    private String name;
    private String imgid;
    private String note;
    private String searchjson; //视图关键字
    private String resultModel; //结果类型
    private String themes; //展示主题
    private String cuserkey;
}
