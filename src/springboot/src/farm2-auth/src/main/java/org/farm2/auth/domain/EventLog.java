package org.farm2.auth.domain;

import lombok.Data;

/**事件日志 
 * @author cbtg自动生成  2025-1-8 22:28:50 
 */
@Data
public class EventLog {
    private String id;
    private String ctime;
    private String actiontype;
    private String objtype;
    private String objid;
    private String userip;
    private String userkey;
    private String ouserkey;
    private String note;
}