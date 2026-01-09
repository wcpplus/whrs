package org.farm2.base.db;

import lombok.Data;

/**
 * 封装一个查询字段
 */
@Data
public class DataSelectField {
    private String title;
    private String asName;

    public String toSql() {
        return asName.toUpperCase() + "." + title.toUpperCase() + " AS " + title.toUpperCase();
    }
}
