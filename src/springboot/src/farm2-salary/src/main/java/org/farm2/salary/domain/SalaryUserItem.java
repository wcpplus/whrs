package org.farm2.salary.domain;

import lombok.Data;

import java.math.BigDecimal;

/**用户薪酬明细 
 * @author cbtg自动生成  2026-1-8 14:48:48 
 */
@Data
public class SalaryUserItem {
    private String id;
    private String name;
    private String keycode;
    private String ctime;
    private BigDecimal val;
    private String showmodel;
    private String salarytime;
    private String userkey;
    private String username;
}