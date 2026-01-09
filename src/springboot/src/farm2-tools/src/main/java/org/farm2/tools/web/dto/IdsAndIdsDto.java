package org.farm2.tools.web.dto;

import java.util.List;

/**
 * 封装id和ids的查询对象
 */
public class IdsAndIdsDto {

    private List<String> ids1;
    private List<String> ids2;

    public List<String> getIds1() {
        return ids1;
    }

    public void setIds1(List<String> ids1) {
        this.ids1 = ids1;
    }

    public List<String> getIds2() {
        return ids2;
    }

    public void setIds2(List<String> ids2) {
        this.ids2 = ids2;
    }
}
