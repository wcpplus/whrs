package org.farm2.tools.db;

import java.util.Map;

/**
 * 迭代结果集合中行的处理接口配合result的runDataHandle方法使用
 */
public interface ResultDataHandle {
    public void handle(Map<String, Object> row);
}
