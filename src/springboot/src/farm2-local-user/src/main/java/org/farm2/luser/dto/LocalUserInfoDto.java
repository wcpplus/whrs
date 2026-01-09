package org.farm2.luser.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farm2.files.domain.ResourceFile;

/**
 * 用户信息
 *
 * @author cbtg自动生成  2026-1-5 21:07:40
 */
@Data
@NoArgsConstructor
public class LocalUserInfoDto {
    //@RegExValidator(type = ValidType.loginName)//自定义校验方法

    private String id;

    private String sex;

    private String birthdate;

    private String phonecode;

    private String emptime;

    private String email;

    private String idcode;

    private String citycode;

    private String pan;

    private String contractfid;

    private String eqfid;

    private String degfid;

    private String merfid;

    private String icardfid;

    private Integer process;

    private String userkey;


    private ResourceFile contractfFile;
    private ResourceFile eqfFile;
    private ResourceFile degfFile;
    private ResourceFile merfFile;
    private ResourceFile icardfFile;
}