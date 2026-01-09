package org.farm2.base.parameter;

import lombok.Data;

@Data
public class ParameterGroupDomain {
    private String name;
    private String key;

    public ParameterGroupDomain(String name, String key) {
        this.name = name;
        this.key = key;
    }
}
