package org.farm2.base.jwt;

import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 封裝claims
 */
public class FarmJwtClaims {
    private Map<String, Object> claims = new HashMap<>();
    private Date expiration;

    public static FarmJwtClaims getInstance() {
        return new FarmJwtClaims();
    }

    public FarmJwtClaims(Claims claims) {
        this.claims.putAll(claims);
        this.expiration = claims.getExpiration();
    }

    private FarmJwtClaims() {

    }

    public FarmJwtClaims addFeild(FarmJwtConfig.ClaimsKey claimsKey, Object value) {
        claims.put(claimsKey.getKey(), value);
        return this;
    }

    public Map<String, Object> getClaims() {
        return claims;
    }

    public FarmJwtClaims addUserKey(String userKey) {
        claims.put(FarmJwtConfig.ClaimsKey.LoginName.getKey(), userKey);
        return this;
    }

    public Date getExpiration() {
        return expiration;
    }

    public Object get(FarmJwtConfig.ClaimsKey claimsKey) {
        return claims.get(claimsKey.getKey());
    }
}
