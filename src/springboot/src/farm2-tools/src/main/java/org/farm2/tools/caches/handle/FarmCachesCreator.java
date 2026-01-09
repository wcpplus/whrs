package org.farm2.tools.caches.handle;

/**
 * 知识创建者（通过该接口像缓存注入对象）
 */
public interface FarmCachesCreator {
    public Object handle(String key);
}
