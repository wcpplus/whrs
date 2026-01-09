package org.farm2.wdap.convertor.utils;

public class OsUtils {
    public static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return true;
        } else {
            return false;
        }
    }
}
