package com.gdpu.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JWTUtils {
    //生成一个密钥签名
    private static final String SECRET = "David_wannnng";
    public static String createJWT(Integer username, String userId, List<String> authority){
        Date currentTime = new Date();
        Date expireTime = new Date(currentTime.getTime() + 1000 * 60 * 5);

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header) //头
                .withClaim("username", username)
                .withClaim("userId", userId)
                .withClaim("authority", authority)
                .withIssuedAt(currentTime)
                .withExpiresAt(expireTime)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static Boolean verifyJWT(String jwt){
        try{
            //使用密钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证JWT
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static String getUserId(String jwt){
        try{
            //使用密钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证jwt
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            //获取用户id
            Claim userId = decodedJWT.getClaim("userId");
            return userId.asString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    public static String getUsername(String jwt){
        try{
            //使用密钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证jwt
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            //获取用户名
            Claim username = decodedJWT.getClaim("username");
            return username.asString();
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    public static List<String> getAuthority(String jwt){
        try{
            //使用密钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证jwt
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            //获取权限
            Claim authority = decodedJWT.getClaim("authority");
            return authority.asList(String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
