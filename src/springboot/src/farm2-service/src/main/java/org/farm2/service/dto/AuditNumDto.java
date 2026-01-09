package org.farm2.service.dto;

import lombok.Data;


/**
 * 知识审核信息封装
 */
@Data
public class AuditNumDto {
    private int task;
    private int wait;
    private int history;

    public int getNum() {
        if (task == 0 && wait == 0) {
            return 0;
        }
        return task > wait ? task : wait;
    }

    public String getNumModel() {
        if (task == 0 && wait == 0) {
            return "HIS";
        }
        return task > wait ? "TASK" : "WAIT";
    }
}
