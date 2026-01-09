package org.farm2.service.enums;

/**
 * 用户的知识权限，不同与分类权限（还是有一些差别的）
 */
public enum FarmKnowPopEnum {
    VIEW("知识阅读"),
    EDIT("知识发布"),
    DELETE("知识删除"),
    FILE_VIEW("附件预览"),
    FILE_DOWNLOAD("附件下载"),
    KNOW_SHARE("知识分享"),
    KNOW_AUDIT("知识审核"),
    TYPE_MANAGE("分类管理");
    private String title;

    FarmKnowPopEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
