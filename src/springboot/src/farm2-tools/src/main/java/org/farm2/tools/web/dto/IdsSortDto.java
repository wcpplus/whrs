package org.farm2.tools.web.dto;

import java.util.List;

public class IdsSortDto {
    private Integer sort;
    private List<String> ids;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
