package org.farm2.tools.files;

/**
 * 封装处理进度
 */
public class Farm2ProcessState {
    private int percent;
    private String msg;

    public static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPercent() {
        return percent;
    }

    public String getMsg() {
        return msg;
    }

    public void setPercent(int percent) {
        //System.out.println(percent);
        this.percent = percent;
    }

    public Farm2ProcessState setPercent(int percent, String msg) {
        //System.out.println(percent);
        this.percent = percent;
        this.msg = msg;
        return this;
    }

    public boolean isEnd() {
        return percent >= 100;
    }
}
