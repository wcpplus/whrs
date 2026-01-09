package org.farm2.tools.structure;


/**
 * 用来封装资源使用量
 */
public class ResourceInfo {

    /**
     * 资源总数
     */
    private long all;

    /**
     * 已用
     */
    private long use;

    /**
     * 百分比
     */
    private int percent;


    /**
     * 资源名称
     */
    private String title;

    public ResourceInfo(String title, long total, long used, int percentUsed) {
        this.all = total;
        this.use = used;
        this.percent = percentUsed;
        this.title = title;
    }

    public ResourceInfo(String title, long total) {
        this.all = total;
        this.title = title;
    }

    public ResourceInfo(String title, long total, long used) {
        this.all = total;
        this.use = used;
        double percentUsed = total > 0 ? (double) used / total * 100 : 0;
        this.percent = (int) percentUsed;
        this.title = title;
    }


    public long getAll() {
        return all;
    }

    public void setAll(long all) {
        this.all = all;
    }

    public long getUse() {
        return use;
    }

    public void setUse(long use) {
        this.use = use;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
