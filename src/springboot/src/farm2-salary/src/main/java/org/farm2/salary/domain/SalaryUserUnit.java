package org.farm2.salary.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户薪资
 *
 * @author cbtg自动生成  2026-1-8 14:47:36
 */
@Data
public class SalaryUserUnit {
    private String id;
    private String salarytime;
    private String userkey;
    private String username;
    private String ctime;
    private String note;
    private String state;
    private BigDecimal grosspay;
    private BigDecimal netpay;
    private BigDecimal taxamount;
}