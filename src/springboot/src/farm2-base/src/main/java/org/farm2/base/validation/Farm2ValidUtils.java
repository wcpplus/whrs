package org.farm2.base.validation;

import org.apache.commons.lang3.StringUtils;

/**
 * 手动验证工具
 */
public class Farm2ValidUtils {


    /**
     * 验证方法
     *
     * @param val        验证值
     * @param type       验证类型
     * @param throwEAble 是否抛出异常：
     * @return
     */
    public static boolean valid(String val, ValidType type, boolean unllAble, boolean throwEAble, String title) {
        if (val == null) {
            if (unllAble) {
                return true;
            }
            if (throwEAble) {
                throw new IllegalArgumentException((title == null ? "" : title) + "参数不能为null");
            }
            return false;
        }
        String regEx = type.getRegEx();
        boolean isValid = val.matches(regEx);
        if (!isValid && throwEAble) {
            throw new IllegalArgumentException((title == null ? "" : title) + val + ":" + type.getMessage());
        }
        return isValid;
    }

    /**
     * 验证方法
     *
     * @param val        验证值
     * @param type       验证类型
     * @param throwEAble 是否抛出异常：
     * @return
     */
    public static boolean valid(String val, ValidType type, boolean unllAble, boolean throwEAble) {
        return valid(val, type, unllAble, throwEAble, null);
    }
    /**
     * 普通校验
     *
     * @param val        值
     * @param unllAble   是否可以空
     * @param minNum     最小长度
     * @param maxNum     最大长度
     * @param throwEAble 是否可以抛出异常
     * @return
     */
    public static boolean valid(String val, boolean unllAble, int minNum, int maxNum, boolean throwEAble) {
        return valid(val, unllAble, minNum, maxNum, throwEAble, null);
    }

    /**
     * 普通校验
     *
     * @param val        值
     * @param unllAble   是否可以空
     * @param minNum     最小长度
     * @param maxNum     最大长度
     * @param throwEAble 是否可以抛出异常
     * @return
     */
    public static boolean valid(String val, boolean unllAble, int minNum, int maxNum, boolean throwEAble, String title) {
        // 检查 val 是否为 null 或空字符串
        if (StringUtils.isBlank(val)) {
            if (unllAble) {
                return true; // 如果允许为空且 val 确实为空，则直接返回 true
            }
            if (throwEAble) {
                throw new IllegalArgumentException((title == null ? "" : title) + "值不能为空");
            }
            return false;
        }

        // 获取字符串的实际长度
        int length = val.length();

        // 检查长度范围
        if (length < minNum || length > maxNum) {
            if (throwEAble) {
                throw new IllegalArgumentException(
                        String.format((title == null ? "" : title) + "字符串长度必须在 %d 到 %d 之间", minNum, maxNum));
            }
            return false;
        }

        // 所有校验通过
        return true;
    }
}
