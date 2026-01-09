package org.farm2.tools.files.domain;

/**
 * 文件数量
 */
public class FilesNum {
    private int num = 0;

    public int add(int i) {
        num = num + i;
        return num;
    }

    public int getNum() {
        return num;
    }
}
