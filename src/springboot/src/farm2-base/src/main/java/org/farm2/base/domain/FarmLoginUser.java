package org.farm2.base.domain;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * 用于权限验证 security框架需要UserDetails 的实现类
 */

public class FarmLoginUser implements UserDetails {
    private String username;
    private String loginname;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        if (StringUtils.isNotBlank(loginname)) {
            return loginname;
        } else {
            return username;
        }
    }

    public FarmLoginUser(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
