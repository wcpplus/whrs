package org.farm2.tools.test;

import java.util.Date;

/**
 * 处理运行时间的工具类
 */
public class FarmRunTimes {
    public static boolean isPrint = true;

    /**是否打印测试时间
     * @param isPrint
     */
    public static void setIsPrint(boolean isPrint) {
        FarmRunTimes.isPrint = isPrint;
    }

    public static FarmRunTimes getInstance(String note) {
        if (isPrint)
            System.out.println(note + ":" + "开始计时------《");
        return new FarmRunTimes();
    }

    private Date startTime = new Date();

    /**
     * 开始计时
     */
    public void start(String note) {
        if (isPrint)
            System.out.println(note + ":" + "开始计时------《");
        this.startTime = new Date();
    }

    /**
     * 打印运行时间
     */
    public long printlnRunTime() {
        long time = (new Date().getTime() - this.startTime.getTime());
        if (isPrint)
            System.out.println(time + "sm-------》");
        return time;
    }
}
