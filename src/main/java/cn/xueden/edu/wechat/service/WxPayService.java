package cn.xueden.edu.wechat.service;

import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduWxpayRepository;

import cn.xueden.edu.wechat.dto.WxOrderDto;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;



/**功能描述：微信支付自定义接口
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.service
 * @version:1.0
 */
@Component
@Slf4j
public class WxPayService {
    private final EduWxpayRepository eduWxpayRepository;

    public WxPayService(EduWxpayRepository eduWxpayRepository) {
        this.eduWxpayRepository = eduWxpayRepository;
    }

    /**
     * Native支付统一下单
     */
    public String CreateNativeOrder(WxOrderDto wxOrderDto,EduWxpay dbEduWxpay) {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(dbEduWxpay.getMerchantId())
                        .privateKey(dbEduWxpay.getMerchantPrivatekey())
                        .merchantSerialNumber(dbEduWxpay.getMerchantSerialnumber())
                        .apiV3Key(dbEduWxpay.getApiV3())
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(wxOrderDto.getAmount().getTotal());
        request.setAmount(amount);
        request.setAppid(dbEduWxpay.getAppId());
        request.setMchid(dbEduWxpay.getMerchantId());
        request.setDescription(wxOrderDto.getDescription());
        request.setNotifyUrl(wxOrderDto.getNotify_url());
        request.setOutTradeNo(wxOrderDto.getOut_trade_no());
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
        return response.getCodeUrl();
    }
}
