package org.farm2.base.event.face;

import lombok.Data;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.event.F2EAction;
import org.farm2.base.event.F2EObject;
import org.farm2.base.event.F2Event;
import org.farm2.base.event.Farm2EventRunner;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.structure.ResourceInfo;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 定义一个事件
 */
@Data
public class Farm2Events {
    private static LinkedBlockingQueue<F2Event> queue = new LinkedBlockingQueue<>();
    private static int queueSize = 10000;
    private static List<Farm2EventHandle> handles = new CopyOnWriteArrayList<>();


    public static List<Farm2EventHandle> getHandles() {
        return handles;
    }

    /**
     * 可用表示任何字符串
     */
    public static String ANY = "any";


    /**
     * 在main绑定事件处理实现类
     * 从队列中抓取元素
     *
     * @param num
     * @return
     */
    synchronized
    public static List<F2Event> getEvent(int num) {
        List<F2Event> events = new ArrayList<>();
        for (int n = 0; n < num; n++) {
            F2Event event = queue.poll();
            if (event != null) {
                events.add(event);
            } else {
                break;
            }
        }
        return events;
    }

    /**
     * 触发事件
     *
     * @param f2EObjectT    事件对象类型
     * @param eventObjectId 事件对象ID
     * @param f2EActionT    事件动作类型
     */
    public static void emit(F2EObjectT f2EObjectT, String eventObjectId, F2EActionT f2EActionT, Integer... vals) {
        emit(new F2Event(new F2EObject(f2EObjectT, eventObjectId), new F2EAction(f2EActionT, vals), null));
    }


    public static void emit(F2EObjectT f2EObjectT, String eventObjectId, F2EActionT f2EActionT, String note, Integer... vals) {
        emit(new F2Event(new F2EObject(f2EObjectT, eventObjectId), new F2EAction(f2EActionT, vals), note));
    }

    public static void emit(F2EObject f2EObject, F2EAction f2EAction, String note) {
        emit(new F2Event(f2EObject, f2EAction, note));
    }

    public static void emit(F2EObject f2EObject, F2EAction f2EAction) {
        emit(new F2Event(f2EObject, f2EAction, null));
    }

    public static void emitError(String note) {
        Farm2Events.emit(F2EObjectT.ERROR, F2EObjectT.ERROR.name(), F2EActionT.ERROR, F2EObjectT.ERROR.getTitle() + ":" + note);
    }


    public static void emit(F2EObjectT f2EObjectT, String eventObjectId, F2EActionT f2EActionT, FarmUserContext user) {
        emit(new F2Event(new F2EObject(f2EObjectT, eventObjectId), new F2EAction(f2EActionT, user), null));
    }

    /**
     * 触发事件（添加一个事件进入队列）
     *
     * @param event
     */
    synchronized
    public static void emit(F2Event event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    doEmit(event);
                }
            });
        } else {
            doEmit(event);
        }
    }


    /**
     * 触发事件（添加一个事件进入队列）
     *
     * @param event
     */
    public static void doEmit(F2Event event) {
        if (queue.size() > queueSize) {
            Farm2EventRunner.runHandles();
            try {
                throw new RuntimeException(I18n.msg("事件队列阻塞，当前未处理事件为：?", queue.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            queue.offer(event);
            Farm2EventRunner.runHandles();
        }
    }

    public static ResourceInfo getResourceInfo() {
        return new ResourceInfo("业务事件", queueSize, queue.size());
    }
}
