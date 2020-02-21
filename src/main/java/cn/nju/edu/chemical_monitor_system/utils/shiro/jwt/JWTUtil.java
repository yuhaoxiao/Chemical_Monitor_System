package cn.nju.edu.chemical_monitor_system.utils.shiro.jwt;

import cn.nju.edu.chemical_monitor_system.constant.ConstantVariables;
import cn.nju.edu.chemical_monitor_system.exception.UserException;
import cn.nju.edu.chemical_monitor_system.utils.encryption.Base64ConvertUtil;
import cn.nju.edu.chemical_monitor_system.exception.UnauthorizedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * @author 肖宇豪
 */
@Component
public class JWTUtil {
    public static boolean verify(String token) {
        String secret = null;
        try {
            secret = getClaim(token, ConstantVariables.USERNAME) + Base64ConvertUtil.decode(ConstantVariables.ENCRYPT_JWT);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            throw new UnauthorizedException("token解析过程中出现异常");
        }
    }

    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String sign(String username, String currentTimeMillis) {
        String secret = null;
        try {
            secret = username + Base64ConvertUtil.decode(ConstantVariables.ENCRYPT_JWT);
            Date date = new Date(System.currentTimeMillis() + ConstantVariables.EXPIRE_TIME);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withClaim(ConstantVariables.USERNAME, username)
                    .withClaim(ConstantVariables.CURRENT_TIME_MILLIS, currentTimeMillis)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            throw new UserException("JWTToken加密出现UnsupportedEncodingException异常");
        }
    }
}