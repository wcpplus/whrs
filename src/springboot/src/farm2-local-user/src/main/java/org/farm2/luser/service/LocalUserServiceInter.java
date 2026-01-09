package org.farm2.luser.service;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.domain.LocalUser;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;

import java.util.List;

public interface LocalUserServiceInter {

    public LocalUser insertLocalUserEntity(LocalUser user);

    public LocalUser editLocalUserEntity(LocalUser user);

    public void delLocalUser(String id);

    public LocalUser getLocalUserById(String id);

    public List<LocalUser> getLocalUsers(DataQuery query);

    public DataResult searchLocalUser(DataQuery query);

    public int getLocalUserNum(DataQuery query);

    public LocalUser getLocalUserByLoginName(String loginname);

    public void reSetPassword(String userid, String rawPassword);

    /**
     * 通过dbpassword更新用户的密码
     *
     * @param loginname
     * @param newSysPassword
     */
    public void resetPasswordByDbPassword(String loginname, String newSysPassword);

    /**
     * 导入用户通过excel
     *
     * @param fileid
     */
    public void importUser(String fileid);

    /**
     * 更新登录时间
     *
     * @param loginname
     */
    public void updateLoginTime(String loginname);

    public long getUserAllNum();

    public List<LocalOrg> getLocalOrgs(String loginname);

    public void updateState(String loginname, String stateType);
}
