package com.gdpu.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtUtils {
    //生成一个密钥签名
    private static final String SECRET = "David_wannnng";

    public static String createJWT(Integer userId, String username, List<String> authority){
        Date currentTime = new Date();
        //设置截止时间，5分钟
        Date expireTime = new Date(currentTime.getTime() + 5 * 60 * 1000);
        //设置头数据
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create()
                .withHeader(header) //头
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withClaim("authority", authority)
                .withIssuedAt(currentTime)
                .withExpiresAt(expireTime)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 认证JWT
     */
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

    /**
     * 获取用户id
     */
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

    /**
     * 获取用户名
     */
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

    /**
     * 获取权限
     */
    public static List<String> getAuthority(String jwt){
        try{
            //使用密钥创建一个解析对象
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            //验证jwt
            DecodedJWT decodedJWT = jwtVerifier.verify(jwt);
            //获取权限
            Claim authority = decodedJWT.getClaim("authority");
            return authority.asList(String.class);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
