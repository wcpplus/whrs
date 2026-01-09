package org.farm2.base.process;

import lombok.extern.slf4j.Slf4j;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.files.Farm2ProcessState;

/**
 * 负责缓存和展示各种进度
 */
@Slf4j
public class FarmProcessUtils {


    public static void sleep(int i) {
        Farm2ProcessState.sleep(i);
    }

    /**
     * 获得处理进度
     *
     * @param key
     * @return
     */
    public static Farm2ProcessState getProcess(String key, FarmProcessTypeEnum type) {
        return FarmCaches.getInstance().getCacheData(key + type.name(), FarmCacheKeys.PROCESS);
    }

    public static void setProcess(FarmProcessKey key, int percent, String msg) {
        setProcess(key.getKey(), key.getType(), percent, msg);
    }

    public static void setProcess(FarmProcessKey key, int percent) {
        setProcess(key.getKey(), key.getType(), percent, null);
    }

    public static void setProcessEnd(FarmProcessKey key, String msg) {
        setProcess(key.getKey(), key.getType(), 100, msg);
    }
    /**
     * 设置处理进度
     *
     * @param key
     * @param percent
     * @param msg
     */
    public static void setProcess(String key, FarmProcessTypeEnum type, int percent, String msg) {
        log.info("PROCESS:" + percent + ":" + msg);
        FarmCaches.getInstance().putCacheData(key + type.name(), new Farm2ProcessState().setPercent(percent, msg), FarmCacheKeys.PROCESS);
    }


    public static void setProcess(String key, FarmProcessTypeEnum type, String msg) {
        Farm2ProcessState state = getProcess(key, type);
        if (state == null) {
            setProcess(key, type, 1, msg);
        } else {
            int precent = state.getPercent();
            precent = precent + (100 - precent) / 10;
            if (precent >= 100) {
                precent = 99;
            }
            setProcess(key, type, precent, msg);
        }
    }

    public static void setProcessEnd(String key, FarmProcessTypeEnum type, String msg) {
        setProcess(key, type, 100, msg);
    }
}
