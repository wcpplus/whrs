package org.farm2.base.event.enums;

/**
 * 事件关系人类型
 */
public enum F2EUserT {
    OBJ_USER("对象作者"),
    ACTION_USER("操作人");

    private String title;

    F2EUserT(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
