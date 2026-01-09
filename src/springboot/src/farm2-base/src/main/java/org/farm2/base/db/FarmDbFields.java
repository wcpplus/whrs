package org.farm2.base.db;

import org.farm2.base.domain.FarmUserContext;
import org.farm2.tools.time.FarmTimeTool;

import java.lang.reflect.Field;

/**
 * 处理bean在数据库操作前初始化或设置默认数据值
 */
public class FarmDbFields {
    /**插入对象时添加更新信息，用户和时间
     * @param bean
     * @param currentUser
     */
    public static void initInsertBean(Object bean, FarmUserContext currentUser) {
        updateField(bean, "id", null);
        initField(bean, "ctime", FarmTimeTool.getTimeDate14());
        initField(bean, "etime", FarmTimeTool.getTimeDate14());
        if (currentUser != null) {
            initField(bean, "cuserkey", currentUser.getLoginname());
            initField(bean, "euserkey", currentUser.getLoginname());
            initField(bean, "cuser", currentUser.getLoginname());
            initField(bean, "euser", currentUser.getLoginname());
            initField(bean, "cusername", currentUser.getName());
            initField(bean, "eusername", currentUser.getName());
        }
        initField(bean, "state", "1");
        initField(bean, "sortcode", "1");
        initField(bean, "parentid", "NONE");
        initField(bean, "treecode", "NONE");
    }

    /**修改对象时添加更新信息，更新用户和更新时间
     * @param bean
     * @param currentUser
     */
    public static void initUpdateBean(Object bean, FarmUserContext currentUser) {
        updateField(bean, "etime", FarmTimeTool.getTimeDate14());
        if (currentUser != null) {
            initField(bean, "euserkey", currentUser.getLoginname());
        }
    }

    /**
     * 是否存在字段
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static boolean hasField(Class<?> clazz, String fieldName) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 查找 bean 中的 field 属性，如果有且为空就将 val 赋值给这个字段。
     *
     * @param bean  要操作的对象
     * @param field 字段名
     * @param val   要赋值的值
     */
    public static void initField(Object bean, String field, String val) {
        if (bean == null || field == null || field.isEmpty()) {
            throw new IllegalArgumentException("bean or field cannot be null or empty");
        }
        try {
            // 获取类的 Class 对象
            Class<?> clazz = bean.getClass();
            if (!hasField(bean.getClass(), field)) {
                //不存在属性就结束
                return;
            }
            // 获取指定名称的字段
            Field f = clazz.getDeclaredField(field);
            // 设置字段可访问（即使它是私有的）
            f.setAccessible(true);
            // 获取字段的当前值
            Object currentValue = f.get(bean);
            // 检查字段是否为空
            if (currentValue == null || (currentValue instanceof String && ((String) currentValue).isEmpty())) {
                // 如果字段为空，则设置新值
                if (f.getType().equals(Integer.class)) {
                    f.set(bean, Integer.valueOf(val));
                } else {
                    f.set(bean, val);
                }
            }
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field '" + field + "' not found in " + bean.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field '" + field + "'", e);
        }
    }

    /**
     * 查找 bean 中的 field 属性，直接val 赋值给这个字段。
     *
     * @param bean  要操作的对象
     * @param field 字段名
     * @param val   要赋值的值
     */
    public static void updateField(Object bean, String field, String val) {
        if (bean == null || field == null || field.isEmpty()) {
            throw new IllegalArgumentException("bean or field cannot be null or empty");
        }
        try {
            // 获取类的 Class 对象
            Class<?> clazz = bean.getClass();
            if (!hasField(bean.getClass(), field)) {
                //不存在属性就结束
                return;
            }
            // 获取指定名称的字段
            Field f = clazz.getDeclaredField(field);
            // 设置字段可访问（即使它是私有的）
            f.setAccessible(true);
            // 获取字段的当前值
            Object currentValue = f.get(bean);
            //  则设置新值
            f.set(bean, val);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field '" + field + "' not found in " + bean.getClass().getName(), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access field '" + field + "'", e);
        }
    }

}
