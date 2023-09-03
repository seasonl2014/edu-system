package cn.xueden.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**功能描述：jwt工具类
 * @author:梁志杰
 * @date:2023/5/22
 * @description:cn.xueden.utils
 * @version:1.0
 */
public class JWTUtil {

    /**
     * 密钥要保管好,可以放到配置文件中使用@Vaule或者@ConfigurationProperties读取
     */
    private static String SECRET = "a1b2ca0eff66930b08463073384fbb40";

    /**
     * 传入payload信息获取token
     *
     * @param map payload
     * @return token
     */
    public static String getToken(Map<String, String> map) {
        JWTCreator.Builder builder = JWT.create();
        //payload
        map.forEach(builder::withClaim);
        Calendar instance = Calendar.getInstance();
        //365天过期
        instance.add(Calendar.DATE, 365);
        //指定令牌的过期时间
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证token
     */
    public static DecodedJWT verify(String token) {
        //如果有任何验证异常，此处都会抛出异常
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
    }

    /**
     * 获取token中的payload
     */
    public static Map<String, Claim> getPayloadFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getClaims();
    }

}
