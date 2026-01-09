package org.farm2.tools.caches.domain;

/**
 *封装缓存对象
 */
public class CacheObject {

    private Object val = null;

    public CacheObject() {

    }

    public CacheObject(Object val) {
        this.val = val;
    }

    public Object getVal() {
        return val;
    }
}
