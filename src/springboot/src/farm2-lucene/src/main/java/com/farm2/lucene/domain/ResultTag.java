package com.farm2.lucene.domain;

import lombok.Data;

/**
 * 封装索引记录的tag
 */
@Data
public class ResultTag {
    private String title;
    private int knowNum;

    public ResultTag(String title) {
        this.title = title;
        this.knowNum = 0;
    }

    public void add(int addNum) {
        knowNum = knowNum + addNum;
    }
}
