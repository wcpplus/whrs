package org.farm2.wdap.convertor.utils;

public class DocumentFormatUtils {

    /**
     * 获得openoffice支持的扩展名
     *
     * @param exname
     * @return
     */
    public static String getExname(String exname) {
        if (exname.toLowerCase().indexOf("ppt") >= 0) {
            return "ppt";
        }
        if (exname.toLowerCase().indexOf("doc") >= 0) {
            return "doc";
        }
        if (exname.toLowerCase().indexOf("xls") >= 0) {
            return "xls";
        }
        if (exname.toLowerCase().indexOf("wps") >= 0) {
            return "doc";
        }
        if (exname.toLowerCase().indexOf("dps") >= 0) {
            return "ppt";
        }
        if (exname.toLowerCase().indexOf("et") >= 0) {
            return "xls";
        }
        return exname;
    }
}
