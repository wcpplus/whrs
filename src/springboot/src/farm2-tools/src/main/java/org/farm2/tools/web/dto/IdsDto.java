package org.farm2.tools.web.dto;

import java.util.List;

/**
 * 封装id和ids的查询对象
 */
public class IdsDto {

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    private List<String> ids;

}
