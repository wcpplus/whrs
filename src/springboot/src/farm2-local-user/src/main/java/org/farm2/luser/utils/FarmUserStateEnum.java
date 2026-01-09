package org.farm2.luser.utils;

import lombok.Getter;
import lombok.Setter;

public enum FarmUserStateEnum {

    //待入职、在职（试用）、在职（正式）、已离职。
    PENDING("待入职", "0"),
    PROBATION("试用", "1"),
    ACTIVE("正式", "2"),
    LEFT("离职", "3"),
    RETIRED("退休", "4"),
    DEL("删除", "9");
    @Getter
    private final String title;
    @Getter
    private final String type;

    FarmUserStateEnum(String title, String type) {
        this.title = title;
        this.type = type;
    }

    public static FarmUserStateEnum valueOfType(String state) {
        if (state == null || state.isEmpty()) {
            return null;
        }
        for (FarmUserStateEnum item : values()) {
            if (item.type.equals(state)) {
                return item;
            }
        }
        throw new IllegalArgumentException("Unknown state: " + state);
    }
}
