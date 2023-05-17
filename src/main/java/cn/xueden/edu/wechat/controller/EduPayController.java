package cn.xueden.edu.wechat.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.service.IEduStudentBuyVipService;
import cn.xueden.edu.wechat.config.WechatConfig;
import cn.xueden.edu.wechat.dto.WxCiphertextDto;
import cn.xueden.edu.wechat.dto.WxResultDto;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

/**功能描述：支付回调前端控制器
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/edu/pay")
@Slf4j
public class EduPayController {


    private final WechatConfig wechatConfig;


    private final IEduStudentBuyVipService studentBuyVipService;

    public EduPayController(WechatConfig wechatConfig, IEduStudentBuyVipService studentBuyVipService) {
        this.wechatConfig = wechatConfig;
        this.studentBuyVipService = studentBuyVipService;
    }

    /**
     * 购买VIP微信支付返回通知
     * 通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m
     */
    @RequestMapping("/wxVipCallback")
    public Map<String,Object> wxVipCallback(@RequestBody WxResultDto wxResultDto){
        log.info("购买VIP微信支付开始返回通知");
        Map<String,Object> returnMap = new HashMap<>();
        try {
            if (Security.getProvider("BC") == null) {
                Security.addProvider(new BouncyCastleProvider());
            }
            log.info("algorithm",wxResultDto.getResource().getAlgorithm());
            log.info("nonce",wxResultDto.getResource().getNonce());
            log.info("associated_data",wxResultDto.getResource().getAssociated_data());

            String nonce = wxResultDto.getResource().getNonce();
            String associatedData = wxResultDto.getResource().getAssociated_data();
            String ciphertext = wxResultDto.getResource().getCiphertext();
            //使用key、nonce和associated_data，对数据密文resource.ciphertext进行解密，得到JSON形式的资源对象
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
            SecretKeySpec key = new SecretKeySpec(wechatConfig.getAPIv3key().getBytes(StandardCharsets.UTF_8), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(StandardCharsets.UTF_8));

            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(StandardCharsets.UTF_8));

            byte[] bytes;
            try {
                bytes = cipher.doFinal( Base64Utils.decodeFromString(ciphertext));
                String rt = new String(bytes, StandardCharsets.UTF_8);
                WxCiphertextDto wxCiphertextDto= JSON.parseObject(rt, WxCiphertextDto.class);
                if(wxCiphertextDto!=null&& wxCiphertextDto.getTrade_state().equals("SUCCESS")){ // 支付成功
                    String Out_trade_no = wxCiphertextDto.getOut_trade_no();
                    if(Out_trade_no!=null){
                        EduStudentBuyVip pay=studentBuyVipService.getOrderInfo(Out_trade_no);
                        if(pay!=null&&pay.getIsPayment()!=0){
                            returnMap.put("code","SUCCESS");
                            returnMap.put("message","成功");
                            return returnMap;
                        }

                        //获取成交金额记录
                     /*   EduDealMoney eduDealMoney = dealMoneyService.getByOrderNumber(Out_trade_no);
                        // 插入一条记录
                        if(eduDealMoney==null){
                            eduDealMoney = new EduDealMoney();
                            eduDealMoney.setOrderNo(Out_trade_no);
                            eduDealMoney.setCountry(pay.getCountry());
                            eduDealMoney.setArea(pay.getArea());
                            eduDealMoney.setProvince(pay.getProvince());
                            eduDealMoney.setCity(pay.getCity());
                            eduDealMoney.setIsp(pay.getIsp());
                            eduDealMoney.setBuyType(pay.getVipId());
                            eduDealMoney.setMemberId(pay.getMemberId());
                            eduDealMoney.setPrice(pay.getPrice());
                            eduDealMoney.setUpdateId(pay.getMemberId());
                            eduDealMoney.setCreateId(pay.getMemberId());
                            eduDealMoney.setRemarks(pay.getRemarks());
                            eduDealMoney.setPayChannel(pay.getPayChannel());
                            dealMoneyService.save(eduDealMoney);
                        }*/

                        //更新状态
                        pay.setIsPayment(1);//已付款
                        studentBuyVipService.updatePayment(pay);

                        try {
                            Map<String, Object> map = new HashMap<String, Object>();
                            ObjectMapper mapper = new ObjectMapper();
                            map.put("status", 1);
                            map.put("msg", "支付成功,恭喜您成为网站的会员！");
                            String json = mapper.writeValueAsString(map);
                            // WebSocketServer.sendInfo(json,Out_trade_no);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }

                    }

                }
                log.info("out_trade_no微信支付返回处理结果："+rt);
            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }


        }catch (Exception e) {
            log.info("微信支付返回通知，失败处理结束");
            log.error(e.getMessage(),e);
            returnMap.put("code","ERROR");
            returnMap.put("message","失败");
            return returnMap;
        }
        log.info("微信支付结束返回通知");
        returnMap.put("code","SUCCESS");
        returnMap.put("message","成功");
        return returnMap;
    }

}
