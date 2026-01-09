package org.farm2.tools.web;

public enum FarmResponseCode {

    SUCESS(1, "成功"), ERROR(0, "失败"), WARN(2, "警告");
    private int code;
    private String title;

    private FarmResponseCode(int code, String title) {
        this.code = code;
        this.title = title;
    }

    public int getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }
}
