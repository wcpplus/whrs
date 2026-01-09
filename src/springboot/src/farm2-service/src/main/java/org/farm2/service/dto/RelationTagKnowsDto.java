package org.farm2.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class RelationTagKnowsDto {
    private String tagId;
    private List<String> tagKeys;
}
