package org.farm2.base.event;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 定义一个事件
 *
 *
 * 事件触发： Farm2Events.emit(F2EObjectT.FILE, fileid, F2EActionT.SUBMIT_FILE, FarmUserContextLoader.getCurrentUser());
 *
 *
 */
@Data
public class F2Event {
    F2EObject obj;
    F2EAction action;
    String note;

    public F2Event(F2EObject obj, F2EAction action, String note) {
        this.obj = obj;
        this.action = action;
        if (StringUtils.isNotBlank(note)) {
            if (note.length() > 256) {
                this.note = note.substring(0, 255);
            } else {
                this.note = note;
            }
        }
    }

    public String toString() {
        return obj.getType().getTitle() + "_" + action.getType().getTitle();
    }

    public String getObjUserKey() {
        if (getObj() != null && getObj().getOperator() != null) {
            return getObj().getOperator().getUserKey();
        } else {
            return null;
        }
    }

    public String getActionUserKey() {
        if (getAction() != null && getAction().getOperator() != null) {
            return getAction().getOperator().getUserKey();
        } else {
            return null;
        }
    }
}
