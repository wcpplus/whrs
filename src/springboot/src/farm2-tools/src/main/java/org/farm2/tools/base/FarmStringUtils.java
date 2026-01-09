package org.farm2.tools.base;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.db.commons.FarmUUID;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FarmStringUtils {

    /**
     * 转为拼音
     *
     * @param chinese
     * @return
     */
    public static String convertToPinyin(String chinese) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            // 设置拼音输出格式：无音调、小写字母
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            StringBuilder pinyin = new StringBuilder();
            for (char c : chinese.toCharArray()) {
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    // 对于汉字，使用指定格式获取拼音
                    String pinyinStr = pinyinArray[0]; // 默认取第一个读音
                    // 将拼音字符串的首字母大写，其余小写
                    pinyin.append(Character.toUpperCase(pinyinStr.charAt(0)));
                    pinyin.append(pinyinStr.substring(1).toLowerCase());
                } else {
                    // 如果不是汉字，则直接追加
                    pinyin.append(c);
                }
            }
            String backStr = (pinyin.toString().trim() + generateRandomString(6)).replaceAll("[^a-zA-Z]", "");
            return backStr;// 移除最后一个多余的空格
        } catch (Exception e) {
            return FarmUUID.getUUID32();
        }
    }

    /**
     * 生成随机的字符串
     *
     * @param length
     * @return
     */
    public static String generateRandomString(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
    /**
     * 将一个 List 按指定大小分割为多个子 List
     *
     * @param list 待分割的 List
     * @param size 每个子 List 的最大大小
     * @return 分割后的 List 集合
     */
    public static List<List<String>> splitList(List<String> list, int size) {
        List<List<String>> result = new ArrayList<>();
        if (list == null || list.isEmpty() || size <= 0) {
            return result; // 返回空结果
        }
        int totalSize = list.size();
        for (int i = 0; i < totalSize; i += size) {
            // 使用 subList 获取子列表
            result.add(list.subList(i, Math.min(i + size, totalSize)));
        }
        return result;
    }


    /**按长度截取字符串
     * @param input
     * @param length
     * @return
     */
    public static List<String> splitStringByLength(String input, int length) {
        List<String> result = new ArrayList<>();
        if (input == null || input.isEmpty()) {
            return result; // 返回空列表如果输入字符串为空或null
        }
        for (int i = 0; i < input.length(); i += length) {
            // 计算结束索引，避免超出原字符串长度
            int end = Math.min(i + length, input.length());
            // 获取子串并添加到结果列表
            String substring = input.substring(i, end);
            result.add(substring);
        }
        return result;
    }

    /**
     * 按照第一个匹配到的flag字符分隔字符串input
     *
     * @param input
     * @param flag
     * @return
     */
    public static String[] splitStringByFirstChart(String input, String flag) {
        input = input.replace("：", ":");
        if (input == null || !input.contains(flag)) {
            return new String[]{input}; // 如果没有冒号，则返回原字符串
        }
        int index = input.indexOf(flag); // 找到第一个冒号的位置
        // 使用 substring 方法分别获取两部分
        String part1 = input.substring(0, index);
        String part2 = input.substring(index + 1);
        return new String[]{part1, part2};
    }

    /**
     * 从url中解析附件id
     *
     * @param url
     * @return
     */
    public static String getFileIdByUrl(String url) {
        // 定义正则表达式，匹配最后一个斜杠后的部分
        ////http://127.0.0.1:8080/farm2/api/files/download/661a5f482d9e466eadb20d8fb647a075
        if (url.indexOf("api/files/download/") >= 0) {
            String regex = ".*/([^/]+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);  // 返回匹配到的第一个捕获组
            } else {
                return null;
            }
        }
        // 定义正则表达式，匹配最后一个斜杠后的部分
        ////http://127.0.0.1:8080/farm2/api/files/media/661a5f482d9e466eadb20d8fb647a075
        if (url.indexOf("api/files/media/") >= 0) {
            String regex = ".*/([^/]+)$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);  // 返回匹配到的第一个捕获组
            } else {
                return null;
            }
        }
        return null;
    }

    /**
     * 删除TAG-后的空格
     *
     * @param word
     * @return
     */
    public static String formatSearchTag(String word) {
        if (StringUtils.isBlank(word)) {
            return word;
        }
        // 使用正则表达式替换 TAG- 后的空格
        String result = word.replaceAll("(?<=TAG-)\\s+", "");
        return result;
    }

    /**
     * 连接字符串
     *
     * @param key
     * @return
     */
    public static String linkString(String... key) {
        if (key == null || key.length == 0) {
            return ""; // 如果没有传入任何参数，返回空字符串
        }

        // 使用 StringBuilder 进行高效拼接
        StringBuilder result = new StringBuilder();
        for (String str : key) {
            if (str != null) { // 忽略 null 值
                result.append(str);
            }
        }
        return result.toString();
    }

    public static String formatStringForView(String skc) {
        return String.join("-", FarmStringUtils.splitStringByLength(skc, 2));
    }

    /**
     * 超过长度就截取
     *
     * @param text
     * @param i
     * @return
     */
    public static String subString(String text, int i) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        if (text.length() < i) {
            return text;
        } else {
            return text.substring(0, i - 3) + "...";
        }

    }

    public static boolean isInt(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 按逗号分隔
     *
     * @param keys
     * @return
     */
    public static List<String> splitString(String keys) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(keys)) {
            keys = keys.replace("，", ",");
            list.addAll(Arrays.stream(keys.split(",")).toList());
        }
        return list;
    }

    public static String deleteThinkTag(String xml) {
        if (StringUtils.isBlank(xml)) {
            return xml;
        }
        String cleanedHtml = xml.replaceAll("<think>.*?</think>", "");
        return cleanedHtml;
    }

    /**
     * 验证字符串是否符合skc的id格式
     *
     * @param id
     * @return
     */
    public static boolean isSkcId(String id) {
        if (StringUtils.isBlank(id)) {
            return false;
        }
        if (id.trim().length() != 32) {
            return false;
        }
        return true;
    }

    /**
     * 为空时给一个默认值
     *
     * @param stringval
     * @param defaultval
     * @return
     */
    public static String defaultString(String stringval, String defaultval) {
        if (StringUtils.isBlank(stringval)) {
            return defaultval;
        }
        return stringval;
    }
}
