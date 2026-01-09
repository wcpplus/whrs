package org.farm2.service.inter;

import jakarta.validation.constraints.NotBlank;
import org.farm2.auth.dto.MenusDto;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserMin;

import java.util.List;
import java.util.Set;

public interface FarmUserServiceInter {
    public FarmUserContext getUserByLoginName(String userKey);

    public FarmUserContext getUserByLoginName(String userKey, Boolean isCache);

    public String getUserNameByLoginName(String userKey);

    public String getUserPhotoIdByLoginName(String userKey);

    public List<MenusDto> getUserMenus(FarmUserContext user, String domain);

    public long getUserAllNum();

    public void editUser(String name, String photoid);

    public Set<String> getUserkeysByPost(String roleKey);

    public FarmUserMin getMinUserByLoginName(String cuserkey);

    public List<FarmUserMin> getUsersByLoginName(String word);
}
