package org.farm2.base.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.db.commons.FarmUUID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于權限系統緩存的user對象
 */
@Data
public class FarmUserContext implements Serializable {
    private String loginname;
    private String password;
    private String name;
    private String loginid;
    private String state;
    private String photoid;
    private String type;//1:系统用户3超级用户9接口用户
    private String ip;
    private List<String> actions = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private List<FarmPost> posts = new ArrayList<>();
    //后台菜单数量
    private int frameMenuNum;

    public String initLoginid() {
        loginid = FarmUUID.getUUID32();
        return loginid;
    }

    public boolean isSuperManager() {
        if (StringUtils.isBlank(type)) {
            return false;
        } else {
            if (type.equals("3")) {
                return true;
            }
        }
        return false;
    }
}
