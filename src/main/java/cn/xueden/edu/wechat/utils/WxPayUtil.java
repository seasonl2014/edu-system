package cn.xueden.edu.wechat.utils;


import cn.xueden.edu.domain.EduWxpay;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.Verifier;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.cert.CertificatesManager;
import com.wechat.pay.contrib.apache.httpclient.exception.HttpCodeException;
import com.wechat.pay.contrib.apache.httpclient.exception.NotFoundException;
import com.wechat.pay.contrib.apache.httpclient.exception.ParseException;
import com.wechat.pay.contrib.apache.httpclient.exception.ValidationException;
import com.wechat.pay.contrib.apache.httpclient.notification.Notification;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationHandler;
import com.wechat.pay.contrib.apache.httpclient.notification.NotificationRequest;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**功能描述：微信支付
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.utils
 * @version:1.0
 */
@Component
@Slf4j
public class WxPayUtil {
    /**
     *
     * @param merchantId
     *          商户id
     * @param merchantSerialNumber
     *         商户证书序列号
     * @param privateKeyFilePath
     *        商户私钥PrivateKey实例
     * @param wechatpayCertificatePath
     *        平微信支付平台证书的X509Certificate实例列表
     * @return
     * @throws Exception
     */
    public static CloseableHttpClient getClient(String merchantId, String merchantSerialNumber, String privateKeyFilePath, String wechatpayCertificatePath) throws Exception {
        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
                .withMerchant(merchantId, merchantSerialNumber, PemUtil.loadPrivateKey(new FileInputStream(privateKeyFilePath)));

        List<X509Certificate> certs = new ArrayList<>();
        certs.add(PemUtil.loadCertificate(new FileInputStream(wechatpayCertificatePath)));
        builder.withWechatpay(certs);
        return builder.build();
    }

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
     * @return
     */
    public static Notification verificationAndDecryption(HttpServletRequest request,
                                                         EduWxpay dbWxpay){
        try {
            //获取报文
            String body = getRequestBody(request);
            //随机串
            String wechatpayNonce = request.getHeader("Wechatpay-Nonce");

            //微信传递过来的签名
            String wechatpaySignature = request.getHeader("Wechatpay-Signature");

            //证书序列号（微信平台）
            String wechatpaySerial = request.getHeader("Wechatpay-Serial");

            //时间戳
            String wechatpayTimestamp = request.getHeader("Wechatpay-Timestamp");

            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(
                    new ByteArrayInputStream(dbWxpay.getMerchantPrivatekey().getBytes("utf-8")));
            // 获取证书管理器实例
            CertificatesManager certificatesManager = CertificatesManager.getInstance();
            // 向证书管理器增加需要自动更新平台证书的商户信息
            certificatesManager.putMerchant(dbWxpay.getMerchantId(), new WechatPay2Credentials(dbWxpay.getMerchantId(),
                    new PrivateKeySigner(dbWxpay.getMerchantSerialnumber(), merchantPrivateKey)), dbWxpay.getApiV3().getBytes(StandardCharsets.UTF_8));
            // 从证书管理器中获取verifier
            Verifier verifier = certificatesManager.getVerifier(dbWxpay.getMerchantId());
            // 构建request，传入必要参数
            NotificationRequest notificationRequest = new NotificationRequest.Builder().withSerialNumber(wechatpaySerial)
                    .withNonce(wechatpayNonce)
                    .withTimestamp(wechatpayTimestamp)
                    .withSignature(wechatpaySignature)
                    .withBody(body)
                    .build();
            NotificationHandler handler = new NotificationHandler(verifier, dbWxpay.getApiV3().getBytes(StandardCharsets.UTF_8));
            // 验签和解析请求体
            Notification notification = handler.parse(notificationRequest);
            return notification;

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (HttpCodeException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ValidationException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
