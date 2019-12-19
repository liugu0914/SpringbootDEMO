package com.jiopeel.core.config.token;

import static com.auth0.jwt.JWT.create;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenUtil {

    /**
     * 本地公私
     */
    private static final String SECRET = "LYC_SECRET";

    /**
     * 加密token
     *
     * @return token
     * @throws Exception
     */
    public static String createToken(Map<String, String> omap)  {
        //签发时间
        Date btime = new Date();
        Calendar cld = Calendar.getInstance();
        cld.add(Calendar.HOUR, 2);//设置2小时
        //过期时间
        Date etime = cld.getTime();
        Map<String, Object> map = new HashMap<String, Object>();
        JWTCreator.Builder builder = create();
        builder.withHeader(map);
        for (String en : omap.keySet()) {
            builder.withClaim(en, omap.get(en));
        }
        builder.withIssuedAt(btime);
        builder.withExpiresAt(etime);
        String token = null;
        try {
            token = builder
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (UnsupportedEncodingException e) {
            log.error("JWT error :"+e.getMessage());
        }
        return token;
    }

    /**
     * 通过id加密token
     *
     * @param id
     * @return token
     * @throws Exception
     */
    public static String createToken(String id) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        return createToken(map);
    }

    /**
     * 解密token数据
     *
     * @param token
     * @return Map<String, String>
     * @throws Exception
     */
    public static DecodedJWT getJWT(String token) {
        JWTVerifier verifier = null;
        try {
            verifier=JWT.require(Algorithm.HMAC256(SECRET)).build();
        } catch (UnsupportedEncodingException e) {
            log.error("JWT error :"+e.getMessage());
        }
        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException("登陆超时");
        }
        return jwt;
    }
}
