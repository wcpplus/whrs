package org.farm2.luser.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.farm2.auth.dao.PostDao;
import org.farm2.auth.dao.UserPostDao;
import org.farm2.auth.domain.Post;
import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.db.FarmDbFields;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.event.F2EAction;
import org.farm2.base.event.F2EObject;
import org.farm2.base.event.F2Event;
import org.farm2.base.event.enums.F2EActionT;
import org.farm2.base.event.enums.F2EObjectT;
import org.farm2.base.event.face.Farm2Events;
import org.farm2.base.exception.FarmAppException;
import org.farm2.base.exception.FarmExceptionUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.base.validation.Farm2ValidUtils;
import org.farm2.base.validation.ValidType;
import org.farm2.exread.ExcelReader;
import org.farm2.exread.ReaderHandle;
import org.farm2.exread.domain.service.ExcelReaderImpl;
import org.farm2.exread.domain.service.ReaderConfig;
import org.farm2.files.domain.ResourceFile;
import org.farm2.files.service.ResourceFileServiceInter;
import org.farm2.luser.dao.LocalOrgDao;
import org.farm2.luser.dao.LocalUserDao;
import org.farm2.luser.dao.LocalUserInfoDao;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.domain.LocalUser;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.luser.utils.FarmUserStateEnum;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.DataResult;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.DBRuleList;
import org.farm2.tools.db.commons.WhereInRule;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.time.FarmTimeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LocalUserServiceImpl implements LocalUserServiceInter {

    /**
     * 是否在无授权时自动添加sysadmin为当前用户
     */
    @Value("${farm2.conf.disdel.userids}")
    private String disableDeleteUserIds;
    @Autowired
    private UserPostDao userPostDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private LocalUserDao localUserDao;
    @Autowired
    FarmParameter farmParameter;
    @Autowired
    private ResourceFileServiceInter resourceFileServiceImpl;

    @Autowired
    private PostServiceInter postServiceImpl;

    @Autowired
    private LocalUserInfoDao localUserInfoDao;
    @Autowired
    private LocalOrgDao localOrgDao;

    @Transactional
    @Override
    public LocalUser insertLocalUserEntity(LocalUser user) {
        FarmDbFields.initInsertBean(user, FarmUserContextLoader.getCurrentUser());
        FarmBeanUtils.runFunctionByBlank(user.getType(), "1", user::setType);
        FarmBeanUtils.runFunctionByBlank(user.getPhotoid(), "11", user::setPhotoid);
        if (localUserDao.isExist(user.getId(), "LOGINNAME", user.getLoginname())) {
            throw new FarmAppException(I18n.msg("登录名已经存在：?", user.getLoginname()));
        }
        localUserDao.insert(encodePassword(user));
        Farm2Events.emit(F2EObjectT.USER, user.getId(), F2EActionT.ADD);
        return user;
    }

    @Transactional
    @Override
    public LocalUser editLocalUserEntity(LocalUser user) {
        LocalUser saveUser = getLocalUserById(user.getId());
        FarmExceptionUtils.throwNullEx(saveUser, I18n.msg("用户不存在:?", user.getId()));
        saveUser.setName(user.getName());
        saveUser.setNote(user.getNote());
        saveUser.setOrgid(user.getOrgid());
        saveUser.setType(user.getType());
        saveUser.setPhotoid(user.getPhotoid());
        saveUser.setGradeid(user.getGradeid());
        if (user.getState().equals(FarmUserStateEnum.PENDING.getType())) {
            saveUser.setState(user.getState());
        }
        saveUser.setLoginname(user.getLoginname());
        if (localUserDao.isExist(user.getId(), "LOGINNAME", user.getLoginname())) {
            throw new FarmAppException(I18n.msg("登录名已经存在：?", user.getLoginname()));
        }
        FarmDbFields.initUpdateBean(saveUser, FarmUserContextLoader.getCurrentUser());
        localUserDao.update(saveUser);
        Farm2Events.emit(F2EObjectT.USER, user.getId(), F2EActionT.UPDATE);
        return saveUser;
    }

    @Transactional
    @Override
    public LocalUser getLocalUserById(String id) {
        return localUserDao.findById(id);
    }

    @Override
    public List<LocalUser> getLocalUsers(DataQuery query) {
        return localUserDao.queryData(query.setCount(false)).getData(LocalUser.class);
    }


    /**
     * 加密登录密码(用户无密码时更新默认密码)
     *
     * @param user
     * @return
     */
    private LocalUser encodePassword(LocalUser user) {
        FarmBeanUtils.runFunctionByBlank(user.getPassword(), farmParameter.getStringParameter("farm2.config.password.default"), user::setPassword);
        FarmPasswordEncoder encoder = new FarmPasswordEncoder();
        user.setPassword(encoder.encodeDbPassword(user.getPassword()));
        user.setEncode(encoder.getEncodeModelName());
        return user;
    }

    /**
     * 更新用户密码
     *
     * @param user
     * @return
     */
    private LocalUser encodePassword(LocalUser user, String rawPassword) {
        FarmPasswordEncoder encoder = new FarmPasswordEncoder();
        user.setPassword(encoder.encodeDbPassword(rawPassword));
        user.setEncode(encoder.getEncodeModelName());
        return user;
    }

    @Transactional
    @Override
    public DataResult searchLocalUser(DataQuery query) {
        DataResult result = localUserDao.queryData(query);
        return result;
    }

    @Override
    public int getLocalUserNum(DataQuery query) {
        return localUserDao.countData(query);
    }

    @Override
    public LocalUser getLocalUserByLoginName(String loginname) {
        return localUserDao.findByLoginname(loginname);
    }

    @Transactional
    @Override
    public void reSetPassword(String userid, String rawPassword) {
        LocalUser saveUser = getLocalUserById(userid);
        FarmExceptionUtils.throwNullEx(saveUser, I18n.msg("用户不存在:?", userid));
        encodePassword(saveUser, rawPassword);
        FarmDbFields.initUpdateBean(saveUser, FarmUserContextLoader.getCurrentUser());
        localUserDao.update(saveUser);
        Farm2Events.emit(F2EObjectT.USER, saveUser.getLoginname(), F2EActionT.UPDATE_PASSWORD);
    }

    @Transactional
    @Override
    public void resetPasswordByDbPassword(String loginname, String dbPassword) {
        LocalUser user = getLocalUserByLoginName(loginname);
        FarmExceptionUtils.throwNullEx(user, I18n.msg("用户不存在:?", loginname));
        FarmDbFields.initUpdateBean(user, FarmUserContextLoader.getCurrentUser());
        user.setPassword(dbPassword);
        localUserDao.update(user);
        Farm2Events.emit(F2EObjectT.USER, user.getLoginname(), F2EActionT.UPDATE_PASSWORD, FarmUserContextLoader.getCurrentUser());
    }

    @Override
    @Transactional
    public void importUser(String fileid) {
        ResourceFile rfile = resourceFileServiceImpl.getResourceFileById(fileid, false);
        // 校验数据有效性
        if (!rfile.getExname().toUpperCase().equals("XLS")) {
            throw new RuntimeException("模板格式不正确。请重新下载。");
        }
        if (rfile.getFilesize() >= 1024 * 1024 * 10) {
            throw new RuntimeException("文件不能大于10M");
        }
        List<LocalUser> users = new ArrayList<>();
        try (InputStream stream = new FileInputStream(resourceFileServiceImpl.getFile(rfile))) {
            ReaderConfig config = ReaderConfig.newInstance(0, 2, 0)
                    .addColumn(0, "name", ReaderConfig.ColumnType.STRING)
                    .addColumn(1, "loginname", ReaderConfig.ColumnType.STRING)
                    .addColumn(2, "org", ReaderConfig.ColumnType.STRING)
                    .addColumn(3, "role", ReaderConfig.ColumnType.STRING)
                    .addColumn(2, "note", ReaderConfig.ColumnType.STRING);
            ExcelReader reader = ExcelReaderImpl.getInstance(config, stream);
            //校验数据
            reader.read(new ReaderHandle() {
                @Override
                public void handle(Map<String, Object> node) {
                    LocalUser user = new LocalUser();
                    try {
                        user.setName((String) node.get("name"));
                        user.setLoginname((String) node.get("loginname"));
                        user.setNote((String) node.get("note"));
                        Farm2ValidUtils.valid((String) node.get("name"), false, 2, 16, true);
                        Farm2ValidUtils.valid((String) node.get("loginname"), ValidType.loginName, false, true);
                        Farm2ValidUtils.valid((String) node.get("note"), true, 0, 256, true);
                        Farm2ValidUtils.valid((String) node.get("org"), false, 0, 32, true);
                        LocalOrg org = localOrgDao.findById((String) node.get("org"));
                        if (org == null) {
                            List<LocalOrg> orgs = localOrgDao.findByName((String) node.get("org"));
                            if (orgs.size() > 0) {
                                org = orgs.get(0);
                            }
                        }
                        if (org == null) {
                            throw new RuntimeException(I18n.msg("组织机构未发现"));
                        }
                        user.setOrgid(org.getId());
                        if (node.get("role") != null) {
                            String rolekeys = (String) node.get("role");
                            String roleids = "";
                            rolekeys = rolekeys.replace("，", ",");
                            for (String rolekey : rolekeys.split(",")) {
                                //解析多个岗位
                                if (StringUtils.isNotBlank(rolekey.trim())) {
                                    Post post = null;
                                    {
                                        List<Post> posts = postDao.findByKeyid(rolekey);
                                        if (posts.size() > 0) {
                                            post = posts.get(0);
                                        }
                                    }
                                    if (post == null) {
                                        List<Post> posts = postDao.findByName(rolekey);
                                        if (posts.size() > 0) {
                                            post = posts.get(0);
                                        }
                                    }
                                    if (post == null) {
                                        throw new RuntimeException(I18n.msg("角色?未发现", rolekey));
                                    }
                                    roleids = roleids + "," + post.getId();
                                }
                            }
                            //通过euserkey暂存岗位
                            user.setEuserkey(roleids);
                        }

                    } catch (Exception e) {
                        throw new RuntimeException("<" + user.getName() + ">行:" + e.getMessage());
                    }
                    users.add(user);
                }
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (LocalUser user : users) {
            if (StringUtils.isNotBlank(user.getEuserkey())) {
                //通过euserkey暂存岗位
                List<String> posts = new ArrayList<>();
                for (String postid : user.getEuserkey().split(",")) {
                    if (StringUtils.isNotBlank(postid.trim())) {
                        posts.add(postid.trim());
                    }
                }
                postServiceImpl.savePostUser(user.getLoginname(), posts);
                user.setEuserkey(null);
            }
            insertLocalUserEntity(user);
        }
    }

    @Override
    @Transactional
    public void updateLoginTime(String loginname) {
        LocalUser user = localUserDao.findByLoginname(loginname);
        user.setLogintime(FarmTimeTool.getTimeDate14());
        localUserDao.update(user);
    }

    @Override
    public long getUserAllNum() {
        return localUserDao.countData(new DataQuery().addRule(new DBRule("state", "1", "=")));
    }

    @Override
    public List<LocalOrg> getLocalOrgs(String loginname) {
        LocalUser user = getLocalUserByLoginName(loginname);
        LocalOrg org = localOrgDao.findById(user.getOrgid());
        if (org == null) {
            return List.of();
        }
        List<LocalOrg> orgs = localOrgDao.find(DBRuleList.getInstance()
                .add(new WhereInRule("ID", FarmStringUtils.splitStringByLength(org.getTreecode(), 32).stream().collect(Collectors.toSet())))
                .toList());
        orgs.sort((n1, n2) -> n1.getTreecode().length() - n2.getTreecode().length());
        return orgs;
    }

    @Transactional
    @Override
    public void updateState(String loginname, String stateType) {
        LocalUser user = localUserDao.findByLoginname(loginname);
        user.setState(stateType);
        localUserDao.update(user);
    }

    @Transactional
    @Override
    public void delLocalUser(String id) {
        LocalUser user = localUserDao.findById(id);
        if (StringUtils.isNotBlank(disableDeleteUserIds)) {
            String[] stringArray = disableDeleteUserIds.split(",");
            if (Arrays.asList(stringArray).contains(id)) {
                log.warn(I18n.msg("用户id为?的用户被禁止删除！", id));
                return;
            }
        }
        localUserInfoDao.delete(DBRule.getInstance("USERKEY", user.getLoginname(), "=").getRules());
        userPostDao.delete(DBRule.getInstance("USERKEY", user.getLoginname(), "=").getRules());
        localUserDao.deleteById(id);
    }
}
