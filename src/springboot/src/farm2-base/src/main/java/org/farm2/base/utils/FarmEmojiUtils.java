package org.farm2.base.utils;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class FarmEmojiUtils {
    //-------------------------处理emoji字符串-------开始--------------------------------------------

    /**
     * Unicode表情符号的正则表达式
     */
    private static final Pattern EMOJI_PATTERN = Pattern.compile(
            "[\\uD83C\\uDF00-\\uD83D\\uDDFF]|" +
                    "[\\uD83E\\uDD00-\\uD83E\\uDDFF]|" +
                    "[\\uD83D\\uDC00-\\uD83D\\uDE4F]|" +
                    "[\\uD83D\\uDE80-\\uD83D\\uDEFF]"
    );

    /**
     * 处理emoji字符串
     *
     * @param bean 需要处理的对象
     */
    public static void emojiHandle(Object bean) {
        if (bean == null) {
            return;
        }

        // 获取对象的所有字段
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // 只处理字符串类型的字段
            if (field.getType() == String.class) {
                try {
                    // 允许访问私有字段
                    field.setAccessible(true);

                    // 获取字段值
                    String value = (String) field.get(bean);
                    if (value != null) {
                        // 删除emoji字符
                        String filteredValue = removeEmoji(value);

                        // 将过滤后的值重新设置回字段
                        field.set(bean, filteredValue);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 删除字符串中的emoji字符
     *
     * @param input 输入字符串
     * @return 过滤后的字符串
     */
    private static String removeEmoji(String input) {
        return EMOJI_PATTERN.matcher(input).replaceAll("");
    }

//------------------------处理emoji字符串-----------结束------------------------------------------
}
