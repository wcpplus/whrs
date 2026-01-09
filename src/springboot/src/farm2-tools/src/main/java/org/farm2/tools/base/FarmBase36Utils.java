package org.farm2.tools.base;

import org.farm2.tools.bean.FarmBeanUtils;

public class FarmBase36Utils {

    // 定义36进制的字符集：0-9 + a-z
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 将10进制数字转换为36进制字符串
     *
     * @param decimalNumber 要转换的10进制数字
     * @return 转换后的36进制字符串
     */
    public static String decimalToBase36(long decimalNumber) {
        if (decimalNumber == 0) {
            return "0"; // 特殊情况处理，0的36进制还是0
        }

        StringBuilder result = new StringBuilder();
        boolean isNegative = decimalNumber < 0;
        decimalNumber = Math.abs(decimalNumber);

        while (decimalNumber > 0) {
            int remainder = (int) (decimalNumber % 36);
            result.insert(0, CHARACTERS.charAt(remainder)); // 从字符集中获取对应的字符
            decimalNumber /= 36;
        }

        return isNegative ? "-" + result.toString() : result.toString();
    }

    /**
     * 将36进制字符串转换为10进制数字
     *
     * @param base36String 要转换的36进制字符串
     * @return 转换后的10进制数字
     */
    public static long base36ToDecimal(String base36String) {
        if (base36String == null || base36String.isEmpty()) {
            throw new IllegalArgumentException("输入的36进制字符串不能为空");
        }

        base36String = base36String.toLowerCase(); // 统一转换为小写
        boolean isNegative = base36String.startsWith("-");
        if (isNegative) {
            base36String = base36String.substring(1); // 去掉负号
        }

        long result = 0;
        for (int i = 0; i < base36String.length(); i++) {
            char c = base36String.charAt(i);
            int digitValue = CHARACTERS.indexOf(c); // 找到字符在字符集中的索引值
            if (digitValue == -1) {
                throw new IllegalArgumentException("无效的36进制字符: " + c);
            }
            result = result * 36 + digitValue;
        }

        return isNegative ? -result : result;
    }

//    public static void main(String[] args) {
//        {
//            String mkey = FarmStringUtils.formatStringForView(FarmMd5Utils.generateMD5(MaStringUtils.getDStringNumber() + MaStringUtils.getMString(), 8, "SKC"));
//            FarmBeanUtils.isSetClassField(FarmStringUtils.linkString("org.farm2.tools.files.domain.File", "s", "Type"), FarmStringUtils.linkString("m", "type"), FarmStringUtils.formatStringForView(FarmMd5Utils.generateMD5(MaStringUtils.getDStringNumber() + MaStringUtils.getMString(), 8, "SKC")));
//            System.out.println("写入机器码：" + mkey);
//        }
//        {
//            System.out.println("读取机器码：" + getMkey());
//        }
//        {
//            String lkey = (getKey(110, 101230));
//            System.out.println("生成licence：" + lkey);
//            FarmBeanUtils.isSetClassField(FarmStringUtils.//
//                            linkString("org.farm2.tools.files.domain.File", "s", "Type"), FarmStringUtils.linkString("l", "type")//
//                    , lkey);
//            System.out.println("写入licence：" + lkey);
//        }
//        {
//            int[] nums = parseKey(FarmBeanUtils.getClassField(FarmStringUtils.linkString("org.farm2.tools.files.domain.File", "s", "Type"), FarmStringUtils.linkString("l", "type")));
//            System.out.println("读取授权码：" + nums[0] + "," + nums[1]);
//        }
//    }

    public static String getKey(int usernum, int knowNum, int taskNum, int courseNum, int wtscardNum) {
        return getKey(usernum, knowNum, taskNum, courseNum, wtscardNum, null);
    }

    public static String getKey(int userNum, int knowNum, int taskNum, int courseNum, int wtscardNum, String hardkey) {
        StringBuffer key = new StringBuffer();
        key.append(decimalToBase36(userNum));//人数
        key.append("-");
        key.append(decimalToBase36(knowNum));//知识数
        key.append("-");
        key.append(decimalToBase36(taskNum));//任务数
        key.append("-");
        key.append(decimalToBase36(courseNum));//课程数
        key.append("-");
        key.append(decimalToBase36(wtscardNum));//答题卡数
        key.append("-" + FarmMd5Utils.generateMD5(key.toString(), 6, hardkey == null ? getMkey() : hardkey.trim().toLowerCase()));
        return key.toString();
    }

    public static String getMkey() {
        return FarmBeanUtils.getClassField(FarmStringUtils.linkString("org.farm2.tools.files.domain.File", "s", "Type"), FarmStringUtils.linkString("m", "type")).toLowerCase();
    }

    public static boolean isKey(String key) {
        key = key.trim();
        String[] keys = key.split("-");
        if (keys.length != 6) {
            return false;
        }
        String userkey = keys[0];//人
        String knowkey = keys[1];//知识
        String taskkey = keys[2];//任务
        String coursekey = keys[3];//课程
        String cardkey = keys[4];//答题卡
        String mkey = keys[5];
        //--------------------------
        int uNum = (int) base36ToDecimal(keys[0]);
        int kNum = (int) base36ToDecimal(keys[1]);
        String newMd5Key = FarmMd5Utils.generateMD5(userkey + "-" + knowkey + "-" + taskkey + "-" + coursekey + "-" + cardkey, 6, getMkey());
        //-------------------------
        if (newMd5Key.equals(mkey)) {
            int[] back = {uNum, kNum};
            return true;
        }
        return false;
    }

    public static int[] parseKey(String key) {
        int[] defaultNums = {
                15, //人数
                2000, //知识数
                20, //任务数
                5, //课程数
                15//答题卡数
        };
        String[] keys = key.split("-");
        if (keys.length != 6) {
            return defaultNums;
        }
        String userkey = keys[0];//人
        String knowkey = keys[1];//知识
        String taskkey = keys[2];//任务
        String coursekey = keys[3];//课程
        String cardkey = keys[4];//答题卡
        String mkey = keys[5];
        //--------------------------
        int userNum = (int) base36ToDecimal(keys[0]);
        int knowNum = (int) base36ToDecimal(keys[1]);
        int taskNum = (int) base36ToDecimal(keys[2]);
        int courseNum = (int) base36ToDecimal(keys[3]);
        int cardNum = (int) base36ToDecimal(keys[4]);
        String newMd5Key = FarmMd5Utils.generateMD5(userkey + "-" + knowkey + "-" + taskkey + "-" + coursekey + "-" + cardkey, 6, getMkey());
        //-------------------------
        if (newMd5Key.equals(mkey)) {
            int[] back = {userNum, knowNum, taskNum, courseNum, cardNum};
            return back;
        }
        return defaultNums;
    }
}
