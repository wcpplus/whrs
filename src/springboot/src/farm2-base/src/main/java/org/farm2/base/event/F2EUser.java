package org.farm2.base.event;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 定义一个人员信息
 */
@Data
@NoArgsConstructor
public class F2EUser {
    private String userKey;
    private String ip;

    public static F2EUser getAnonymous() {
        F2EUser user = new F2EUser();
        user.userKey = "0:ANONYMOUS";
        user.ip = "NONE";
        return user;
    }

    public boolean isLogin() {
        if (this.userKey == null) {
            return false;
        }
        return !this.userKey.equals("0:ANONYMOUS");
    }
}
