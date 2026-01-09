package org.farm2.service.dto;

import lombok.Data;

/**
 * 封装评论信息
 */
@Data
public class CommentDto {
    private String msg;
    private String appId;
    private String appModel;
    private String parentId;//被回复评论
    private String id;
}
