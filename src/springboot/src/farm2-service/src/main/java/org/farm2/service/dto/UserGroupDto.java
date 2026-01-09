package org.farm2.service.dto;

import lombok.Data;
import org.farm2.luser.dto.LocalUserDto;
import org.farm2.luser.dto.LocalUserInfoDto;

@Data
public class UserGroupDto {
    private LocalUserDto user;
    private LocalUserInfoDto info;
    private String state;
}
