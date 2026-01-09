package org.farm2.main.cecurity.service;

import org.farm2.auth.face.FarmParameter;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义权限校验方法，作用在controller注解上
 *
 * 预置权限：SYS.login:必须登录
 */
@Component("farmAction")
public class FarmActionCheck {
    public boolean has(String... keys) {
        FarmUserContext userContext = FarmUserContextLoader.getCurrentUser();
        if (Arrays.stream(keys).toList().contains("SYS.login")) {
            if (!FarmUserContextLoader.isLogin()) {
                throw new RuntimeException("登录状态异常,请立即登录系统");
            }
            if (keys.length == 1) {
                //仅包含SYS.login的时候，直接通过
                return true;
            }
        }
        if (userContext != null && userContext.getType().equals("3")) {
            //管理员有权限
            return true;
        }
        List<String> userkeys = null;
        if (userContext == null) {
            userkeys = new ArrayList<>();
        } else {
            userkeys = userContext.getActions();
        }
        boolean isNeedLogin = FarmParameter.getInstance().getBooleanParameter("farm2.config.permission.login.need");
        //检索权限所有用户都有
        if (!isNeedLogin || (isNeedLogin && userContext != null)) {
            userkeys.addAll(List.of("search*", "wknow*", "wtag*", "resourcefile*", "wchat*", "wtype*"));
        }
        for (String key : keys) {
            for (String actionKey : userkeys) {
                // 将通配符模式转换为正则表达式
                String regex = actionKey.toUpperCase().replace("*", ".*");
                if (key.trim().toUpperCase().matches(regex)) {
                    return true;
                }
            }
        }
        return false;
    }
}
