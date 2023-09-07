package cn.xueden.edu.wechat.service;

import cn.xueden.edu.domain.EduCouponStock;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduWxpayRepository;


import cn.xueden.edu.wechat.dto.WxOrderDto;

import cn.xueden.utils.XuedenUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.cashcoupons.CashCouponsService;
import com.wechat.pay.java.service.cashcoupons.model.*;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;

import com.wechat.pay.java.service.refund.RefundService;
import com.wechat.pay.java.service.refund.model.AmountReq;
import com.wechat.pay.java.service.refund.model.CreateRequest;
import com.wechat.pay.java.service.refund.model.QueryByOutRefundNoRequest;
import com.wechat.pay.java.service.refund.model.Refund;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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

    public static RefundService service;

    public static CashCouponsService cashCouponsService;

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

    /**
     * 订单退款
     * @param dbEduWxpay 微信支付配置信息
     * @param orderNo 原来订单编号
     * @param remarks 退款原因
     * @param price 退款金额
     * @param oldPrice 原订单金额
     */
    public Refund refundOrder(EduWxpay dbEduWxpay,
                            String orderNo,
                            String remarks,
                            BigDecimal price,
                            BigDecimal oldPrice){
        // 初始化商户配置
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(dbEduWxpay.getMerchantId())
                        // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                        .privateKey(dbEduWxpay.getMerchantPrivatekey())
                        .merchantSerialNumber(dbEduWxpay.getMerchantSerialnumber())
                        .apiV3Key(dbEduWxpay.getApiV3())
                        .build();

        // 初始化服务
        service = new RefundService.Builder().config(config).build();
        // ... 调用接口
        try {
            // 生成退款单号
            String outRefundNo = XuedenUtil.createOrderNumber();
            Refund response = create(orderNo,remarks,price,oldPrice,outRefundNo);
            log.info("申请退款返回信息{}",response);
            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e);
        }
        return null;
    }

    /**
     * 退款申请
     * */
    public static Refund create(String orderNo,
                                String remarks,
                                BigDecimal price,
                                BigDecimal oldPrice,
                                String outRefundNo) {
        CreateRequest request = new CreateRequest();
        // 金额
        AmountReq amountReq = new AmountReq();
        // 原订单金额 说明：原支付交易的订单总金额，币种的最小单位，只能为整数。
        amountReq.setTotal(oldPrice.longValue());
        //退款金额 说明：退款金额，币种的最小单位，只能为整数，不能超过原订单支付金额。
        amountReq.setRefund(price.longValue());
        // 退款币种 说明：符合ISO 4217标准的三位字母代码，目前只支持人民币：CNY
        amountReq.setCurrency("CNY");
        request.setAmount(amountReq);
        // 订单编号
        request.setOutTradeNo(orderNo);
        // 退款原因
        request.setReason(remarks);
        // 退款单号
        request.setOutRefundNo(outRefundNo);
        // 调用接口
        return service.create(request);
    }

    /** 查询单笔退款（通过商户退款单号） */
    public static Refund queryByOutRefundNo() {

        QueryByOutRefundNoRequest request = new QueryByOutRefundNoRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        return service.queryByOutRefundNo(request);
    }


    /**
     * 创建代金券批次
     * */
    public  CreateCouponStockResponse createCouponStock(EduWxpay dbEduWxpay,
                                                        EduCouponStock eduCoupon) {
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(dbEduWxpay.getMerchantId())
                        .privateKey(dbEduWxpay.getMerchantPrivatekey())
                        .merchantSerialNumber(dbEduWxpay.getMerchantSerialnumber())
                        .apiV3Key(dbEduWxpay.getApiV3())
                        .build();
        // 初始化服务
        cashCouponsService = new CashCouponsService.Builder().config(config).build();
        CreateCouponStockRequest request = new CreateCouponStockRequest();
        // 开始时间
        request.setAvailableBeginTime(eduCoupon.getAvailableBeginTime());
        // 结束时间
        request.setAvailableEndTime(eduCoupon.getAvailableEndTime());
        // 批次名称
        request.setStockName(eduCoupon.getStockName());
        // 备注
        request.setComment(eduCoupon.getRemarks());
        // 归属商户号
        request.setBelongMerchant(dbEduWxpay.getMerchantId());
        // 是否无资金流 免充值设置为true
        request.setNoCash(true);
        // 批次类型 NORMAL-固定面额满减券批次
        request.setStockType("NORMAL");
        // 商户单据号
        request.setOutRequestNo(eduCoupon.getOutRequestNo());
        // 发放规则 ---开始
        StockRule stockRule = new StockRule();
        // 发放总上限
        stockRule.setMaxCoupons(eduCoupon.getMaxCoupons());
        // 总预算 批次预算等于批次面额乘以发券数 说明：总消耗金额，单位分
        stockRule.setMaxAmount(eduCoupon.getCouponAmount()*eduCoupon.getMaxCoupons()*100);
        // 是否开启自然人限制
        stockRule.setNaturalPersonLimit(false);
        // api发券防刷
        stockRule.setPreventApiAbuse(true);

        // 单个用户领取个数
        stockRule.setMaxCouponsPerUser(eduCoupon.getMaxCouponsPerUser());
        request.setStockUseRule(stockRule);
        // 发放规则 ---结束

        // 核销规则 ---开始
        CouponRule couponRule = new CouponRule();
        FixedValueStockMsg fixedValueStockMsg = new FixedValueStockMsg();
        // 面额 单位是分
        fixedValueStockMsg.setCouponAmount(eduCoupon.getCouponAmount()*100);
        // 使用券金额门槛 单位是分
        fixedValueStockMsg.setTransactionMinimum(eduCoupon.getTransactionMinimum()*100);
        couponRule.setFixedNormalCoupon(fixedValueStockMsg);

        // 可核销商户号
        List<String> availableMerchants = new ArrayList<>();
        availableMerchants.add(dbEduWxpay.getMerchantId());
        couponRule.setAvailableMerchants(availableMerchants);

        request.setCouponUseRule(couponRule);
        // 核销规则 ---结束

        try {
            CreateCouponStockResponse response = cashCouponsService.createCouponStock(request);
            log.info("创建指定批次的代金券返回结果{}",response);

            // 查询代金券消息通知地址
            Callback callback = queryCallback(dbEduWxpay);
            if(callback==null){
                // 设置代金券消息通知地址
                setCallback(true,dbEduWxpay);
            }

            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e.getMessage());
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e.getResponseBody());
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e.getMessage());
        }

        return null;
    }


    /**
     * 发放指定批次的代金券
     * @param outRequestNo 商户单据号
     * @param wxOpenId 微信openID
     * @param eduCoupon 代金券信息
     * @param dbEduWxpay 微信支付配置信息
     * @return
     */
    public  SendCouponResponse sendCoupon(String outRequestNo,
                                          String wxOpenId,
                                          EduCouponStock eduCoupon,
                                          EduWxpay dbEduWxpay) {

        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(dbEduWxpay.getMerchantId())
                        .privateKey(dbEduWxpay.getMerchantPrivatekey())
                        .merchantSerialNumber(dbEduWxpay.getMerchantSerialnumber())
                        .apiV3Key(dbEduWxpay.getApiV3())
                        .build();
        // 初始化服务
        cashCouponsService = new CashCouponsService.Builder().config(config).build();

        SendCouponRequest request = new SendCouponRequest();

        try {
            // 批次ID
            request.setStockId(eduCoupon.getStockId());
            // 用户openid
            request.setOpenid(wxOpenId);
            // 商户单据号
            request.setOutRequestNo(outRequestNo);
            // 公众账号ID
            request.setAppid(dbEduWxpay.getAppId());
            // 创建批次的商户号
            request.setStockCreatorMchid(dbEduWxpay.getMerchantId());
            SendCouponResponse response = cashCouponsService.sendCoupon(request);
            log.info("发放指定批次的代金券{}",response);
            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e.getMessage());
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e.getResponseBody());
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e.getMessage());
        }

       return null;
    }

    /** 激活开启指定批次批次 */
    public  StartStockResponse startStock(EduCouponStock eduCoupon,
                                          EduWxpay dbEduWxpay) {
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(dbEduWxpay.getMerchantId())
                        .privateKey(dbEduWxpay.getMerchantPrivatekey())
                        .merchantSerialNumber(dbEduWxpay.getMerchantSerialnumber())
                        .apiV3Key(dbEduWxpay.getApiV3())
                        .build();
        // 初始化服务
        cashCouponsService = new CashCouponsService.Builder().config(config).build();
        StartStockRequest request = new StartStockRequest();
        try {
            // 创建批次的商户号 说明：批次创建方商户号
            request.setStockCreatorMchid(dbEduWxpay.getMerchantId());
            // 批次号 说明：批次号
            request.setStockId(eduCoupon.getStockId());
            StartStockResponse response = cashCouponsService.startStock(request);
            log.info("激活开启指定批次批次返回信息{}",response);
            // 查询代金券消息通知地址
            Callback callback = queryCallback(dbEduWxpay);
            if(callback==null){
                // 设置代金券消息通知地址
                setCallback(true,dbEduWxpay);
            }
            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e);
        }

        return null;
    }

    /**
     * 查询批次详情
     * */
    public  Stock queryStock(EduCouponStock eduCoupon,
                             EduWxpay dbEduWxpay) {
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(dbEduWxpay.getMerchantId())
                        .privateKey(dbEduWxpay.getMerchantPrivatekey())
                        .merchantSerialNumber(dbEduWxpay.getMerchantSerialnumber())
                        .apiV3Key(dbEduWxpay.getApiV3())
                        .build();
        // 初始化服务
        cashCouponsService = new CashCouponsService.Builder().config(config).build();
        QueryStockRequest request = new QueryStockRequest();
        try {
            // 批次号 说明：批次id
            request.setStockId(eduCoupon.getStockId());
            // 创建批次的商户号 说明：批次创建时的商户号
            request.setStockCreatorMchid(dbEduWxpay.getMerchantId());

            Stock response = cashCouponsService.queryStock(request);
            log.info("查询指定批次批次返回信息{}",response);
            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e);
        }
       return null;
    }

    /**
     * 查询代金券消息通知地址
     * */
    public  Callback queryCallback(EduWxpay dbEduWxpay) {

        QueryCallbackRequest request = new QueryCallbackRequest();
        try {
            request.setMchid(dbEduWxpay.getMerchantId());
            Callback response = cashCouponsService.queryCallback(request);
            log.info("查询代金券消息通知地址返回信息{}",response);
            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e);
        }
        return null;

    }

    /** 设置代金券消息通知地址 */
    public static SetCallbackResponse setCallback(Boolean _switch,
                                                  EduWxpay dbEduWxpay) {
        SetCallbackRequest request = new SetCallbackRequest();
        try {
            // 回调开关 说明：true-开启推送；false-停止推送
            request.setSwitch(_switch);
            // 通知url地址 说明：支付通知商户url地址
            //request.setNotifyUrl("https://xueden.natapp4.cc/edu/coupon/callback");
            request.setNotifyUrl(dbEduWxpay.getNotifyCouponUrl());
            // 商户号
            request.setMchid(dbEduWxpay.getMerchantId());
            SetCallbackResponse response = cashCouponsService.setCallback(request);
            log.info("设置代金券消息通知地址返回信息{}",response);
            return response;
        } catch (HttpException e) { // 发送HTTP请求失败
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
            log.info("发送HTTP请求失败",e);
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
            log.info("服务返回状态小于200或大于等于300",e);
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
            log.info("返回体类型不合法，或者解析返回体失败",e);
        }
        return null;

    }

}
