package org.farm2.service.domain;

import lombok.Data;

/**
 * 缓存的知识对象
 */
@Data
public class SortKnow {
    private String title;
    private String id;
    private SortFields sort = new SortFields();

    public SortKnow(String id, String title, String etime) {
        this.title = title;
        this.id = id;
        this.sort.setId(id);
        this.sort.setEtime(etime);
        this.sort.setTitle(title);
    }
}
