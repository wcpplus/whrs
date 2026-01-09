package org.farm2.base.process;

import lombok.Data;

/**
 * 封装process的key
 */
@Data
public class FarmProcessKey {
    private String key;
    private FarmProcessTypeEnum type;

    public FarmProcessKey(String key, FarmProcessTypeEnum type) {
        this.key = key;
        this.type = type;
    }
}
