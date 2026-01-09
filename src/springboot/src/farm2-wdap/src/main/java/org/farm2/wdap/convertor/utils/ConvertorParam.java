package org.farm2.wdap.convertor.utils;


import lombok.Data;

/**
 * 转换器参数封装类
 *
 * @author Wd
 */
@Data
public class ConvertorParam {
    private String field;
    private String title;
    private String note;
    private String value;
    public ConvertorParam(String field, String title, String note, String val) {
        super();
        this.title = title;
        this.note = note;
        this.field = field;
        this.value = val;
    }
}
