package org.farm2.base.password;

import lombok.Data;

/**
 * 封装密码盐和盐兑换码
 */
@Data
public class FarmSalt {
    private String val;
    private String code;
}
