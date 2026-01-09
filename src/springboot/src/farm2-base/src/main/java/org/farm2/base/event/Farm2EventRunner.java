package org.farm2.base.event;

import lombok.extern.slf4j.Slf4j;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.base.event.inter.Farm2EventHandle;
import org.farm2.tools.i18n.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class Farm2EventRunner {

    // 使用命名线程工厂，便于排查问题
    private static final ThreadFactory threadFactory = r -> {
        Thread t = new Thread(r, "event-handler-pool-thread");
        t.setDaemon(true);
        return t;
    };

    private static final ExecutorService HANDLER_POOL = Executors.newFixedThreadPool(4, threadFactory);

    // 控制事件处理器线程是否正在运行
    private static final AtomicBoolean isRunning = new AtomicBoolean(false);

    /**
     * 启动事件处理器。
     * 如果已在运行，则直接返回。
     * 使用独立线程异步处理事件。
     */
    public static void runHandles() {
        if (!isRunning.compareAndSet(false, true)) {
            log.debug("Farm2EventRunner 已在运行，跳过重复启动");
            return;
        }

        new Thread(() -> {
            try {
                log.info("Farm2EventRunner 启动，开始处理事件...");

                // 持续处理事件，直到没有更多事件（或根据业务需求调整）
                boolean hasMoreEvents;
                do {
                    List<F2Event> events;
                    try {
                        events = Farm2Events.getEvent(10);
                    } catch (Exception e) {
                        log.error(I18n.msg("获取事件时发生异常"), e);
                        break; // 避免无限循环报错
                    }

                    hasMoreEvents = !events.isEmpty();

                    if (hasMoreEvents) {
                        processEventsConcurrently(events);
                    }

                } while (hasMoreEvents);

                log.info("Farm2EventRunner 处理完成，退出。");

            } catch (Throwable t) {
                log.error(I18n.msg("Farm2EventRunner 执行过程中发生未预期错误"), t);
            } finally {
                isRunning.set(false);
            }
        }, "event-runner-thread").start();
    }

    /**
     * 并发处理一批事件：对每个事件，其所有处理器并行执行，并设置统一超时
     */
    private static void processEventsConcurrently(List<F2Event> events) {
        for (F2Event event : events) {
            List<Farm2EventHandle> handles = Farm2Events.getHandles();
            if (handles.isEmpty()) {
                log.debug("事件 {} 无处理器注册，跳过", event);
                continue;
            }

            // 提交所有处理器任务
            List<Future<?>> futures = new ArrayList<>();
            for (Farm2EventHandle handle : handles) {
                Future<?> future = HANDLER_POOL.submit(() -> {
                    try {
                        handle.handle(event);
                    } catch (Exception e) {
                        log.error(I18n.msg("处理器 {} 处理事件 {} 失败", handle.getClass().getSimpleName(), event), e);
                    }
                });
                futures.add(future);
            }

            // 统一等待所有处理器完成（带超时）
            try {
                for (Future<?> future : futures) {
                    future.get(60, TimeUnit.SECONDS);
                }
            } catch (TimeoutException e) {
                String eventType = event.getObj().getType().name();
                String actionType = event.getAction().getType().name();
                log.warn(I18n.msg("事件 {}:{} 处理超时，当前处理器数量: {}", eventType, actionType, handles.size()));
                // 取消未完成任务
                futures.forEach(f -> f.cancel(true));
            } catch (InterruptedException e) {
                log.warn(I18n.msg("事件 {} 处理被中断"), event, e);
                Thread.currentThread().interrupt();
                break;
            } catch (ExecutionException e) {
                log.error(I18n.msg("事件 {} 处理过程中发生异常"), event, e.getCause());
            }
        }
    }

    /**
     * 获取当前运行状态（可用于监控或调试）
     */
    public static boolean isRunning() {
        return isRunning.get();
    }

    /**
     * 关闭处理器线程池（建议在应用关闭时调用）
     */
    public static void shutdown() {
        HANDLER_POOL.shutdown();
        try {
            if (!HANDLER_POOL.awaitTermination(5, TimeUnit.SECONDS)) {
                HANDLER_POOL.shutdownNow();
            }
        } catch (InterruptedException e) {
            HANDLER_POOL.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}