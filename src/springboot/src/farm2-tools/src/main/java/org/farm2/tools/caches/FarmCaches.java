package org.farm2.tools.caches;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.caches.domain.CacheObject;
import org.farm2.tools.caches.handle.FarmCachesCreator;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.structure.ResourceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FarmCaches {
    private static FarmCaches OBJ;
    private CacheManager cacheManager = null;

    private FarmCaches() {
    }

    public static FarmCaches getInstance() {
        return OBJ;
    }

    static {
        OBJ = new FarmCaches();
        OBJ.cacheManager = CacheManager.create();
        for (FarmCacheKeys node : FarmCacheKeys.values()) {
            // 定义缓存配置
            CacheConfiguration cacheConfiguration = new CacheConfiguration()
                    .name(node.name()) // 设置缓存名称
                    .maxEntriesLocalHeap(node.getMaxNum()) // 设置堆内存中最大条目数
                    .eternal(false) // 设置元素是否为永久的
                    .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU); // 设置淘汰策略
            if (node.getLiveSeconds() > 0) {
                // 设置存活过期时间（秒）
                cacheConfiguration.timeToLiveSeconds(node.getLiveSeconds());
            }
            if (node.getIdleSeconds() > 0) {
                // 设置空闲过期时间（秒）
                cacheConfiguration.timeToIdleSeconds(node.getIdleSeconds());
            }
            // 创建并添加缓存到CacheManager
            Cache myCache = new Cache(cacheConfiguration);
            OBJ.cacheManager.addCache(myCache);
        }
    }

    /**
     * 通过多个key生成一个缓存key
     *
     * @param keye
     * @return
     */
    public static String getKey(String... keye) {
        StringBuffer keyBuffer = new StringBuffer("KEY");
        for (String key : keye) {
            if (StringUtils.isNotBlank(key)) {
                keyBuffer.append("-" + key);
            }
        }
        return keyBuffer.toString();
    }

    /**
     * 创建一个毫无意义的key
     *
     * @return 创建一个毫无意义的key
     */
    public static String getNoneKey() {
        return "NONE";
    }

    /**
     * 清空所有缓存
     */
    public void clearAllCache() {
        String[] names = cacheManager.getCacheNames();
        if (names != null) {
            for (String name : names) {
                clearCache(name);
            }
        }
    }

    private void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.removeAll();
        }
    }

    /**
     * 清空指定缓存
     */
    public void clearCache(FarmCacheKeys cacheKeys) {
        clearCache(cacheKeys.name());
    }

    /**
     * 獲得cache的數量
     *
     * @return
     */
    public int getCacheSize(FarmCacheKeys cacheKeys, boolean isrefresh) {
        Cache cache = cacheManager.getCache(cacheKeys.name());
        if (cache == null) {
            System.err.println(I18n.msg("the cache  ? is not exist!", cacheKeys.name()));
            return 0;
        }
        if (isrefresh) {
            cache.evictExpiredElements();
        }
        return cache.getSize();
    }

    /**
     * 獲得所有缓存对象
     *
     * @return
     */
    public <T> List<T> getAll(FarmCacheKeys cacheKeys) {
        Cache cache = cacheManager.getCache(cacheKeys.name());
        List<T> all = new ArrayList<>();
        for (net.sf.ehcache.Element node : cache.getAll(cache.getKeys()).values()) {
            T data = (T) ((CacheObject) node.getObjectValue()).getVal();
            if (data != null) {
                all.add(data);
            }
        }
        return all;
    }


    /**
     * 通过部分key删除缓存
     *
     * @return
     */
    public void removeBySubKey(String subKey, FarmCacheKeys cacheKeys) {
        Cache cache = cacheManager.getCache(cacheKeys.name());
        for (Object key : cache.getKeys()) {
            if (key.toString().indexOf(subKey) >= 0) {
                removeCacheData(key.toString(), cacheKeys);
            }
        }
    }

    /**
     * 记录数据到缓存中
     *
     * @param key
     * @param val
     * @return
     */
    public void putCacheData(String key, Object val, FarmCacheKeys cacheKeys) {
        CacheObject obj = new CacheObject(val);
        final Cache live = cacheManager.getCache(cacheKeys.name());
        Element element = new Element(key, obj);
        live.put(element);
    }

    /**
     * 获得缓存数据
     *
     * @param key
     * @return
     */
    public <T> T getCacheData(String key, FarmCacheKeys cacheKeys) {
        final Cache live = cacheManager.getCache(cacheKeys.name());
        Element returnVal = live.get(key);
        if (returnVal == null) {
            return null;
        } else {
            return (T) ((CacheObject) returnVal.getObjectValue()).getVal();
        }
    }


    public <T> T getCacheData(String key, FarmCacheKeys cacheKeys, FarmCachesCreator creator) {
        if (key == null) {
            return (T) creator.handle(key);
        }
        if (FarmCaches.getInstance().isCacheData(key, cacheKeys)) {
            return FarmCaches.getInstance().getCacheData(key, cacheKeys);
        } else {
            Object content = creator.handle(key);
            FarmCaches.getInstance().putCacheData(key, content, cacheKeys);
            return (T) content;
        }
    }

    /**
     * @param key
     * @param cacheKeys
     * @param creator
     * @param isCache   为true时才使用缓存，否则直接返回
     * @param <T>
     * @return
     */
    public <T> T getCacheData(String key, FarmCacheKeys cacheKeys, FarmCachesCreator creator, boolean isCache) {
        if (isCache) {
            return getCacheData(key, cacheKeys, creator);
        } else {
            return (T) creator.handle(key);
        }
    }


    /**
     * 是否有缓存数据
     *
     * @param key
     * @param cacheKeys
     * @return
     */
    public Boolean isCacheData(String key, FarmCacheKeys cacheKeys) {
        final Cache live = cacheManager.getCache(cacheKeys.name());
        Element returnVal = live.get(key);
        if (returnVal == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除一个缓存数据
     *
     * @param key
     */
    public void removeCacheData(String key, FarmCacheKeys cacheKeys) {
        final Cache live = cacheManager.getCache(cacheKeys.name());
        live.remove(key);
    }


    public ResourceInfo getAllSize() {
        long use = 0;
        long all = 0;
        for (FarmCacheKeys key : FarmCacheKeys.values()) {
            use = use + getCacheSize(key, true);
            all = all + key.getMaxNum();
        }
        return new ResourceInfo("缓存用量", all, use);
    }

    public void refresh(FarmCacheKeys cacheKeys) {
        Cache cache = cacheManager.getCache(cacheKeys.name());
        cache.evictExpiredElements();
    }

//    public static void main(String[] args) {
//        FarmCaches caches = FarmCaches.getInstance();
//
//        caches.putCacheData("123", null, FarmCacheKeys.ONLINE_USER);
//
//        System.out.println(caches.isCacheData("123", FarmCacheKeys.ONLINE_USER));
//
//    }
}
