package org.farm2.salary.domain;

import lombok.Data;

import java.math.BigDecimal;

/**用户薪酬定义 
 * @author cbtg自动生成  2026-1-8 14:45:42 
 */
@Data
public class SalaryUserBase {
    private String id;
    private String name;
    private String keycode;
    private BigDecimal val;
    private String showmodel;
    private String userkey;
    private String username;
    private String salarytime;
    private String userover;
}