package org.farm2.report;

import lombok.Data;

@Data
public class FieldDemo {
    private String key;
    private String val;

    public FieldDemo() {
    }

    public FieldDemo(String key, String val) {
        this.key = key;
        this.val = val;

    }
}
