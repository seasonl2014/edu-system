package cn.xueden.edu.wechat.handle;

import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.wechat.constant.WxConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;

import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**功能描述：处理公众号消息事件
 * @author:梁志杰
 * @date:2023/9/8
 * @description:cn.xueden.edu.wechat.service
 * @version:1.0
 */
@Service
@Slf4j
public class WeChatMessageHandle {

   private final IEduStudentService eduStudentService;

    public WeChatMessageHandle(IEduStudentService eduStudentService) {
        this.eduStudentService = eduStudentService;
    }

    public String handleMessage(HttpServletRequest request) {
        try {
            WxMpXmlMessage wxMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            // 处理消息，例如回复文本消息
            if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.TEXT)) {
                String content = "你发送了文本消息：" + wxMessage.getContent();
                WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();

                // 处理回复关键字事件

                return response.toXml();
            // 处理关注公众号事件,并从学灯网扫码带参数的二维码
            }else if (wxMessage.getEvent().equals(WxConsts.EventType.SUBSCRIBE)&& StringUtils.isNotBlank(wxMessage.getEventKey())){
                // 更新学员的对应公众号的openId，并发送代金券
                eduStudentService.subscribe(wxMessage.getFromUser());
                log.info("用户扫了带参数的二维码并第一次关注执行的事件{}",wxMessage.getEventKey());
                String content = "优惠券已经发放，请登录学灯网，在个人中心我的优惠券查看是否领取成功！";
                WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
                return response.toXml();
            // 处理取消关注公众号事件
            }else if(wxMessage.getEvent().equals(WxConsts.EventType.UNSUBSCRIBE)){
                log.info("用户取消关注执行的事件{}",wxMessage);
            // 处理已经关注过公众号事件
            }else if(wxMessage.getEvent().equals(WxConsts.EventType.SCAN)){
                // 更新学员的对应公众号的openId，并发送代金券
                eduStudentService.subscribe(wxMessage.getFromUser());
                log.info("用户扫了带参数的二维码并已关注执行的事件{}",wxMessage);
                String content = "优惠券已经发放，请登录学灯网，在个人中心我的优惠券查看是否领取成功！";
                WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
                return response.toXml();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    public boolean checkSignature(String signature, String timestamp, String nonce) {
        // Sort the token, timestamp, and nonce alphabetically
        String[] arr = {WxConstants.MP_TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        // Concatenate them
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s);
        }
        String combined = sb.toString();

        // Calculate the SHA-1 hash of the combined string
        String calculatedSignature = sha1(combined);

        // Compare the calculated signature with the received signature
        return calculatedSignature.equals(signature);
    }

    private String sha1(String input) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] bytes = sha1.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : bytes) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
