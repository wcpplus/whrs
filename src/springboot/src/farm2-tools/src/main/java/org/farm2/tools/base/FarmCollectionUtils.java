package org.farm2.tools.base;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class FarmCollectionUtils {
    /**
     * 计算两个集合的交集
     *
     * @param coll1
     * @param coll2
     * @return
     */
    public static Collection<String> getIntersection(Collection<String> coll1, Collection<String> coll2) {
        // 将第一个集合转为 HashSet，便于快速查找
        Set<String> set = new HashSet<>(coll1);
        // 创建结果集
        Set<String> result = new HashSet<>();
        // 遍历第二个集合，查找存在于第一个集合中的元素
        for (String item : coll2) {
            if (set.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }
}
