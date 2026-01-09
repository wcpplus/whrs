package org.farm2.luser.domain;

import lombok.Data;

@Data
public class LocalUser {
    private String id;
    private String ctime;
    private String etime;
    private String cuserkey;
    private String euserkey;
    private String password;
    private String encode;
    private String type;
    private String loginname;
    private String name;
    private String state;
    private String orgid;
    private String logintime;
    private String photoid;
    private String gradeid;
    /**
     * 可空
     */
    private String note;
}