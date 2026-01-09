package org.farm2.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KeyValueDto {
    private String key;
    private String val;
    private String note;
    private String time;
}
