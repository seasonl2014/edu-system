package cn.xueden.edu.wechat.utils;

import cn.xueden.edu.domain.EduWxpay;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.ValidationException;
import com.wechat.pay.java.core.notification.NotificationConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**功能描述：处理微信支付回调通知
 * @author:梁志杰
 * @date:2023/9/4
 * @description:cn.xueden.edu.wechat.utils
 * @version:1.0
 */
@Slf4j
public class WxPayUtil {

    /**
     * 读取请求数据流
     *
     * @param request
     * @return
     */
    private static String getRequestBody(HttpServletRequest request) {

        StringBuffer sb = new StringBuffer();

        try (ServletInputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.error("读取数据流异常:{}", e);
        }
        return sb.toString();

    }

    /**
     * 回调通知的验签与解密
     * @param request
     * @param dbWxpay
     * @return
     */
    public static Transaction verificationAndDecryption(HttpServletRequest request,
                                                        EduWxpay dbWxpay){
        //获取报文
        String requestBody=getRequestBody(request);
        //随机串
        String wechatpayNonce = request.getHeader("Wechatpay-Nonce");
        //微信传递过来的签名
        String wechatSignature = request.getHeader("Wechatpay-Signature");
        //证书序列号（微信平台）
        String wechatPaySerial = request.getHeader("Wechatpay-Serial");
        //时间戳
        String wechatTimestamp = request.getHeader("Wechatpay-Timestamp");
        // 构造 RequestParam
        RequestParam requestParam = new RequestParam.Builder()
                .serialNumber(wechatPaySerial)
                .nonce(wechatpayNonce)
                .signature(wechatSignature)
                .timestamp(wechatTimestamp)
                .body(requestBody)
                .build();

        // 如果已经初始化了 RSAAutoCertificateConfig，可直接使用
        // 没有的话，则构造一个
        NotificationConfig config = new RSAAutoCertificateConfig.Builder()
                .merchantId(dbWxpay.getMerchantId())
                .privateKeyFromPath(dbWxpay.getMerchantPrivatekey())
                .merchantSerialNumber(dbWxpay.getMerchantSerialnumber())
                .apiV3Key(dbWxpay.getApiV3())
                .build();

        // 初始化 NotificationParser
        NotificationParser parser = new NotificationParser(config);
        try {
            // 以支付通知回调为例，验签、解密并转换成 Transaction
            Transaction transaction = parser.parse(requestParam, Transaction.class);
            return transaction;
        } catch (ValidationException e) {
            // 签名验证失败，返回 401 UNAUTHORIZED 状态码
            log.error("sign verification failed", e);
            return null;
        }

    }

}
