package org.farm2.main.cecurity.service.domain;

import lombok.Data;

import java.util.Date;

@Data
public class OnlineUser {
    private String ip;
    private String name;
    private String loginName;
    private String loginId;
    private long startTime;
    private long endTime;
    private long visitNum;

    public OnlineUser(String ip, String name, String loginname, String loginId) {
        this.ip = ip;
        this.name = name;
        this.loginName = loginname;
        this.loginId = loginId;
        this.startTime = (new Date()).getTime();
        this.endTime = (new Date()).getTime();
    }
}
