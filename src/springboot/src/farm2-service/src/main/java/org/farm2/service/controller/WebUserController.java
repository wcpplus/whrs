package org.farm2.service.controller;

import org.farm2.auth.service.PostServiceInter;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.luser.domain.LocalUser;
import org.farm2.luser.domain.LocalUserInfo;
import org.farm2.luser.service.LocalUserInfoServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.luser.utils.FarmUserStateEnum;
import org.farm2.service.dto.FileViewInfoDto;
import org.farm2.service.dto.UserGroupDto;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdDto;
import org.farm2.wdap.domain.WdapExtendFile;
import org.farm2.wdap.domain.WdapTask;
import org.farm2.wdap.service.WdapExtendFileServiceInter;
import org.farm2.wdap.service.WdapTaskServiceInter;
import org.farm2.wdap.utils.FileModelUtils;
import org.farm2.wdap.utils.WdapTaskStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识
 *
 * @author cbtg自动生成  2025-2-4 12:13:51
 */
@RestController
@RequestMapping("/api/wuser")
public class WebUserController {
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private LocalUserInfoServiceInter localUserInfoServiceImpl;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;

    @PostMapping("/nums")
    public FarmResponseResult nums(@RequestBody UserGroupDto user) {
        Map<String, Integer> nums = new HashMap<>();
        nums.put("pendingNum", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance().addRule(new DBRule("STATE", FarmUserStateEnum.PENDING.getType(), "="))));
        nums.put("probationNum", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance().addRule(new DBRule("STATE", FarmUserStateEnum.PROBATION.getType(), "="))));
        nums.put("activeNum", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance().addRule(new DBRule("STATE", FarmUserStateEnum.ACTIVE.getType(), "="))));
        nums.put("leftNum", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance().addRule(new DBRule("STATE", FarmUserStateEnum.LEFT.getType(), "="))));
        nums.put("retiredNum", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance().addRule(new DBRule("STATE", FarmUserStateEnum.RETIRED.getType(), "="))));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, nums);
    }

    /**
     * 加载知识内容
     *
     * @return
     */
    @PostMapping("/submit")
    public FarmResponseResult submit(@RequestBody UserGroupDto user) {
        LocalUser upUser = localUserServiceImpl.getLocalUserById(user.getUser().getId());
        LocalUserInfo upInfo = localUserInfoServiceImpl.getLocalUserInfoById(user.getInfo().getId());
        FarmUserStateEnum state = FarmUserStateEnum.valueOfType(upUser.getState());
        if (user.getState().equals(FarmUserStateEnum.PROBATION.name())) {
            state = FarmUserStateEnum.PROBATION;
            //入职
            upUser.setName(user.getUser().getName());
            upUser.setOrgid(user.getUser().getOrgid());
            postServiceImpl.savePostUser(user.getUser().getLoginname(), user.getUser().getPost());
            upUser.setGradeid(user.getUser().getGradeid());
            //--
            upInfo.setEmptime(user.getInfo().getEmptime());
            upInfo.setEmail(user.getInfo().getEmail());
            upInfo.setIdcode(user.getInfo().getIdcode());
            upInfo.setPan(user.getInfo().getPan());
            upInfo.setContractfid(user.getInfo().getContractfid());
            upInfo.setEqfid(user.getInfo().getEqfid());
            upInfo.setDegfid(user.getInfo().getDegfid());
            upInfo.setMerfid(user.getInfo().getMerfid());
            upInfo.setIcardfid(user.getInfo().getIcardfid());

        }
        if (user.getState().equals(FarmUserStateEnum.ACTIVE.name())) {
            state = FarmUserStateEnum.ACTIVE;
            //转正
            upUser.setName(user.getUser().getName());
            upUser.setOrgid(user.getUser().getOrgid());
            postServiceImpl.savePostUser(user.getUser().getLoginname(), user.getUser().getPost());
            upUser.setGradeid(user.getUser().getGradeid());
        }
        if (user.getState().equals(FarmUserStateEnum.LEFT.name())) {
            state = FarmUserStateEnum.LEFT;
            //转正
            upUser.setName(user.getUser().getName());
            upUser.setOrgid(user.getUser().getOrgid());
        }
        if (user.getState().equals("NONE")) {
            //调动
            upUser.setName(user.getUser().getName());
            upUser.setOrgid(user.getUser().getOrgid());
            postServiceImpl.savePostUser(user.getUser().getLoginname(), user.getUser().getPost());
            upUser.setGradeid(user.getUser().getGradeid());
        }

        localUserServiceImpl.editLocalUserEntity(upUser);
        localUserInfoServiceImpl.editLocalUserInfoEntity(upInfo);
        localUserServiceImpl.updateState(user.getUser().getLoginname(), state.getType());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }
}
