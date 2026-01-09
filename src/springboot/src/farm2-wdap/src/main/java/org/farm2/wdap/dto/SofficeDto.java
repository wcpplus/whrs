package org.farm2.wdap.dto;

import lombok.Data;

@Data
public class SofficeDto {

    private String ip;
    private int port;
    private String startCmd;
    private String stopCmd;
    private boolean isLive;
    private String os;
}
