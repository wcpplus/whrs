package org.farm2.tools.web.dto;

import java.util.List;

/**
 * 修改状态时封装参数
 */
public class StateDto {
    private String id;
    private List<String> ids;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
