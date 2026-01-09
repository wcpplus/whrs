package org.farm2.base.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.farm2.tools.config.Farm2ConfigEnum;
import org.farm2.tools.config.Farm2ConfigUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
public class FarmJwtUtils {
    public static SecretKey skey = null;
    public static String skeyText = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.jwt.secret.key");
    public static Boolean KEY_TYPE_RANDOM = new Farm2ConfigUtils(Farm2ConfigEnum.FARM2_PROPERTIES).getData("farm2.conf.jwt.secret.key").equals("RANDOM");
    /**是否使用随机密钥
     * @param keyTypeRandom
     */
    public static void setKeyTypeRandom(Boolean keyTypeRandom) {
        KEY_TYPE_RANDOM = keyTypeRandom;
    }

    private static SecretKey getSecretKey() {
        if (skey == null) {
            if (KEY_TYPE_RANDOM) {
                skey = Jwts.SIG.HS256.key().build();
            } else {
                skey = Keys.hmacShaKeyFor(skeyText.getBytes(StandardCharsets.UTF_8));
            }
        }
        return skey;
    }

    /**
     * 生成jwt
     * 使用Hs256算法
     *
     * @param ttlMillis jwt过期时间(毫秒)
     * @param claims    设置的信息
     * @return
     */
    public static String createJwt(long ttlMillis, Map<String, Object> claims) {
        // 生成JWT的时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);
        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        // 设置jwt的body
        JwtBuilder builder = Jwts.builder()
                // 设置签名使用的签名算法和签名使用的秘钥
                .signWith(getSecretKey())
                // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
                .claims(claims)
                // 设置过期时间
                .expiration(exp);
        return builder.compact();
    }

    /**
     * Token解密
     *
     * @param token 加密后的token
     * @return
     */
    public static FarmJwtClaims parseJWT(String token) {
        Claims claims = null;
        try {
        //生成 HMAC 密钥，根据提供的字节数组长度选择适当的 HMAC 算法，并返回相应的 SecretKey 对象。
        // 得到DefaultJwtParser
        JwtParser jwtParser = Jwts.parser()
                // 设置签名的秘钥
                .verifyWith(getSecretKey()).build();
        Jws<Claims> jws = jwtParser.parseSignedClaims(token);
            claims = jws.getPayload();
        } catch (Exception e) {
            //jwt验证失败会进入这里
            log.error(e.getMessage(), e);
            return null;
        }
        return new FarmJwtClaims(claims);
    }

    /**
     * 获取jwt剩余有效时间
     *
     * @param token jwt字符串
     * @return 剩余有效时间(毫秒), 如果token已过期, 返回负值
     */
    public static long getJwtRemainingTime(String token) {
        FarmJwtClaims claims = parseJWT(token);
        Date expiration = claims.getExpiration();
        long currentTimeMillis = System.currentTimeMillis();
        return (expiration.getTime() - currentTimeMillis)/1000;
    }

    public static String createRefreshJwt(Map<String, Object> claims) {
        claims.put(FarmJwtConfig.ClaimsKey.TYPE.getKey(), "2");
        return createJwt(FarmJwtConfig.getRefreshTtlMillis(), claims);
    }

    public static String createAuthJwt(Map<String, Object> claims) {
        claims.put(FarmJwtConfig.ClaimsKey.TYPE.getKey(), "1");
        return createJwt(FarmJwtConfig.getAuthTtlMillis(), claims);
    }
}


