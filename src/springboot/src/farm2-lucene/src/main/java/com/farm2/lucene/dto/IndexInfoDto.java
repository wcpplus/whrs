package com.farm2.lucene.dto;

import lombok.Data;

/**
 * 索引信息
 */
@Data
public class IndexInfoDto {
    private String path;
    private long size;
    private String title;
}
