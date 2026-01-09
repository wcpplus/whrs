package org.farm2.salary.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 薪酬项
 *
 * @author cbtg自动生成  2026-1-7 10:48:10
 */
@Data
public class SalaryTemplateItem {
    private String id;
    private String note;
    private String name;
    private BigDecimal defaultval;
    private String keycode;
    private String componenttype;
    private String sourcemodel;
    private String userover;
    private Integer sortcode;
    private String templateid;
    private String showmodel;
}