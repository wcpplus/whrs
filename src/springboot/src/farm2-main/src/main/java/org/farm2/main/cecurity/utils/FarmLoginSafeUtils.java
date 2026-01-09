package org.farm2.main.cecurity.utils;

import org.farm2.auth.face.FarmParameter;
import org.farm2.auth.service.ParameterServiceInter;
import org.farm2.base.parameter.FarmParameterInter;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.i18n.I18n;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 安全登录相关
 */
@Component
public class FarmLoginSafeUtils {
    public static final String MATCHES_FALSEMSG = "验证码错误";
    @Autowired
    private FarmParameterInter farmParameter;

    /**
     * 是否需要图形验证码（超过几次后）
     *
     * @param loginname
     * @return
     */
    public boolean isNeedImgCode(String loginname) {
        //超过就校验图形验证码
        int imgcodeNum = farmParameter.getIntParameter("farm2.config.imgcode.limit");
        if (imgcodeNum == 0) {
            return true;
        }
        if (imgcodeNum == -1) {
            return false;
        }
        //登录错误次数
        Integer loginErrorNum = FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.LOGIN_ERROR_NUM);
        if (loginErrorNum == null) {
            loginErrorNum = 0;
        }
        if (loginErrorNum >= imgcodeNum) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 注册一个验证码
     *
     * @param loginname
     * @param imgCodeText
     */
    synchronized
    public void registImgCode(String loginname, String imgCodeText) {
        Set<String> codes = FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.LOGIN_IMG_CODES);
        if (codes == null) {
            codes = new HashSet<>();
        }
        if (imgCodeText != null) {
            codes.add(imgCodeText.toUpperCase());
        }
        FarmCaches.getInstance().putCacheData(loginname, codes, FarmCacheKeys.LOGIN_IMG_CODES);
    }

    /**
     * 记录登录名和密码校验结果（为了出发验证码或锁定登录等行为）
     *
     * @param loginname
     * @param isSuccess 是否登录成功
     */
    synchronized
    public void registValidLoginResult(String loginname, boolean isSuccess) {
        if (!isSuccess) {
            //登录失败
            Integer loginErrorNum = FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.LOGIN_ERROR_NUM);
            if (loginErrorNum == null) {
                loginErrorNum = 1;
            } else {
                loginErrorNum++;
            }
            FarmCaches.getInstance().putCacheData(loginname, loginErrorNum, FarmCacheKeys.LOGIN_ERROR_NUM);
        } else {
            //登录成功
            FarmCaches.getInstance().removeCacheData(loginname, FarmCacheKeys.LOGIN_IMG_CODES);
            FarmCaches.getInstance().putCacheData(loginname, 0, FarmCacheKeys.LOGIN_ERROR_NUM);
        }
    }

    /**
     * 校验用户录入的验证码和系统验证码是否匹配（一般在登录时使用）
     *
     * @param loginname
     * @param imgcode
     * @return
     */
    synchronized
    public boolean validImgCode(String loginname, String imgcode) {
        {//校验是否被锁定账户
            //超过就锁定用户
            int lockNum = farmParameter.getIntParameter("farm2.config.login.lock");
            //登录错误次数
            Integer loginErrorNum = FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.LOGIN_ERROR_NUM);
            if (loginErrorNum == null) {
                loginErrorNum = 0;
            }
            if (loginErrorNum > lockNum) {
                throw new RuntimeException(I18n.msg("该账户被锁定，请10分钟后尝试"));
            }
        }
        {//处理验证码
            if (!isNeedImgCode(loginname)) {
                //不需要验证码，就直接通过
                return true;
            }
            Set<String> codes = FarmCaches.getInstance().getCacheData(loginname, FarmCacheKeys.LOGIN_IMG_CODES);
            if (codes == null) {
                return false;
            }
            if (codes.contains(imgcode.toUpperCase())) {
                FarmCaches.getInstance().removeCacheData(loginname, FarmCacheKeys.LOGIN_IMG_CODES);
                return true;
            } else {
                return false;
            }
        }
    }
}
