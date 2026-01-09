package org.farm2.service.dto;

import lombok.Data;

/**
 * 编辑知识时从后台查询知识信息
 */
@Data
public class KnowByEditDto {
    private String id;
    private String versionid;
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
