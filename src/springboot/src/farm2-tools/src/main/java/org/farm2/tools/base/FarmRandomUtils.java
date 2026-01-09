package org.farm2.tools.base;

import java.util.Random;

public class FarmRandomUtils {
    public static String generateRandomId(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // 0~9之间的随机数字
        }
        return sb.toString();
    }
}
