package org.farm2.base.event.inter;

import org.farm2.base.event.F2Event;

/**
 * 事件处理接口
 *  注册事件示例： Farm2Events.emit(F2EObjectT.USER, userContext.getLoginname(), F2EActionT.LOGIN, userContext);
 */
public interface Farm2EventHandle {
    public void handle(F2Event event);
}
