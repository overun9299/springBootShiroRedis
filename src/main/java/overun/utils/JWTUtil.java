package overun.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @ClassName: JWTUtil
 * @Description:
 * @author: 薏米滴答-西安-zhangPY
 * @version: V1.0
 * @date: 2019/4/11 13:57
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */
public class JWTUtil {

    // 过期时间5分钟
    private static final long EXPIRE_TIME = 5*60*1000;

    /**
     * 校验token是否正确
     * @param token 密钥
     * @param username 用户名
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username" , username).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage()+"verify");
            return false;
        }

    }


    /**
     * 获得token中的信息无需secret解密也能获得
     * @param token
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {

        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (Exception e) {
            System.out.println(e.getMessage()+"getUsername");
            return null;
        }

    }


    /**
     * 生成签名,5min后过期
     * @param username 用户名
     * @param secret 用户的密码（加密后）
     * @return 加密的token
     */
    public static String sign(String username, String secret) {

        try {
            Date date = new Date(System.currentTimeMillis()+EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(Md5Utils.getMd5(username,secret));
            /** 附带username信息 */
            return JWT.create().withClaim("username", username).withExpiresAt(date).sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage()+"sign");
            return null;
        }

    }
}
