package org.farm2.tools.bean;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class FarmBeanUtils {


    /**
     * 设置类的静态变量的值
     *
     * @param classStr 类的包名+类名，如org.farm2.tools.bean.FarmBeanUtils
     * @param name     属性名称
     * @param val      值
     */
    public static Boolean isSetClassField(String classStr, String name, String val) {
        try {
            // 获取目标类的 Class 对象
            Class<?> clazz = Class.forName(classStr);

            // 获取指定的静态字段
            Field field = clazz.getDeclaredField(name);

            // 确保可以访问私有字段
            field.setAccessible(true);

            // 根据字段类型进行转换（例如：String、int、boolean 等）
            Object convertedValue = convertValue(field.getType(), val);

            // 设置静态字段的值（静态字段的实例对象传入 null）
            field.set(null, convertedValue);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("类未找到: " + classStr, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("字段未找到: " + name, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法访问字段: " + name, e);
        }
        return true;
    }

    /**
     * 获取类的静态变量的值
     *
     * @param classStr 类的包名+类名，如org.farm2.tools.bean.FarmBeanUtils
     * @param name     属性名称
     * @return 静态变量的值（转换为字符串返回）
     */
    public static String getClassField(String classStr, String name) {
        try {
            // 获取目标类的 Class 对象
            Class<?> clazz = Class.forName(classStr);

            // 获取指定的静态字段
            Field field = clazz.getDeclaredField(name);

            // 确保可以访问私有字段
            field.setAccessible(true);

            // 获取静态字段的值（静态字段的实例对象传入 null）
            Object value = field.get(null);

            // 返回值转换为字符串
            return value != null ? value.toString() : null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("类未找到: " + classStr, e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("字段未找到: " + name, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("无法访问字段: " + name, e);
        }
    }

    /**
     * 根据字段类型将字符串值转换为对应类型的值
     *
     * @param fieldType 字段的类型
     * @param value     字符串值
     * @return 转换后的值
     */
    private static Object convertValue(Class<?> fieldType, String value) {
        if (fieldType == String.class) {
            return value;
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(value);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(value);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(value);
        } else if (fieldType == float.class || fieldType == Float.class) {
            return Float.parseFloat(value);
        } else if (fieldType == short.class || fieldType == Short.class) {
            return Short.parseShort(value);
        } else if (fieldType == byte.class || fieldType == Byte.class) {
            return Byte.parseByte(value);
        } else if (fieldType == char.class || fieldType == Character.class) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("无法将字符串转换为字符: " + value);
            }
            return value.charAt(0);
        } else {
            throw new IllegalArgumentException("不支持的字段类型: " + fieldType.getName());
        }
    }
    /**
     * 讲map中的数据拷贝到对象中(map的key不分大小写)
     *
     * @param map
     * @param bean
     */
    public static Object copyProperties(Map<String, ?> map, Object bean) {
        Map<String, Object> upperCaseMap = new java.util.HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            upperCaseMap.put(entry.getKey().toUpperCase(), entry.getValue());
        }
        // 获取目标对象的所有字段
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toUpperCase();
            if (upperCaseMap.containsKey(fieldName)) {
                try {
                    // 设置字段可访问
                    field.setAccessible(true);
                    // 将 Map 中的值赋给目标对象的字段
                    field.set(bean, upperCaseMap.get(fieldName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace(); // 在生产环境中应该更优雅地处理异常
                }
            }
        }
        return bean;
    }


    /**
     * 将bean的属性拷贝到map中(key会转换为大写)
     *
     * @param bean
     * @param map
     */
    public static Map<String, Object> copyProperties(Object bean, Map<String, Object> map) {
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                // 设置字段可访问
                field.setAccessible(true);
                // 获取字段名和字段值
                String fieldName = field.getName();
                Object fieldValue = field.get(bean);
                if (fieldValue != null) {
                    // 将字段名和字段值放入Map中
                    map.put(fieldName.toUpperCase(), fieldValue);
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }


    /**当value值为空时，执行方法setter，设置默认值defaultValue
     * @param value
     * @param defaultValue
     * @param setter
     */
    public static void runFunctionByBlank(Object value, String defaultValue, Consumer<String> setter) {
        boolean isNull = false;
        if (value instanceof String) {
            isNull = StringUtils.isBlank((String) value);
        } else {
            isNull = Objects.isNull(value);
        }
        if (isNull && StringUtils.isNotBlank(defaultValue)) {
            setter.accept(defaultValue);
        }
    }

    /**
     * 将对象sObj的属性拷贝到tObj中
     *
     * @param sObj 來源
     * @param tObj 目標
     */
    public static <T> T copyProperties(Object sObj, T tObj) {
        if (sObj == null) {
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(sObj, tObj);
        return tObj;
    }

    /**
     * 通过class获取对象实例
     *
     * @param implclass  如：org.farm2.tools.bean.FarmBeanUtils
     * @return
     * @throws Exception
     */
    public static Object getInstance(String implclass) throws Exception {
        Class<?> clazz = null;
        clazz = Class.forName(implclass);
        // 创建实例（假设该类有一个无参构造函数）
        return clazz.getDeclaredConstructor().newInstance();
    }

    public static Map<String, ?> toMap(Object stag) {
        Map map = new BeanMap(stag);
        return map;
    }
}
