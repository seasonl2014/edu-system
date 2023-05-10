package cn.xueden.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;

import cn.xueden.system.domain.SysUser;

import java.util.HashMap;
import java.util.Map;

/**功能描述：JWT工具类
 * @author:梁志杰
 * @date:2023/1/29
 * @description:cn.xueden.utils
 * @version:1.0
 */
public class HutoolJWTUtil {
    /**
     * 生成系统用户token
     * @param sysUser
     * @return
     */
    public static String createToken(SysUser sysUser){
        DateTime now = DateTime.now();
        DateTime newTime = now.offsetNew(DateField.MINUTE,120);
        Map<String,Object> payload = new HashMap<String,Object>();
        // 签发时间
        payload.put(JWTPayload.ISSUED_AT,now);
        // 过期时间
        payload.put(JWTPayload.EXPIRES_AT,newTime);
        // 生效时间
        payload.put(JWTPayload.NOT_BEFORE,now);
        // 载荷
        payload.put("username",sysUser.getUsername());
        payload.put("aid",sysUser.getId());
        String key = "www.xueden.cn";
        String token = JWTUtil.createToken(payload,key.getBytes());
        return token;
    }


    /**
     * 解析token
     * @param token
     * @return
     */
    public static Long parseToken(String token){
        final JWT jwt = JWTUtil.parseToken(token);
        String aid = jwt.getPayload("aid").toString();
        return Long.parseLong(aid);
    }

}
