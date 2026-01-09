package org.farm2.base.db;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

/**
 * 条件查询时构造Select后的title
 */
public class DataSelectFields {

    private List<DataSelectField> titles = null;
    private List<String> ignoreTitles = new ArrayList<>();

    /**
     * 排除某個字段
     *
     * @param fieldTitle
     */
    public DataSelectFields ignore(String fieldTitle) {
        ignoreTitles.add(fieldTitle.toUpperCase());
        return this;
    }


    public DataSelectFields(Class<?> beanClass, String tableAlias) {
        List<DataSelectField> fields = new ArrayList<>();
        // 获取类的所有字段，包括私有字段
        Field[] declaredFields = beanClass.getDeclaredFields();
        for (Field field : declaredFields) {
            // 创建 DataSelectField 实例
            DataSelectField dataSelectField = new DataSelectField();
            dataSelectField.setTitle(field.getName()); // 设置 title 为字段名
            dataSelectField.setAsName(tableAlias);         // 设置 asName
            // 添加到列表
            fields.add(dataSelectField);
        }
        titles = fields;
    }

    public String getTitles() {
        List<String> titleSqls = new ArrayList<>();
        for (DataSelectField field : titles) {
            if (!ignoreTitles.contains(field.getTitle().toUpperCase())) {
                titleSqls.add(field.toSql());
            }
        }
        return String.join(", ", titleSqls);
    }
}
