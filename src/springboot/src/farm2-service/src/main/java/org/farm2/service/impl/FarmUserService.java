package org.farm2.service.impl;

import org.farm2.auth.domain.Post;
import org.farm2.auth.dto.MenusDto;
import org.farm2.auth.service.ActionsServiceInter;
import org.farm2.auth.service.PostServiceInter;
import org.farm2.base.domain.FarmPost;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.domain.FarmUserMin;
import org.farm2.luser.domain.LocalOrg;
import org.farm2.luser.domain.LocalUser;
import org.farm2.luser.service.LocalOrgServiceInter;
import org.farm2.luser.service.LocalUserServiceInter;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.tools.base.FarmStringUtils;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.DataQuery;
import org.farm2.tools.db.commons.DBRule;
import org.farm2.tools.db.commons.FarmSqls;
import org.farm2.tools.db.commons.SqlRule;
import org.farm2.tools.db.enums.FarmDbLikeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 用戶信息读取的实现类，将来可以替换为第三方实现
 */
@Component
public class FarmUserService implements FarmUserServiceInter {
    @Autowired
    private LocalUserServiceInter localUserServiceImpl;
    @Autowired
    private LocalOrgServiceInter localOrgServiceImpl;
    @Autowired
    private PostServiceInter postServiceImpl;
    @Autowired
    private ActionsServiceInter actionsServiceImpl;

    @Override
    public FarmUserContext getUserByLoginName(String loginname, Boolean isCache) {
        if (isCache) {
            return FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.CONTEXT_USER, key -> {
                return getUserByLoginName(key);
            });
        } else {
            return getUserByLoginName(loginname);
        }
    }

    @Override
    public FarmUserContext getUserByLoginName(String loginname) {
        List<String> postIds = postServiceImpl.getUserPostIds(loginname);
        LocalUser localuser = localUserServiceImpl.getLocalUserByLoginName(loginname);
        if (localuser == null) {
            return null;
        }
        //加载用户权限
        List<String> actionKeys = actionsServiceImpl.getActionsKeysByUserKey(loginname);
        FarmUserContext userContext = new FarmUserContext();
        userContext.setLoginname(localuser.getLoginname());
        userContext.setState(localuser.getState());
        userContext.setType(localuser.getType());
        userContext.setPhotoid(localuser.getPhotoid());
        userContext.setName(localuser.getName());
        userContext.setPassword(localuser.getPassword());
        userContext.setActions(actionKeys);
        LocalOrg org = localOrgServiceImpl.getLocalOrgById(localuser.getOrgid());
        if (org != null) {
            userContext.getTags().add(org.getName());
        }
        for (String id : postIds) {
            Post post = postServiceImpl.getPostById(id);
            if (post != null) {
                userContext.getPosts().add(new FarmPost(post.getKeyid(), post.getName(), post.getId()));
            }
        }
        return userContext;
    }


    @Override
    public String getUserNameByLoginName(String userKey) {
        if (FarmCaches.getInstance().isCacheData(userKey, FarmCacheKeys.USER_NAME)) {
            return FarmCaches.getInstance().getCacheData(userKey, FarmCacheKeys.USER_NAME);
        }
        String username = null;
        LocalUser user = localUserServiceImpl.getLocalUserByLoginName(userKey);
        if (user != null) {
            username = user.getName();
        }
        FarmCaches.getInstance().putCacheData(userKey, username, FarmCacheKeys.USER_NAME);
        return username;
    }

    @Override
    public String getUserPhotoIdByLoginName(String userKey) {
        String photoId = null;
        LocalUser user = localUserServiceImpl.getLocalUserByLoginName(userKey);
        if (user != null) {
            photoId = user.getPhotoid();
        }
        return photoId;
    }

    @Override
    public List<MenusDto> getUserMenus(FarmUserContext user, String domain) {
        FarmCacheKeys cacheEnum = null;
        if (domain.toLowerCase().equals("frame".toLowerCase())) {
            cacheEnum = FarmCacheKeys.USER_MENUS_FRAME;
        }
        if (domain.toLowerCase().equals("app".toLowerCase())) {
            cacheEnum = FarmCacheKeys.USER_MENUS_APP;
        }
        if (cacheEnum == null) {
            throw new RuntimeException("菜单域无效：" + domain);
        }
        if (FarmCaches.getInstance().isCacheData(user.getLoginname(), cacheEnum)) {
            return FarmCaches.getInstance().getCacheData(user.getLoginname(), cacheEnum);
        } else {
            List<MenusDto> menus = null;
            if (user.getType().equals("3")) {
                menus = actionsServiceImpl.getAllMenus(domain);
            } else {
                menus = actionsServiceImpl.getMenusByUserKey(user.getLoginname(), domain);
            }
            FarmCaches.getInstance().putCacheData(user.getLoginname(), menus, cacheEnum);
            return menus;
        }
    }

    @Override
    public long getUserAllNum() {
        return localUserServiceImpl.getUserAllNum();
    }

    @Override
    public void editUser(String name, String photoid) {
        LocalUser user = localUserServiceImpl.getLocalUserByLoginName(FarmUserContextLoader.getCurrentUserKey());
        user.setName(name);
        user.setPhotoid(photoid);
        localUserServiceImpl.editLocalUserEntity(user);
    }

    @Override
    public Set<String> getUserkeysByPost(String roleKey) {
        return postServiceImpl.getUserkeysByPostKey(roleKey);
    }

    @Override
    public FarmUserMin getMinUserByLoginName(String cuserkey) {
        FarmUserMin minUser = new FarmUserMin();
        LocalUser localuser = localUserServiceImpl.getLocalUserByLoginName(cuserkey);
        if (localuser == null) {
            return null;
        }
        minUser.setLoginname(localuser.getLoginname());
        minUser.setState(localuser.getState());
        minUser.setType(localuser.getType());
        minUser.setName(localuser.getName());
        minUser.setPhotoid(localuser.getPhotoid());
        return minUser;
    }

    @Override
    public List<FarmUserMin> getUsersByLoginName(String word) {
        List<LocalUser> users = localUserServiceImpl
                .getLocalUsers(DataQuery.getInstance().setPageSize(10)
                        .addRule(new SqlRule(" and (A.NAME like ? OR A.LOGINNAME like ?)", FarmSqls.getValueLike(word, FarmDbLikeModel.ALL), FarmSqls.getValueLike(word, FarmDbLikeModel.ALL)))
                );
        return users.stream().map(node -> new FarmUserMin(node.getLoginname(), node.getName())).toList();
    }
}
