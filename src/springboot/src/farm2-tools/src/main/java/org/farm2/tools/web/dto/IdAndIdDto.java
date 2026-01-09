package org.farm2.tools.web.dto;

import java.util.List;

/**
 * 封装id和ids的查询对象
 */
public class IdAndIdDto {
    private String id;
    private String id1;
    private String id2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }
}
