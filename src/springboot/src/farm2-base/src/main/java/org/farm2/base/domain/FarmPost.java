package org.farm2.base.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class FarmPost implements Serializable {
    private String key;
    private String name;
    private String id;

    public FarmPost(String key, String name, String id) {
        this.key = key;
        this.name = name;
        this.id = id;
    }
}
