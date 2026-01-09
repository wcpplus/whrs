package org.farm2.service.dto;

import lombok.Data;

@Data
public class SearchTypeKnowDto {
    private String typeid;
    private int pagesize;
    private int page;
    private String sortModel;
}
