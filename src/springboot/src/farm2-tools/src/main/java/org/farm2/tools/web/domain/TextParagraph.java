package org.farm2.tools.web.domain;

/**
 * 文章段落-----》用于对文章进行分段索引（用于AI向量索引）
 */
public class TextParagraph {
    private int sort;
    private int size;
    private String text;
    private int textLength;


    public TextParagraph(int sort, int size, String text) {
        this.sort = sort;
        this.text = text;
        this.size = size;
        this.textLength = text.length();
    }

    public int getSize() {
        return size;
    }

    public int getSort() {
        return sort;
    }

    public String getText() {
        return text;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
