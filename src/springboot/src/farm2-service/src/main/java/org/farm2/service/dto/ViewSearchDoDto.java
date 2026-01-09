package org.farm2.service.dto;

import lombok.Data;

@Data
public class ViewSearchDoDto {
    private String viewId;
    private String word;
    private Integer page;
    private String json;
    private String model;
}
