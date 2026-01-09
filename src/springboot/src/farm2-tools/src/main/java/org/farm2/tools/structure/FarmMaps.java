package org.farm2.tools.structure;

import java.util.HashMap;
import java.util.Map;

/**
 * 辅助数据结构使用的工具类
 */
public class FarmMaps {
    private Map<String, Object> map = new HashMap<>();

    public static FarmMaps getInstance() {
        return new FarmMaps();
    }

    public FarmMaps put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<String, Object> getMap() {
        return map;
    }

}
