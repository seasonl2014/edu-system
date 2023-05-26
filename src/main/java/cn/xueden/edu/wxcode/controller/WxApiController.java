package cn.xueden.edu.wxcode.controller;

import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.wxcode.WechatCodeConfig;
import cn.xueden.edu.wxcode.utils.WeChatHttpUtils;

import cn.xueden.utils.IpInfo;
import cn.xueden.utils.JWTUtil;
import cn.xueden.utils.Md5Util;

import cn.xueden.utils.XuedenUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**功能描述：微信扫码登录前端控制器
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wxcode.controller
 * @version:1.0
 */
@Controller
@Slf4j
@RequestMapping("wechat")
public class WxApiController {

    private final WechatCodeConfig wechatConfig;

    private final IEduStudentService eduStudentService;

    public WxApiController(WechatCodeConfig wechatConfig, IEduStudentService eduStudentService) {
        this.wechatConfig = wechatConfig;
        this.eduStudentService = eduStudentService;
    }

    //1、生成微信二维码
    @GetMapping("login")
    public String getWxCode() {
        //固定地址，拼接参数
        //微信开放平台授权baseUrl  固定格式
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";

        //对redirect_url进行URLEncoder编码
        String redirectUrl = wechatConfig.getRedirectUri();

        try {
            //对URL进行utf-8的编码
            URLEncoder.encode(redirectUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = String.format( //向指定字符串中按顺序替换%s
                baseUrl,
                wechatConfig.getAppId(),
                wechatConfig.getRedirectUri(),
                "Xueden" //自定义（随意设置）
        );


        //请求微信地址
        return "redirect:" + url;
    }

    //2、扫描人信息，添加数据
    @GetMapping("callback")
    //1、获取code值，临时票据、类似于验证码(该数据为扫码后跳转时微信方传来)
    public String callback(String code,String state, Model model,
                           HttpServletRequest request) throws IOException {
        // 获取用户IP地址
        String ipAddress = XuedenUtil.getClientIp(request);
        IpInfo ipInfo = XuedenUtil.getCityInfo(ipAddress);

        //2、拿着code请求微信固定的地址，得到两个值access_token 和 openid
        String baseAccessTokenUrl =
                "https://api.weixin.qq.com/sns/oauth2/access_token" +
                        "?appid=%s" +
                        "&secret=%s" +
                        "&code=%s" +
                        "&grant_type=authorization_code";
        //3、拼接三个参数：id   密钥   code值
        String accessTokenUrl = String.format(
                baseAccessTokenUrl,
                wechatConfig.getAppId(),
                wechatConfig.getAppSecret(),
                code
        );
        HttpGet httpGet = null;
        CloseableHttpResponse response = null;
        URIBuilder uriBuilder = null;
        EduStudent wxMember = new EduStudent();
        try {
            //请求这个拼接好的地址，得到两个值access_token 和 openid
            //使用httpClient发送请求，得到返回结果
            httpGet = new HttpGet(accessTokenUrl);
            response = WeChatHttpUtils.getClient().execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();

            JSONObject jsonObject = JSON.parseObject(EntityUtils.toString(response.getEntity()));

            String access_token = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");
            String unionid = jsonObject.getString("unionid");

            // 根据openid获取学员信息
            EduStudent dbEduStudent = eduStudentService.getByOpenid(openid);
            if(dbEduStudent!=null){
                if(ipInfo!=null){
                    dbEduStudent.setCity(ipInfo.getCity());
                    dbEduStudent.setArea(ipInfo.getCountry());
                    dbEduStudent.setProvince(ipInfo.getProvince());
                    dbEduStudent.setIsp(ipInfo.getIsp());
                }
                // 更改登录次数
                dbEduStudent.setLoginTimes(dbEduStudent.getLoginTimes()==null?1:dbEduStudent.getLoginTimes()+1);
                eduStudentService.editStudent(dbEduStudent);
                // 生成登录token
                Map<String,String> map = new HashMap<>();
                map.put("studentId",dbEduStudent.getId().toString());
                String studentToken = JWTUtil.getToken(map);
                dbEduStudent.setStudentToken(studentToken);
                model.addAttribute("dbEduStudent",dbEduStudent);
                model.addAttribute("frontUrl", wechatConfig.getFrontUrl());
                return "result";
            }


            // 获取扫码人信息
            uriBuilder = new URIBuilder("https://api.weixin.qq.com/sns/userinfo");
            uriBuilder.setParameter("access_token",access_token);
            uriBuilder.setParameter("openid",openid);
            uriBuilder.setParameter("lang","zh_CN");
            httpGet.setHeader("Accept", "application/json");
            httpGet.addHeader("Content-type","application/json; charset=utf-8");
            httpGet = new HttpGet(uriBuilder.build());
            response = WeChatHttpUtils.getClient().execute(httpGet);
            JSONObject jsonUserinfo = JSON.parseObject(EntityUtils.toString(response.getEntity()));
            log.info("access_token{},openid{},unionid{},获取信息{}",access_token, openid,unionid,jsonUserinfo);
            if(ipInfo!=null){
                wxMember.setCity(ipInfo.getCity());
                wxMember.setArea(ipInfo.getCountry());
                wxMember.setProvince(ipInfo.getProvince());
                wxMember.setIsp(ipInfo.getIsp());
            }

            wxMember.setStudentIcon(jsonUserinfo.getString("headimgurl"));
            String nickname = new String(jsonUserinfo.getString("nickname").getBytes("ISO-8859-1"), "UTF-8");
            wxMember.setName(nickname);
            wxMember.setLoginName(nickname);
            wxMember.setWxOpenId(openid);
            wxMember.setUnionId(unionid);
            wxMember.setSex(jsonUserinfo.getString("sex"));
            wxMember.setLoginTimes(1);
            wxMember.setRemarks("微信扫码注册");
            wxMember.setPassword(Md5Util.Md5("123456"));
            // 获取学员是否判断微信扫码登录
            eduStudentService.addStudent(wxMember);
        } catch (Exception e) {
            e.printStackTrace();

        }
        // 生成登录token
        Map<String,String> wxMap = new HashMap<>();
        wxMap.put("studentId",wxMember.getId().toString());
        String studentToken = JWTUtil.getToken(wxMap);
        wxMember.setStudentToken(studentToken);
        model.addAttribute("dbEduStudent",wxMember);
        model.addAttribute("frontUrl", wechatConfig.getFrontUrl());
        return "result";
    }
}
