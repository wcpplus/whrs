package org.farm2.base.utils;

import lombok.Data;

@Data
public class FarmValueKey {
    private String title;
    private String key;

    public FarmValueKey(String title, String key) {
        this.title = title;
        this.key = key;
    }
}
