package org.farm2.main.cecurity.controller;

import com.wf.captcha.SpecCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.farm2.auth.dto.MenusDto;
import org.farm2.auth.service.ActionsServiceInter;
import org.farm2.base.domain.FarmUserContext;
import org.farm2.base.domain.FarmUserContextLoader;
import org.farm2.base.jwt.FarmJwtClaims;
import org.farm2.base.jwt.FarmJwtConfig;
import org.farm2.base.jwt.FarmJwtUtils;
import org.farm2.base.password.FarmPasswordEncoder;
import org.farm2.luser.utils.FarmUserStateEnum;
import org.farm2.main.cecurity.dto.TokensDto;
import org.farm2.main.cecurity.utils.FarmCookieJwtUtils;
import org.farm2.main.cecurity.utils.FarmLoginSafeUtils;
import org.farm2.base.web.IpUtils;
import org.farm2.main.cecurity.dto.LoginUserDto;
import org.farm2.main.cecurity.service.FarmUserOnlines;
import org.farm2.service.inter.FarmUserServiceInter;
import org.farm2.tools.bean.FarmBeanUtils;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;
import org.farm2.tools.i18n.I18n;
import org.farm2.tools.structure.FarmMaps;
import org.farm2.tools.web.FarmResponseCode;
import org.farm2.tools.web.FarmResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户登录
 */
@RestController
@Tag(name = "用户登录相关", description = "登录相关操作的API")
@RequestMapping("/api/login")
public class LoginController {
    @Autowired
    private FarmUserServiceInter farmUserService;
    @Autowired
    private ActionsServiceInter actionsServiceImpl;
    @Autowired
    private FarmLoginSafeUtils farmLoginSafeUtils;
    private static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    private static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    /**
     * jwt存储模式：
     */
    public static String jwtModel = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.auth.jwt.model");

    @Operation(summary = "用戶预登录", description = "获取用户登录所必须的盐")
    @PostMapping(value = "/info")
    @ResponseBody
    public FarmResponseResult info(@RequestBody LoginUserDto user, HttpServletRequest request) {
        try {
            FarmUserContext dbuser = farmUserService.getUserByLoginName(user.getLoginname());
            if (dbuser == null) {
                FarmPasswordEncoder passwordEncoder = new FarmPasswordEncoder();
                //返回两个盐，数据库盐和前端随机盐（用户不存在时的伪装数据）
                Map<String, Object> map = new HashMap<>();
                map.put("webcode", passwordEncoder.getSalt());//前端盐
                map.put("syscode", passwordEncoder.getSalt(user.getLoginname()).getVal());//数据库盐
                map.put("imgcode", (boolean) farmLoginSafeUtils.isNeedImgCode(user.getLoginname()));//图形验证码，如果有的话
                return new FarmResponseResult<Map>(FarmResponseCode.SUCESS, map);
            } else {
                FarmPasswordEncoder passwordEncoder = new FarmPasswordEncoder();
                //返回两个盐，数据库盐和前端随机盐
                Map<String, Object> map = new HashMap<>();
                map.put("webcode", passwordEncoder.getSalt());//前端盐
                map.put("syscode", passwordEncoder.getDbSalt(dbuser.getPassword()));//数据库盐
                map.put("imgcode", (boolean) farmLoginSafeUtils.isNeedImgCode(user.getLoginname()));//图形验证码，如果有的话
                return new FarmResponseResult<Map>(FarmResponseCode.SUCESS, map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(FarmPasswordEncoder.MATCHES_FALSEMSG);
        }
    }


    @Operation(summary = "用戶登录", description = "通过登录名和密码登录")
    @PostMapping(value = "/auth")
    @ResponseBody
    public FarmResponseResult login(@RequestBody LoginUserDto user, HttpServletRequest request, HttpServletResponse response) {
        //通过springSecurity登录
        // Authentication back = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getLoginname(), user.getPassword()));
        //校验验证码
        if (!farmLoginSafeUtils.validImgCode(user.getLoginname(), user.getImgcode())) {
            return new FarmResponseResult<Map>(FarmResponseCode.ERROR, I18n.msg(FarmLoginSafeUtils.MATCHES_FALSEMSG), null);
        }
        //从数据库加载用户信息
        FarmUserContext dbuser = farmUserService.getUserByLoginName(user.getLoginname());
        if (Objects.isNull(dbuser) || (!dbuser.getState().equals(FarmUserStateEnum.PROBATION.getType()) && !dbuser.getState().equals(FarmUserStateEnum.ACTIVE.getType()))) {
            farmLoginSafeUtils.registValidLoginResult(user.getLoginname(), false);
            //"登录名或密码错误"
            throw new RuntimeException(I18n.msg(FarmPasswordEncoder.MATCHES_FALSEMSG));
        }
        //验证用户密码
        FarmPasswordEncoder passwordEncoder = new FarmPasswordEncoder();
        if (!passwordEncoder.matches(user.getPassword(), FarmPasswordEncoder.getSaltByCode(user.getCode()), dbuser.getPassword())) {
            farmLoginSafeUtils.registValidLoginResult(user.getLoginname(), false);
            //"登录名或密码错误"
            throw new RuntimeException(I18n.msg(FarmPasswordEncoder.MATCHES_FALSEMSG));
        }
        //为验证码校验提供数据（记录登录结果）
        farmLoginSafeUtils.registValidLoginResult(user.getLoginname(), true);
        dbuser.setIp(IpUtils.getClientIp(request));

        Map<String, Object> claims = FarmJwtClaims.getInstance()
                .addUserKey(dbuser.getLoginname())
                .addFeild(FarmJwtConfig.ClaimsKey.LoginId, dbuser.initLoginid())
                .addFeild(FarmJwtConfig.ClaimsKey.IP, IpUtils.getClientIp(request))
                .getClaims();
        //签发生成验证JWT
        String jwt_auth = FarmJwtUtils.createAuthJwt(claims);
        String jwt_refresh = FarmJwtUtils.createRefreshJwt(claims);
        //登录用户（存入全局缓存中）
        FarmUserOnlines.login(dbuser);
        // 👇 关键：根据 jwtModel 决定如何返回 token
        if ("cookie".equals(jwtModel)) {
            // 写入 Cookie（和 Filter 中一致）
            FarmCookieJwtUtils.addTokenToCookie(response, jwt_auth, 30 * 60); // 30分钟
            FarmCookieJwtUtils.addRefreshTokenToCookie(response, jwt_refresh, 7 * 24 * 60 * 60); // 7天
            return new FarmResponseResult(FarmResponseCode.SUCESS, "login success", null);
        } else {
            // Header 模式：返回 token 字符串
            return new FarmResponseResult<Map>(FarmResponseCode.SUCESS, FarmMaps.getInstance().put("token", jwt_auth + "||" + jwt_refresh).getMap());
        }
    }

    @Operation(summary = "用戶注销", description = "注销当前用户")
    @GetMapping(value = "/out")
    @ResponseBody
    public FarmResponseResult out(HttpServletResponse response) {
        FarmUserContext user = FarmUserContextLoader.getCurrentUser();
        if (user == null) {
            return new FarmResponseResult<Map>(FarmResponseCode.WARN, "not login");
        }
        FarmUserOnlines.logout(user);
        // 清除 Cookie
        if ("cookie".equals(jwtModel)) {
            FarmCookieJwtUtils.clearCookie(response, ACCESS_TOKEN_COOKIE_NAME);
            FarmCookieJwtUtils.clearCookie(response, REFRESH_TOKEN_COOKIE_NAME);
        }
        return new FarmResponseResult<Map>(FarmResponseCode.SUCESS, "logout");
    }


    /**
     * 生成验证码
     *
     * @param loginname
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/captcha/{loginname}")
    public void getCaptcha(@PathVariable String loginname, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 创建算术验证码对象（你也可以选择其他类型的验证码）
        SpecCaptcha captcha = new SpecCaptcha(110, 40, 4);
        //获取验证码：captcha.text());
        farmLoginSafeUtils.registImgCode(loginname, captcha.text());
        captcha.setLen(4); // 设置验证码长度为3位
        // 设置响应头
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        // 输出验证码图片
        captcha.out(response.getOutputStream());
    }

    /**
     * 仅JWT在local模式下使用（ 通过前端存储jwt，非cookie模式）
     *
     * @param keys
     * @param request
     * @return
     */
    @Operation(summary = "刷新token", description = "客户端请求刷新token授权")
    @PostMapping(value = "/rekey")
    @ResponseBody
    public FarmResponseResult rekey(@RequestBody TokensDto keys, HttpServletRequest request) {
        try {
            FarmJwtClaims clainms = FarmJwtUtils.parseJWT(keys.getLongKey());
            if (clainms == null) {
                return new FarmResponseResult<String>(FarmResponseCode.SUCESS, null, null);
            }
            if (clainms.get(FarmJwtConfig.ClaimsKey.TYPE).equals("2")) {
                if (IpUtils.getClientIp(request).equals(clainms.get(FarmJwtConfig.ClaimsKey.IP))) {
                    //记录刷新次数
                    Integer loadNum = FarmCaches.getInstance().getCacheData(keys.getLongKey(), FarmCacheKeys.JWT_REFRESH_KES);
                    if (loadNum != null && loadNum > 20) {
                        //一个周期内使用超过10次就停止响应
                        return new FarmResponseResult<String>(FarmResponseCode.SUCESS, null, null);
                    } else {
                        if (loadNum == null) {
                            loadNum = 0;
                        }
                        FarmCaches.getInstance().putCacheData(keys.getLongKey(), ++loadNum, FarmCacheKeys.JWT_REFRESH_KES);
                    }
                    // System.out.println("有效期" + diffInSeconds);
                    //在过期前最后35分钟以内刷新有效
                    Map<String, Object> claims = FarmJwtClaims.getInstance()
                            .addUserKey((String) clainms.get(FarmJwtConfig.ClaimsKey.LoginName))
                            .addFeild(FarmJwtConfig.ClaimsKey.LoginId, (String) clainms.get(FarmJwtConfig.ClaimsKey.LoginId))
                            .addFeild(FarmJwtConfig.ClaimsKey.IP, (String) clainms.get(FarmJwtConfig.ClaimsKey.IP))
                            .getClaims();
                    String jwt = FarmJwtUtils.createAuthJwt(claims);
                    return new FarmResponseResult<String>(FarmResponseCode.SUCESS, null, jwt);
                } else {
                    throw new RuntimeException(I18n.msg("网络环境改变,请重新登录"));
                }
            }
            return new FarmResponseResult<String>(FarmResponseCode.SUCESS, null, null);
        } catch (Exception e) {
            throw e;
        }
    }
}
