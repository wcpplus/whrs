package org.farm2.tools.json;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FarmJsons {

    /**
     * 将bean转换为json
     *
     * @param bean
     * @return
     */
    public static String toJson(Object bean) {
        try {
            // 使用Fastjson2的JSON工具类将对象转换为JSON字符串
            return JSON.toJSONString(bean);
        } catch (Exception e) {
            // 捕获异常并抛出运行时异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 将bean转换为格式化json
     *
     * @param bean
     * @return
     */
    public static String toFormatJson(Object bean) {
        try {
            // 使用Fastjson2的JSON工具类将对象转换为格式化的JSON字符串
            return JSON.toJSONString(bean, JSONWriter.Feature.PrettyFormat);
        } catch (Exception e) {
            // 捕获异常并抛出运行时异常
            throw new RuntimeException(e);
        }
    }

    /**
     * 将json转换为对象
     * 使用方法，如：FarmJsons.toBean(wdapConvertor.getParams(), (new ArrayList<>()).getClass())
     *
     * @param json  JSON字符串
     * @param clazz 目标类的Class对象
     * @param <T>   泛型类型
     * @return 转换后的对象
     */
    public static <T> T toBean(String json, Class<T> clazz) {
        try {
            // 使用Fastjson2的JSON工具类将JSON字符串转换为目标对象
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            // 捕获异常并抛出运行时异常
            throw new RuntimeException("Failed to parse JSON string to object", e);
        }
    }
    public static Map<String, Object> jsonToMap(String string) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> map = objectMapper.readValue(string, Map.class);
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
