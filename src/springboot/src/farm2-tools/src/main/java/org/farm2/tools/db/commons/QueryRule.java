package org.farm2.tools.db.commons;

import java.util.Map;

/**
 * 查詢条件的封装
 */
public interface QueryRule {
    /**
     * @return 返回参数列表，配合getSafeSql使用，getSafeSql中插入模板，getValues返回参数
     */
    public Map<String, Object> getSafeValues();

    //keyFormat= #{rules[0].values.name}，name是getvalues中的key，最终会用map值替换这个#{rules[0].values.name}
    public String getSafeSql(String keyFormat);

    /**
     * @param keyFormat
     * @param isNullPass 空值是否忽略
     * @return
     */
    public String getSafeSql(String keyFormat, Boolean isNullPass);
}
