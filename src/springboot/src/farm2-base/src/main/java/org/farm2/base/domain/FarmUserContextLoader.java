package org.farm2.base.domain;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
import org.farm2.tools.db.commons.FarmUUID;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理线程中的用户上下文信息
 */
public class FarmUserContextLoader {
    /**将用户信息存储security上下文中
     * @param userDetail
     */
    public static void put(FarmUserContext userDetail) {
        FarmUserContext user = SerializationUtils.clone(userDetail);
        if (user == null) {
            user = new FarmUserContext();
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String action : user.getActions()) {
            if (StringUtils.isBlank(action)) {
                continue;
            }
            authorities.add(new SimpleGrantedAuthority(action));
        }
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(userToken);
    }

    /**从security上下文中获取用户信息
     * @return
     */
    public static FarmUserContext getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null || !(principal instanceof FarmUserContext)) {
                return null;
            }
            FarmUserContext user = (FarmUserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return user;
        } else {
            return new FarmUserContext();
        }
    }

    public static String getCurrentUserKey() {
        FarmUserContext user=   getCurrentUser();
        if(user==null){
            return null;
        }else{
            return user.getLoginname();
        }
    }

    public static boolean isLogin() {
        return getCurrentUser() != null;
    }

    public static boolean isAdmin() {
        FarmUserContext user = getCurrentUser();

        return user != null && user.getType().equals("3");
    }

    public static String getAnonymousUserKey() {
        return "ANONYMOUS" + FarmUUID.getUUID32().substring(16, 32);
    }
}
