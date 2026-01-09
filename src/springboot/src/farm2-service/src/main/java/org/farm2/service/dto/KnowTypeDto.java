package org.farm2.service.dto;

import lombok.Data;

/**
 * 封装知识分类信息
 */
@Data
public class KnowTypeDto {
    private String typeId;
    private String parentId;
    private String name;
    private int knowNum;//当前分类下知识
    private boolean isMoreKnow;//是否子分类有更多知识
}
