package org.farm2.main.cecurity.dto;

import lombok.Data;

import java.util.List;

@Data
public class KeyDto {
    private String key;
    private List<String> keys;
}
