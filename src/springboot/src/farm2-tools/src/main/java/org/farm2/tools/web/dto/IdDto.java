package org.farm2.tools.web.dto;

/**
 * 封装id
 */
public class IdDto {
    private String id;
    private Boolean cacheAble;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCacheAble() {
        if (cacheAble == null) {
            return true;
        } else {
            return cacheAble;
        }
    }

    public void setCacheAble(Boolean cacheAble) {
        this.cacheAble = cacheAble;
    }
}
