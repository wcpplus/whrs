package org.farm2.tools.base;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FarmMd5Utils {
    /**
     * 生成给定字符串的MD5哈希值。
     *
     * @param input 需要进行MD5加密的字符串
     * @return 字符串的MD5哈希值
     */
    public static String generateMD5(String input) {
        try {
            // 获取MessageDigest实例，并指定使用MD5算法
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 将输入字符串转换为字节数组，并更新MessageDigest
            md.update(input.getBytes());

            // 执行哈希计算，返回哈希值的字节数组
            byte[] bytes = md.digest();

            // 将字节数组转换成16进制字符串
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 取md5的后几位
     *
     * @param input
     * @param lastNum
     * @return
     */
    public static String generateMD5(String input, int lastNum, String lcc) {
        if (lcc != null) {
            input = input + lcc;
        }
        String key = generateMD5(input);
        return key.substring(key.length() - lastNum, key.length());
    }

}
