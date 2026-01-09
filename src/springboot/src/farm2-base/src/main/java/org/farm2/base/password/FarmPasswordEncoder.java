package org.farm2.base.password;

import lombok.extern.slf4j.Slf4j;
import org.farm2.tools.caches.FarmCacheKeys;
import org.farm2.tools.caches.FarmCaches;
import org.farm2.tools.db.commons.FarmUUID;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Slf4j
public class FarmPasswordEncoder implements FarmPasswordEncoderInter {
    public String getEncodeModelName() {
        return "FARM2";
    }
    public static String MATCHES_FALSEMSG = "登录名或密码错误";
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    //系统盐长度
    public int dbSaltLen = 10;


    /**
     * 根据code获取盐
     *
     * @param code 通过生成salt时的缓存码兑换盐
     * @return 返回salt
     */
    public static String getSaltByCode(String code) {
        String salt = FarmCaches.getInstance().getCacheData(code, FarmCacheKeys.SALT);
        if (salt == null) {
            salt = "NONE";
        } else {
            FarmCaches.getInstance().removeCacheData(code, FarmCacheKeys.SALT);
        }
        return salt;
    }

    /**
     * 获得加密的数据库密码
     *
     * @param rawPassword 密码明文，用于用户注册时生成数据库密码
     * @return
     */
    @Override
    public String encodeDbPassword(String rawPassword) {
        //用随机盐加密明文密码，将随机盐拼在密码开始位置（10位置）
        String salt = getSalt(dbSaltLen);
        return salt + enHashcode(salt + rawPassword);
    }

    /**
     * 用户前端密码校验数据库密码
     *
     * @param webPassword 前端加密过的密码  HASH(web_salt + db_salt+HASH(DB_salt+rawPassword))
     * @param web_salt    前端盐
     * @param dbPassword  数据库加密的密码
     * @return
     */
    @Override
    public boolean matches(String webPassword, String web_salt, String dbPassword) {
        return webPassword.equals(enHashcode(web_salt + dbPassword));
    }


    public static void main(String[] args) {
        //DBPAWW=  RT8ijBL7NJd97a97207fcb4289c8a9dfa14b356713f97c935003738e856baf39c917d9155f
        //DBdrd94Ets+++++
        //8115b79d4a7d175979443c243c6160599b15a57649d55dcb006ebd508c479020
        //8115b79d4a7d175979443c243c6160599b15a57649d55dcb006ebd508c479020
        String webPass = enHashcode("DBdrd94Ets" + "RT8ijBL7NJd97a97207fcb4289c8a9dfa14b356713f97c935003738e856baf39c917d9155f");
        System.out.println(webPass);
    }

    /**
     * 生成随机密码盐
     *
     * @return
     */
    @Override
    public FarmSalt getSalt() {
        FarmSalt salt = new FarmSalt();
        salt.setVal(getSalt(dbSaltLen));
        salt.setCode(FarmUUID.getUUID32());
        FarmCaches.getInstance().putCacheData(salt.getCode(), salt.getVal(), FarmCacheKeys.SALT);
        return salt;
    }

    /**
     * 生成固定盐
     *
     * @param msg
     * @return
     */
    @Override
    public FarmSalt getSalt(String msg) {
        FarmSalt salt = new FarmSalt();
        salt.setVal(enHashcode(msg).substring(0, dbSaltLen));
        salt.setCode(FarmUUID.getUUID32());
        return salt;
    }

    @Override
    public String getDbSalt(String dbPassword) {
        return dbPassword.substring(0, dbSaltLen);
    }


    /**
     * 获得随机字符串
     *
     * @param len
     * @return
     */
    public static String getSalt(int len) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(index);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }

    /**
     * 哈希加密
     *
     * @param msg
     * @return
     */
    private static String enHashcode(String msg) {
        String token;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(msg.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            token = hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return token;
    }


}