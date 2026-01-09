package org.farm2.luser.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.domain.AuthGrade;
import org.farm2.auth.domain.Post;
import org.farm2.auth.dto.PostDto;
import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.service.AuthGradeServiceInter;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.domain.FarmPost;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.files.utils.FileDownloadUtils;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.domain.LocalUser;
import org.farm2.luser.domain.LocalUserInfo;
import org.farm2.luser.dto.LocalUserDto;
import org.farm2.luser.dto.ResetPasswordDto;
import org.farm2.luser.service.LocalOrgServiceInter;
import org.farm2.luser.service.LocalUserInfoServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.luser.utils.FarmUserStateEnum;
import org.farm2.report.FarmReport;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataQueryDto;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBSort;
import org.farm2.tools.db.commons.SqlRule;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.farm2.tools.web.dto.IdDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/localuser")
public class LocalUserController {
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private LocalOrgServiceInter localOrgServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private FarmParameter farmParameter;
    @Autowired
    private FileDownloadUtils fileDownloadUtils;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;
    @Autowired
    private FarmReport farmReport;
    @Autowired
    private AuthGradeServiceInter authGradeServiceImpl;
    @Autowired
    private LocalUserInfoServiceInter localUserInfoServiceImpl;


    @PostMapping("/counts")
    public FarmResponseResult queryAll() {
        LocalDate firstDay = LocalDate.now().withDayOfMonth(1);
        LocalTime midnight = LocalTime.MIDNIGHT;
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String day1_14 = firstDay.atTime(midnight).format(formatter1);
//        LocalDate lastDay = LocalDate.now()
//                .with(TemporalAdjusters.lastDayOfMonth());
//        LocalTime endOfDay = LocalTime.of(23, 59, 0); // 23:59:00
//        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//        String day31_14 = lastDay.atTime(endOfDay).format(formatter2);
        Integer allNum = localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                .addRule(new WhereInRule("STATE", Set.of(FarmUserStateEnum.PROBATION.getType(), FarmUserStateEnum.ACTIVE.getType())))
        );
        Integer syqNum = localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                .addRule(new WhereInRule("STATE", Set.of(FarmUserStateEnum.PROBATION.getType())))
        );
        Integer rzNum = localUserInfoServiceImpl.getNum(DataQuery.getInstance()
                .addRule(new DBRule("EMPTIME", day1_14, ">"))
        );
        Map<String, Integer> nums = new HashMap<>();
        nums.put("s_all_num", allNum);
        nums.put("s_rz_t_m_num", rzNum);
        nums.put("s_syq_num", syqNum);
        nums.put("s_rz_num", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                .addRule(new WhereInRule("STATE", Set.of(FarmUserStateEnum.PENDING.getType())))
        ));
        nums.put("s_sy_num", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                .addRule(new WhereInRule("STATE", Set.of(FarmUserStateEnum.PROBATION.getType())))
        ));
        nums.put("s_zs_num", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                .addRule(new WhereInRule("STATE", Set.of(FarmUserStateEnum.ACTIVE.getType())))
        ));
        nums.put("s_li_num", localUserServiceImpl.getLocalUserNum(DataQuery.getInstance()
                .addRule(new WhereInRule("STATE", Set.of(FarmUserStateEnum.LEFT.getType())))
        ));
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, nums);
    }


    @PreAuthorize("@farmAction.has('localUser.report')")
    @PostMapping("/report")
    public void downloadFile(@RequestBody DataQueryDto query, HttpServletRequest request, HttpServletResponse response) throws Exception {
        query.setPageSize(1000);
        DataResult result = (DataResult) queryAll(query).getData();
        result.runFormatTime("LOGINTIME");
        result.runDictionary("1:系统用户,3:管理员", "TYPE");
        result.runDictionary("1:可用,0:禁用", "STATE");
        File reportFile = farmReport.addParas("result", result.getData()).generateFile("userList.xlsx");
        fileDownloadUtils.downloadFile(request, response, reportFile, reportFile.length(), reportFile.getName(), "application/octet-stream");
    }


    @PreAuthorize("@farmAction.has('localUser.down.tempate')")
    @PostMapping("/downtemplate")
    public void downloadTemplate(@RequestBody DataQueryDto query, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File templateFile = farmParameter.getClassPathFile(Paths.get("report", "userImpTemplet.xls"));
        fileDownloadUtils.downloadFile(request, response, templateFile, templateFile.length(), templateFile.getName(), "application/octet-stream");
    }


    /**
     * 导入报表
     *
     * @param fileid
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.import')")
    @PostMapping("/import")
    public FarmResponseResult importData(@RequestBody IdDto fileid) throws InterruptedException {
        localUserServiceImpl.importUser(fileid.getId());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 条件查询
     *
     * @return
     */
    @PostMapping("/myinfo")
    public FarmResponseResult myinfo() {
        Map<String, Object> node = new HashMap<>();
        LocalUser user = localUserServiceImpl.getLocalUserByLoginName(FarmUserContextLoader.getCurrentUserKey());
        if (StringUtils.isNotBlank(user.getOrgid())) {
            LocalOrg org = localOrgServiceImpl.getLocalOrgById(user.getOrgid());
            if (org != null) {
                org.setNote(localOrgServiceImpl.getOrgPath(org.getId()).stream().map(LocalOrg::getName).collect(Collectors.joining("/")));
                node.put("LocalOrg", org);
            }
        }
        LocalUserInfo userinfo = localUserInfoServiceImpl.getLocalUserInfoByUserKey(user.getLoginname());
        if (userinfo != null) {
            node.put("Userinfo", userinfo);
        }
        user.setState(FarmUserStateEnum.valueOfType(user.getState()).getTitle());
        node.put("LocalUser", user);
        if (StringUtils.isNotBlank(userinfo.getContractfid())) {
            node.put("ContractfFile", resourceFileServiceImpl.getResourceFileById(userinfo.getContractfid(), true));
        }
        if (StringUtils.isNotBlank(userinfo.getEqfid())) {
            node.put("EqfFile", resourceFileServiceImpl.getResourceFileById(userinfo.getEqfid(), true));
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, node);
    }

    /**
     * 条件查询
     *
     * @param query
     * @return
     */
    @PostMapping("/query")
    public FarmResponseResult queryAll(@RequestBody DataQueryDto query) {
        DataQuery dataQuery = DataQuery.getInstance(query);
        String postid = (String) dataQuery.getRuleValue("POSTID");
        if (StringUtils.isNotBlank(postid)) {
            dataQuery.removeRule("POSTID");
            dataQuery.addRule(new WhereInRule("LOGINNAME", postServiceImpl.getUserkeysByPostId(postid)));
        }
        if ("NONE".equals(dataQuery.getRuleValue("ORGID")) || dataQuery.getRuleValue("ORGID") == null) {
            dataQuery.removeRule("ORGID");
        } else {
            LocalOrg org = localOrgServiceImpl.getLocalOrgById((String) dataQuery.getRuleValue("ORGID"));
            dataQuery.removeRule("ORGID");
            dataQuery.addRule(new SqlRule(" and (TREECODE like ?)", org.getTreecode() + "%"));
        }
        dataQuery.addDefaultSort(new DBSort("LOGINNAME", DBSort.SORT_TYPE.DESC));
        dataQuery.setRuleAsSql("default", " and (A.NAME like ?  or LOGINNAME like ?)", FarmDbLikeModel.ALL);
        DataResult result = localUserServiceImpl.searchLocalUser(dataQuery);
        result.runDataHandle(node -> {
            String orgId = (String) node.get("ORGID");
            String loginname = (String) node.get("LOGINNAME");
            String state = (String) node.get("STATE");
            node.put("STATE_ALT", FarmUserStateEnum.valueOfType(state).getTitle());
            if (StringUtils.isNotBlank(orgId)) {
                List<LocalOrg> orgs = localOrgServiceImpl.getOrgPath(orgId);
                if (!orgs.isEmpty()) {
                    node.put("ORG_ALT", orgs.stream().map(LocalOrg::getName).collect(Collectors.joining("/")));
                }
            }
            List<FarmPost> posts = postServiceImpl.getUserPosts(loginname, true);
            if (!posts.isEmpty()) {
                node.put("POST_ALT", posts.stream().map(FarmPost::getName).collect(Collectors.joining("|")));
            }

            String gradeId = (String) node.get("GRADEID");
            if (StringUtils.isNotBlank(gradeId)) {
                AuthGrade grade = authGradeServiceImpl.getAuthGradeById(gradeId);
                if (grade != null) {
                    node.put("GRADEID_ALT", grade.getName() + grade.getKeyid());
                }
            }
            LocalUserInfo userInfo = localUserInfoServiceImpl.getLocalUserInfoByUserKey(loginname);
            if (userInfo != null) {
                node.put("PROCESS", userInfo.getProcess());
            } else {
                node.put("PROCESS", 0);
            }


        });
        //result.runFormatTime("CTIME", "yyyy-MM-dd HH:mm");
        //刷新权限相关缓存
        {
            FarmCaches.getInstance().clearCache(FarmCacheKeys.PERMISSION_USER_KNOW_POPS);
            FarmCaches.getInstance().clearCache(FarmCacheKeys.USER_POSTS);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, result);
    }

    /**
     * 添加数据
     *
     * @param localUserDto
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.add')")
    @PostMapping
    public FarmResponseResult addSubmit(@Valid @RequestBody LocalUserDto localUserDto) {
        LocalUser user = localUserServiceImpl.insertLocalUserEntity(FarmBeanUtils.copyProperties(localUserDto, new LocalUser()));
        postServiceImpl.savePostUser(user.getLoginname(), localUserDto.getPost());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, user.getId());
    }

    /**
     * 修改数据
     *
     * @param id
     * @param localUserDto
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.edit')")
    @PutMapping("/{id}")
    public FarmResponseResult editSubmit(@PathVariable String id, @Valid @RequestBody LocalUserDto localUserDto) {
        localUserDto.setId(id);
        localUserServiceImpl.editLocalUserEntity(FarmBeanUtils.copyProperties(localUserDto, new LocalUser()));
        LocalUser user = localUserServiceImpl.getLocalUserById(id);
        postServiceImpl.savePostUser(user.getLoginname(), localUserDto.getPost());
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.del')")
    @DeleteMapping("/{id}")
    public FarmResponseResult delSubmit(@PathVariable String id) {
        localUserServiceImpl.delLocalUser(id);
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS, id);
    }


    /**
     * 重置用户密码（默认密码）
     *
     * @param ids
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.resetpassword')")
    @PostMapping("/defaultpassword")
    public FarmResponseResult defaultpassword(@RequestBody List<String> ids) {
        for (String id : ids) {
            localUserServiceImpl.reSetPassword(id, farmParameter.getStringParameter("farm2.config.password.default"));
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }


    /**
     * 重置用户密码（指定密码）
     *
     * @param data
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.resetpassword')")
    @PostMapping("/resetpassword")
    public FarmResponseResult resetPassword(@RequestBody ResetPasswordDto data) {
        FarmPasswordEncoder passwordEncoder = new FarmPasswordEncoder();
        LocalUser localUser = localUserServiceImpl.getLocalUserByLoginName(data.getLoginname());
        if (passwordEncoder.matches(data.getWebPassword(), passwordEncoder.getSaltByCode(data.getCode()), localUser.getPassword())) {
            for (String loginname : data.getUserkeys()) {
                localUserServiceImpl.resetPasswordByDbPassword(loginname, data.getNewSysPassword());
            }
            return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
        } else {
            return FarmResponseResult.getInstance(FarmResponseCode.ERROR, FarmPasswordEncoder.MATCHES_FALSEMSG, null);
        }
    }


    /**
     * 批量删除数据
     *
     * @param ids 要删除的记录ID列表
     * @return 操作结果
     */
    @PreAuthorize("@farmAction.has('localUser.del')")
    @DeleteMapping("/batch")
    public FarmResponseResult batchDelSubmit(@RequestBody List<String> ids) {
        for (String id : ids) {
            localUserServiceImpl.delLocalUser(id);
        }
        return FarmResponseResult.getInstance(FarmResponseCode.SUCESS);
    }

    /**
     * 数据浏览
     *
     * @param id
     * @return
     */
    @PreAuthorize("@farmAction.has('localUser.info')")
    @GetMapping("/{id}")
    public FarmResponseResult info(@PathVariable String id) {
        LocalUser localUser = localUserServiceImpl.getLocalUserById(id);
        LocalUserDto user = FarmBeanUtils.copyProperties(localUser, new LocalUserDto());
        List<String> postKeys = postServiceImpl.getUserPostIds(user.getLoginname());
        List<FarmPost> posts = postServiceImpl.getUserPosts(user.getLoginname(), false);
        user.setPost(postKeys);
        user.setPosts(posts.stream().map(node -> {
            PostDto dto = new PostDto();
            dto.setId(node.getId());
            dto.setName(node.getName());
            dto.setKeyid(node.getKey());
            return dto;
        }).collect(Collectors.toList()));
        return new FarmResponseResult(FarmResponseCode.SUCESS, user);
    }
}
