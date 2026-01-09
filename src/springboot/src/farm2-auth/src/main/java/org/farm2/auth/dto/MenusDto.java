package org.farm2.auth.dto;

import lombok.Data;
import org.farm2.base.elementplus.domain.TreeNode;

import java.util.List;

@Data
public class MenusDto {
    private String id;
    //菜单名称
    private String name;
    //排序号
    private Integer sortcode;
    //菜单路由地址
    private String menukey;
    List<MenusDto> children;
    private String domain;
}
