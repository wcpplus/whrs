package org.farm2.luser.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.farm2.auth.dto.PostDto;
import org.farm2.base.validation.RegExValidator;
import org.farm2.base.validation.ValidType;

import java.util.List;

@Data
@NoArgsConstructor
public class LocalUserDto {
    private String id;
    private String ctime;
    private String etime;
    private String cuserkey;
    private String euserkey;
    private String type;
    @NotNull(message = "{farm2.validate.template.notnull}")
    @Size(min = 2, max = 32, message = "{farm2.validate.template.size}")
    @RegExValidator(type = ValidType.loginName)
    private String loginname;
    @NotNull(message = "{farm2.validate.template.notnull}")
    @Size(min = 2, max = 8, message = "{farm2.validate.template.size}")
    private String name;
    private String state;
    @Size(min = 0, max = 256, message = "{farm2.validate.template.size}")
    private String note;
    private String logintime;
    private String orgid;
    private String photoid;
    private List<String> post;
    private List<LocalOrgDto> orgs;
    private List<PostDto> posts;
    private String gradeid;
}