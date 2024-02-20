package cn.xueden.edu.wechat.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduWxpayRepository;

import cn.xueden.edu.wechat.constant.WxConstants;
import cn.xueden.edu.wechat.dto.TemplateMessageDto;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**功能描述：微信公众平台业务
 * @author:梁志杰
 * @date:2023/9/9
 * @description:cn.xueden.edu.wechat.service
 * @version:1.0
 */
@Service
public class WeChatService {

    private final EduWxpayRepository eduWxpayRepository;

    private static EduWxpay dbEduWxpay;

    public WeChatService(EduWxpayRepository eduWxpayRepository) {
        this.eduWxpayRepository = eduWxpayRepository;
    }

    /**
     * 获取access_token
     * @return
     */
    public String getAccessToken(){
        // 获取微信支付配置信息
        if(dbEduWxpay == null){
            dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        }
        //拼接请求地址
        StringBuffer buffer = new StringBuffer();
        buffer.append("https://api.weixin.qq.com/cgi-bin/token");
        buffer.append("?grant_type=client_credential");
        buffer.append("&appid=%s");
        buffer.append("&secret=%s");
        //设置路径参数
        String url = String.format(buffer.toString(),
                dbEduWxpay.getAppId(),
                dbEduWxpay.getAppSecret());
        //get请求
        try {
            String tokenString = HttpUtil.get(url);
            //获取access_token
            JSONObject jsonObject = JSONObject.parseObject(tokenString);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据微信用户openid获取微信用户信息
     * @param openid
     * @return
     */
    public JSONObject getWxUserInfo(String openid){
        try {

            //获取access_token
            String access_token = getAccessToken();
            if(access_token==null){
                throw new BadRequestException("获取access_token错误");
            }

            // 获取微信用户信息
            //拼接请求地址
            StringBuffer wxUserInfoBuffer = new StringBuffer();
            wxUserInfoBuffer.append("https://api.weixin.qq.com/cgi-bin/user/info");
            wxUserInfoBuffer.append("?access_token="+access_token);
            wxUserInfoBuffer.append("&lang=zh_CN");
            wxUserInfoBuffer.append("&openid=%s");

            String wxUserInfoUrl = String.format(wxUserInfoBuffer.toString(),
                    openid);
            String wxUserInfoString = HttpUtil.get(wxUserInfoUrl);
            //获取微信用户信息
            JSONObject wxUserJsonObject = JSONObject.parseObject(wxUserInfoString);
            return wxUserJsonObject;
        }catch (Exception e){
           e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成带参数的公众号二维码
     * @param studentId 学员ID
     * @return
     */
    public String getQrcode(Long studentId){
        try {

            String access_token = getAccessToken();
            if(access_token==null){
                return null;
            }

            // 获取ticket
            StringBuffer ticketBuffer = new StringBuffer();
            ticketBuffer.append("https://api.weixin.qq.com/cgi-bin/qrcode/create");
            ticketBuffer.append("?access_token="+access_token);
            String ticketUrl = String.format(ticketBuffer.toString());
            // 请求参数

            JsonObject data = new JsonObject();
            data.addProperty("action_name", "QR_SCENE");
            data.addProperty("expire_seconds", 120);
            JsonObject scene = new JsonObject();
            scene.addProperty("scene_id", studentId);

            JsonObject actionInfo = new JsonObject();
            actionInfo.add("scene", scene);
            data.add("action_info", actionInfo);

            String ticketString = HttpUtil.post(ticketUrl,data.toString());
            JSONObject jsonObjectTicket = JSONObject.parseObject(ticketString);
            String ticket = jsonObjectTicket.getString("ticket");
            String qrcodeurl = jsonObjectTicket.getString("url");
            return qrcodeurl;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * 发送模板消息
     * @param couponId
     * @param storkId
     * @param openId
     * @return
     */
    public JSONObject sendMsg(String couponId,String storkId, String openId,String availableBeginTime,String availableEndTime) throws ParseException {
        String access_token = getAccessToken();
        if(access_token==null){
            return null;
        }

        String requestUrl = " https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="  + access_token;

        String templateId = "qrU-CDEgSf5ejgQDJuM_VgPU5taZNDrT-tcwCpDx1yM";
        TemplateMessageDto templateMessageDto = new TemplateMessageDto();
        templateMessageDto.setTemplateId(templateId);
        templateMessageDto.setOpenid(openId);
        templateMessageDto.setUrl("http://www.xueden.cn/");
        Map<String, String> dataMap = new HashMap<String, String>();
        // 优惠券编号
        dataMap.put("character_string8", couponId);
        // 领取数量
        dataMap.put("character_string10", "1");
        // 名称
        dataMap.put("thing1", "领取成功,所属批次："+storkId);
        // 生效时间
        dataMap.put("time9", DateUtil.changeTStringTOStr(availableBeginTime));
        // 到期时间
        dataMap.put("time6", DateUtil.changeTStringTOStr(availableEndTime));
        templateMessageDto.setDataMap(dataMap);
        String messageDto = templateMessageDto.toString();
        String resp = HttpUtil.post(requestUrl,messageDto);
        JSONObject jsonObjectMsg = JSONObject.parseObject(resp);
        System.out.println("发送消息:" + jsonObjectMsg);
        return jsonObjectMsg;
    }

}
