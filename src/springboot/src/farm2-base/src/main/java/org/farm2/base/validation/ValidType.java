package org.farm2.base.validation;

/**
 * 预定义验证类型
 */
public enum ValidType {

    loginName("^[a-zA-Z0-9]+$", "只能包含字母和数字"), pInteger("^(0|[1-9]\\d*)$", "只能是正整数");
    private String regEx; // 用于存储正则表达式
    private String message; // 用于存储错误信息

    ValidType(String regEx, String message) {
        this.regEx = regEx;
        this.message = message;
    }

    public String getRegEx() {
        return regEx;
    }


    public String getMessage() {
        return message;
    }

}
