package org.farm2.base.password;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * 加密方法接口
 */
public interface FarmPasswordEncoderInter {
    /**
     * 获得加密的数据库密码
     *
     * @param rawPassword 密码明文，用于用户注册时生成数据库密码
     * @return
     */
    public String encodeDbPassword(String rawPassword);

    /**
     * 用户前端密码校验数据库密码
     *
     * @param webPassword 前端加密过的密码  HASH(web_salt + db_salt+HASH(DB_salt+rawPassword))
     * @param web_salt    前端盐
     * @param dbPassword  数据库加密的密码
     * @return
     */
    boolean matches(String webPassword, String web_salt, String dbPassword);

    /**
     * 生成随机密码盐
     *
     * @return
     */
    public FarmSalt getSalt();

    /**
     * 生成一个固定盐
     *
     * @param msg
     * @return
     */
    public FarmSalt getSalt(String msg);

    /**
     * 从数据库密码获得密码盐
     *
     * @param dbPassword
     * @return
     */
    public String getDbSalt(String dbPassword);
}