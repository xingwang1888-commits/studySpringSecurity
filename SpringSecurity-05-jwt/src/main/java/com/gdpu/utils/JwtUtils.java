package com.gdpu.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {
    //生成一个密钥
    private static final String SECRET = "David_wannnng";


    /**
     * 生成jwt
     * @param userId
     * @param username
     * @param authority
     * @return
     */
    public static String createJWT(Integer userId, String username, List<String> authority){
        //获取系统当前时间
        Date currentTime = new Date();
        //根据当前时间，设置过期实现(5分钟)
        Date expireTime = new Date(currentTime.getTime() + 5 * 60 * 1000);
        //设置头数据
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");

        return JWT.create()
                .withHeader(header) //头
                .withClaim("userId", userId) //自定义数据
                .withClaim("username", username) //自定义数据
                .withClaim("authority", authority) //自定义数据
                .withIssuedAt(currentTime) //创建时间
                .withExpiresAt(expireTime)//过期时间
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证jwt
     * @param jwt
     * @return
     */
    public static Boolean verifyJWT(String jwt){
        try{
            // 使用秘钥创建一个解析对象
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
     * @param jwt
     * @return
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
     * @param jwt
     * @return
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
     * @param jwt
     * @return
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
