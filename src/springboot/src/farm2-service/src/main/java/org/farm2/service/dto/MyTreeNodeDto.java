package org.farm2.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装标签知识（树结构，含有分类知识）
 */
@Data
public class MyTreeNodeDto {
    private String id; //    id: string;分类或知识id
    private String label; //    label: string;分类或知识标题
    private int level; //    level: number;分类层级，9为知识标题
    private int sort;
    private String parentId;
    private int num;
    private String model;
    private String style;
    private List<MyTreeNodeDto> children = new ArrayList<>();//    children?: Array<TagTreeNode>;子分类或知识

    public MyTreeNodeDto(String id, String parentId, String label, int level, int sort, String model, int num, String style) {
        this.id = id;
        this.label = label;
        this.level = level;
        this.sort = sort;
        this.parentId = parentId;
        this.model = model;
        this.num = num;
        this.style = style;
    }

    public MyTreeNodeDto(String id, String parentId, String label, int level, int sort, String model, int num, String style, List<MyTreeNodeDto> children) {
        this.id = id;
        this.label = label;
        this.level = level;
        this.sort = sort;
        this.parentId = parentId;
        this.children = children;
        this.model = model;
        this.num = num;
        this.style = style;
    }
}
