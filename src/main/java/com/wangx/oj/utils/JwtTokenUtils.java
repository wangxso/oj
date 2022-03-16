package com.wangx.oj.utils;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
public class JwtTokenUtils {
    @Autowired
    private RedisUtils redisUtils;

    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String SECRET = "jwtsecretdemo";
    private static final String ISS = "echisan";

    // 过期时间是3600秒，既是1个小时
    private static final long EXPIRATION = 3600L;

    // 选择了记住我之后的过期时间为7天
    private static final long EXPIRATION_REMEMBER = 604800L;
    // 添加角色的key
    private static final String ROLE_CLAIMS = "rol";
    // 创建token
    public static String createToken(String username, Map<String,Object> claims  , boolean isRememberMe) {
        long expiration = isRememberMe ? EXPIRATION_REMEMBER : EXPIRATION;
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .setClaims(claims)
                .setIssuer(ISS)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .compact();
    }

    // 从token中获取用户名
    public static String getUsername(String token){
        return getTokenBody(token).getSubject();
    }

    public static List<SimpleGrantedAuthority> getUserRole(String token){
        String s = (String) getTokenBody(token).get(ROLE_CLAIMS);
        log.info("get string" + s);
        List<String> grantedAuthorities = JSON.parseArray(s, String.class);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        try {
            for (String role : grantedAuthorities){
                authorities.add(new SimpleGrantedAuthority(role));
            }
        } catch (NullPointerException e) {
            return null;
        }
        return authorities;
    }

    /**
     * @author: zzx
     * @date: 2018-10-19 09:10
     * @deprecation: 解析token,获得subject中的信息
     */
    public static String parseToken(String token) {
        String subject = null;
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET) // 不使用公钥私钥
                    .parseClaimsJws(token).getBody();
            subject = getTokenBody(token).getSubject();
        } catch (Exception e) {
        }
        return subject;
    }



    // 是否已过期
    public static boolean isExpiration(String token){
        return getTokenBody(token).getExpiration().before(new Date());
    }


    public static Claims getTokenBody(String token){
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Set authoritiesToArray(Collection<? extends GrantedAuthority> authorities) {
        Set<String> list = new HashSet<>();
        for (GrantedAuthority ga : authorities) {
            list.add(ga.getAuthority());
        }
        return list;
    }
}
