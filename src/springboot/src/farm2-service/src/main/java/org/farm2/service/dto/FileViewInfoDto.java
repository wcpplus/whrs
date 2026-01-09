package org.farm2.service.dto;

import lombok.Data;
import org.farm2.wdap.domain.WdapExtendFile;

@Data
public class FileViewInfoDto {
    private String title;
    private long size;
    private String state;
    private String stateTitle;
    private WdapExtendFile extendFile;
}
