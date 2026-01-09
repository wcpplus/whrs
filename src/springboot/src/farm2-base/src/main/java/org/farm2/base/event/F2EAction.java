package org.farm2.base.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.tools.time.FarmTimeTool;

import static org.farm2.base.domain.FarmUserContextLoader.getCurrentUser;

/**
 * 定义事件行为
 */
@Data
@NoArgsConstructor
public class F2EAction {
    private F2EActionT type;
    private F2EUser operator;
    private String time;
    private Integer[] vals;

    /**定义事件行为
     * @param farm2EventActionType
     */
    public F2EAction(F2EActionT farm2EventActionType, Integer... vals) {
        FarmUserContext user = FarmUserContextLoader.getCurrentUser();
        this.vals = vals;
        this.init(farm2EventActionType, user);
    }


    public F2EAction(F2EActionT farm2EventActionType, FarmUserContext user, Integer... vals) {
        this.vals = vals;
        this.init(farm2EventActionType, user);
    }


    private void init(F2EActionT farm2EventActionType, FarmUserContext user) {
        if (user != null) {
            this.operator = new F2EUser();
            this.operator.setUserKey(user.getLoginname());
            this.operator.setIp(user.getIp());
        } else {
            this.operator = F2EUser.getAnonymous();
        }
        this.type = farm2EventActionType;
        this.time = FarmTimeTool.getTimeDate14();
    }

}
