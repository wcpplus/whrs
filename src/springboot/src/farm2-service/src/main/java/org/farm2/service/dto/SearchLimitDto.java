package org.farm2.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchLimitDto {
    private String model;
    private List<String> val;

    public SearchLimitDto() {
        super();
    }

    public SearchLimitDto(String model, List<String> val) {
        this.model = model;
        this.val = val;
    }
}
