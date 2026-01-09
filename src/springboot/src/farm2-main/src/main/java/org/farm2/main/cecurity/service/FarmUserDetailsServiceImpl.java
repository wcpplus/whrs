package org.farm2.main.cecurity.service;

import org.farm2.base.domain.FarmLoginUser;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 实现查询用户（security中登录用）
 */
@Service
public class FarmUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private FarmUserServiceInter farmUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FarmUserContext user=    farmUserService.getUserByLoginName(username);
        if (Objects.isNull(user)) {
            throw new RuntimeException(I18n.msg("登录名或密码错误"));
        }
        return new FarmLoginUser(user.getLoginname(), user.getPassword());
    }
}
