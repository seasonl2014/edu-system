package cn.xueden.utils;

import cn.xueden.exception.BadRequestException;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpStatus;

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
        //7天过期
        //instance.add(Calendar.DATE, 7);
        instance.add(Calendar.MINUTE,1);
        //指定令牌的过期时间
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证token
     */
    public static DecodedJWT verify(String token) {
        //如果有任何验证异常，此处都会抛出异常
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
            return decodedJWT;
        }catch (SignatureVerificationException e) {
         e.printStackTrace();
         throw new BadRequestException("签名不一致");
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            throw new BadRequestException(HttpStatus.UNAUTHORIZED,"令牌过期,请退出重新登录");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            throw new BadRequestException("算法不匹配");
        } catch (InvalidClaimException e) {
            e.printStackTrace();
            throw new BadRequestException("失效的payload");
        } catch (Exception e) {
            throw new BadRequestException("token无效");
        }

    }

    /**
     * 获取token中的payload
     */
    public static Map<String, Claim> getPayloadFromToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token).getClaims();
    }

}
