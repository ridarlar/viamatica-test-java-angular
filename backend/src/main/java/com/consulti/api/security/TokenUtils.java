package com.consulti.api.security;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class TokenUtils {

    private static final byte[] SECRET_KEY_BYTES = "3jh598543hn7yf45y34%Y$^y25n986725m62354".getBytes();
    private static final SecretKey ACCESS_TOKEN_SECRET = new SecretKeySpec(SECRET_KEY_BYTES, SignatureAlgorithm.HS256.getJcaName());

    private final static Long ACCESS_TOKEN_VALIDITY_SECONDS=30 * 24 * 60 * 60L;

    public static String createToken(String name, String email){
        long expirationTime=ACCESS_TOKEN_VALIDITY_SECONDS * 1_000;
        Date expirationDate=new Date(System.currentTimeMillis()+expirationTime);

        Map<String, Object> extra= new HashMap<>();
        extra.put("name",name);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(extra)
                .signWith(ACCESS_TOKEN_SECRET)
                .compact();
    }

    public static UsernamePasswordAuthenticationToken getAthentication(String token){
       try{
           Claims claims = Jwts.parserBuilder()
                   .setSigningKey(ACCESS_TOKEN_SECRET)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();

           String userName = claims.getSubject();

           return new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
       }catch (JwtException error){
           System.out.println(error.getMessage());
            return null;
       }
    }
}
