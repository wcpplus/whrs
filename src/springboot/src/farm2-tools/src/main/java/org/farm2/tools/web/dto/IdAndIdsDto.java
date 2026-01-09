package org.farm2.tools.web.dto;

import java.util.List;

/**
 * 封装id和ids的查询对象
 */
public class IdAndIdsDto {

    private String id;
    private List<String> ids;


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
}
