package org.farm2.base.event;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.event.enums.F2EObjectT;

/**
 * 定义事件对象
 */
@Data
public class F2EObject {
    private F2EObjectT type;
    private String id;
    private F2EUser operator;

    public F2EObject(F2EObjectT type, String id) {
        this.type = type;
        this.id = id;
    }
    public F2EObject(F2EObjectT type, String id, FarmUserContext user) {
        this.type = type;
        this.id = id;
        if (user != null) {
            this.operator = new F2EUser();
            this.operator.setUserKey(user.getLoginname());
            this.operator.setIp(user.getIp());
        }
    }

    public F2EObject(F2EObjectT type, String id, String userkey) {
        this.type = type;
        this.id = id;
        if (StringUtils.isNotBlank(userkey)) {
            this.operator = new F2EUser();
            this.operator.setUserKey(userkey);
        }
    }
}
