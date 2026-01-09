package org.farm2.exread.domain;

import org.farm2.exread.domain.service.ReaderConfig;

public class ColumnConfig {
    private int num;
    private String key;
    private ReaderConfig.ColumnType columnType;

    public ColumnConfig(int num, String key, ReaderConfig.ColumnType columnType) {
        this.num = num;
        this.key = key;
        this.columnType = columnType;
    }

    public ColumnConfig(int num, ReaderConfig.ColumnType columnType) {
        this.num = num;
        this.key = Integer.toString(num);
        this.columnType = columnType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ReaderConfig.ColumnType getColumnType() {
        return columnType;
    }

    public void setColumnType(ReaderConfig.ColumnType columnType) {
        this.columnType = columnType;
    }
}
