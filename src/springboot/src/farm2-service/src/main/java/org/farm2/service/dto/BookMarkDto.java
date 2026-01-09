package org.farm2.service.dto;

import lombok.Data;

@Data
public class BookMarkDto {
    private String appId;
    private String appModel;
    private String id;
    private int start;
    private int end;
    private String text;
    private String note;
    private String color;
    private String share;
    private boolean selfMark;
    private String dombk;
    private String ctime;
    private String cuser;
}
