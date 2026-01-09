package org.farm2.wdap.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务状态
 */
public enum WdapTaskStateEnum {
    WAIT("0", "等待"), RUNNING("1", "转换中"), COMPLETE("2", "完成"), DISABLE("3", "无需转换"), ERROR("9", "失败");

    public String code;
    public String title;

    WdapTaskStateEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static Map<String, String> getDic() {
        Map<String, String> dic = new HashMap<>();
        for (WdapTaskStateEnum node : WdapTaskStateEnum.values()) {
            dic.put(node.code, node.title);
        }
        return dic;
    }
}
