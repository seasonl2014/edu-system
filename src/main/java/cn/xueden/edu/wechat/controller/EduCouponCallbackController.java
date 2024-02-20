package cn.xueden.edu.wechat.controller;

import cn.xueden.edu.domain.EduCouponGrantRecord;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.service.IEduWxpayService;
import cn.xueden.edu.wechat.utils.NotifyResult;
import cn.xueden.edu.wechat.utils.WxPayUtil;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**代金券消息通知地址
 * @author:梁志杰
 * @date:2023/9/7
 * @description:cn.xueden.edu.wechat.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/edu/coupon")
@Slf4j
public class EduCouponCallbackController {

    private final IEduWxpayService eduWxpayService;

    private final IEduStudentService eduStudentService;

    public EduCouponCallbackController(IEduWxpayService eduWxpayService, IEduStudentService eduStudentService) {
        this.eduWxpayService = eduWxpayService;
        this.eduStudentService = eduStudentService;
    }

    /**
     * 代金券消息回调通知
     * @param request
     * @return
     */
    @RequestMapping("/callback")
    public NotifyResult wxCouponCallback(HttpServletRequest request){
        log.info("代金券核销通知地址开始返回通知");
        // 获取微信支付信息
        EduWxpay dbWxpay =  eduWxpayService.getOne();
        //获取报文
        String requestBody= WxPayUtil.getRequestBody(request);
        JSONObject jsonObjectMsg = JSONObject.parseObject(requestBody);
        JSONObject resource = jsonObjectMsg.getJSONObject("resource");
        String nonce = resource.getString("nonce");
        String associated_data = resource.getString("associated_data");
        String ciphertext = resource.getString("ciphertext");
        // 解密ciphertext
        String decryptCiphertext = WxPayUtil.decryptResponseBody(associated_data,nonce,ciphertext,dbWxpay);
        System.out.println("decryptCiphertext:"+decryptCiphertext);
        JSONObject jsonCiphertext = JSONObject.parseObject(decryptCiphertext);
        System.out.println("jsonCiphertext:"+jsonCiphertext);
        // 更新学生的优惠券状态
        updateStuCouponByCouponId(jsonCiphertext.getString("coupon_id"),jsonCiphertext.getString("status"));
        return NotifyResult.create().success();
    }

    /**
     * 更新学生优惠券信息
     */
    private void updateStuCouponByCouponId(String couponId,String status){
        // 根据优惠券编号更新学生的优惠券信息
       eduStudentService.updateStuCouponByCouponId(couponId,status);

    }

}
