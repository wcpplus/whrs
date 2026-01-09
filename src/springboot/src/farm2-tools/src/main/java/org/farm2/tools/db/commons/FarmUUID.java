package org.farm2.tools.db.commons;

import com.github.f4b6a3.uuid.UuidCreator;

import java.lang.reflect.Field;
import java.util.UUID;

public class FarmUUID {

    /**生成32位的UUID
     * @return
     */
    public static String getUUID32() {
        return UuidCreator.getTimeOrderedEpoch().toString().replaceAll("-", "");
    }

    /**将32位的uuid填充到对象obj中
     * @param object 要填充的对象
     * @param fieldName 要填充uuid的字段名称
     * @param <T>  对象类型
     * @return 返回填充好数据的对象
     */
    public static <T> T to(T object, String fieldName) {
        if (object == null || fieldName == null || fieldName.isEmpty()) {
            return object;
        }
        try {
            // 获取类的所有声明字段，包括私有字段
            Field[] fields = object.getClass().getDeclaredFields();
            // 将传入的字段名转换为小写
            String lowerCaseFieldName = fieldName.toLowerCase();
            for (Field field : fields) {
                // 将字段名也转换为小写以进行不区分大小写的比较
                if (field.getName().toLowerCase().equals(lowerCaseFieldName)) {
                    // 允许访问私有字段
                    field.setAccessible(true);
                    // 检查字段类型是否为String
                    if (field.getType() == String.class) {
                        // 获取字段的值
                        String fieldValue = (String) field.get(object);
                        // 如果字段为空或null，则设置为"UUID"
                        if (fieldValue == null || fieldValue.trim().isEmpty()) {
                            field.set(object, getUUID32());
                        }
                    }
                    break; // 找到匹配的字段后退出循环
                }
            }
        } catch (Exception e) {
            // 你可以选择在这里记录日志或抛出异常
            e.printStackTrace();
        }
        return object;
    }
}
