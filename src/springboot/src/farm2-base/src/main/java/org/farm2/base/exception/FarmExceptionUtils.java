package org.farm2.base.exception;

import org.farm2.tools.i18n.I18n;

public class FarmExceptionUtils {
    /**对象为空时抛出异常
     * @param obj
     * @param msg
     */
    public static void throwNullEx(Object obj, String msg) {
        if (obj == null) {
            throw new RuntimeException(msg);
        }
    }
}
