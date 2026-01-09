package org.farm2.tools.base;

/**
 * 存储一个布尔状态
 */
public class FarmStateBoolean {
    private boolean state = false;

    public FarmStateBoolean(boolean state) {
        // System.out.println("初始完成状态："+state);
        this.state = state;
    }

    public boolean isState() {
        //System.out.println("是否完成："+state);
        return state;
    }

    public void setState(boolean state) {
        //System.out.println("设置完成状态："+state);
        this.state = state;
    }
}
