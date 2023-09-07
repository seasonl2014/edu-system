package cn.xueden.edu.wechat.controller;

import cn.xueden.edu.wechat.utils.NotifyResult;
import cn.xueden.edu.wechat.utils.WxPayUtil;
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

    /**
     * 代金券消息回调通知
     * @param request
     * @return
     */
    @RequestMapping("/callback")
    public NotifyResult wxVipCallback(HttpServletRequest request){
        //获取报文
        String requestBody= WxPayUtil.getRequestBody(request);
        System.out.println("代金券返回通知："+requestBody);
        return NotifyResult.create().success();
    }

}
