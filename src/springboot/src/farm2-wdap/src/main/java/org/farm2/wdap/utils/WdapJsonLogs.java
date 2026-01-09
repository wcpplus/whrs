package org.farm2.wdap.utils;

import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.json.FarmJsons;
import org.farm2.tools.time.FarmTimeTool;
import org.farm2.wdap.domain.WdapTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 管理和维护json日志
 */
public class WdapJsonLogs {
    List<String> logs = new ArrayList<>();

    public static WdapJsonLogs getInstance(String logjson) {
        WdapJsonLogs jsonLogs = new WdapJsonLogs();
        if (StringUtils.isBlank(logjson)) {
            return new WdapJsonLogs();
        }
        jsonLogs.logs = (ArrayList<String>) FarmJsons.toBean(logjson, (new ArrayList<String>()).getClass());
        return jsonLogs;
    }

    public static WdapTask addLog(WdapTask task, LogType logType, String msg) {
        task.setLogjson(WdapJsonLogs.getInstance(task.getLogjson()).add(logType, msg).toJson());
        return task;
    }

    public enum LogType {
        INFO, WARN, ERROR
    }

    public static WdapJsonLogs getInstance() {
        return new WdapJsonLogs();
    }

    public WdapJsonLogs add(LogType type, String msg) {
        logs.add(type.name() + ":" + FarmTimeTool.getTimeDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "/" + msg);
        return this;
    }

    public String toJson() {
        return FarmJsons.toFormatJson(logs);
    }
}
