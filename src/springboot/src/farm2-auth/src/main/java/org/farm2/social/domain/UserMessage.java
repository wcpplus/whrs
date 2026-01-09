package org.farm2.social.domain;

import lombok.Data;

/**用户消息 
 * @author cbtg自动生成  2025-4-23 10:29:47 
 */
@Data
public class UserMessage {
    private String id;
    private String ctime;
    private String cuserkey;
    private String state;
    private String note;
    private String readuserkey;
    private String content;
    private String title;
    private String readstate;
}