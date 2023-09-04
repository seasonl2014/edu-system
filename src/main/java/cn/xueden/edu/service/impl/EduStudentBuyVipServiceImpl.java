package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.domain.EduVipType;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduStudentBuyVipRepository;
import cn.xueden.edu.repository.EduStudentRepository;
import cn.xueden.edu.repository.EduVipTypeRepository;
import cn.xueden.edu.repository.EduWxpayRepository;
import cn.xueden.edu.service.IEduStudentBuyVipService;

import cn.xueden.edu.wechat.dto.AmountDto;
import cn.xueden.edu.wechat.dto.WxOrderDto;
import cn.xueden.edu.wechat.service.WxPayService;
import cn.xueden.exception.BadRequestException;

import cn.xueden.utils.IpInfo;
import cn.xueden.utils.JWTUtil;
import cn.xueden.utils.XuedenUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

/**功能描述：学员购买VIP业务接口实现类
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduStudentBuyVipServiceImpl implements IEduStudentBuyVipService {

    private final EduStudentBuyVipRepository eduStudentBuyVipRepository;

    private final EduStudentRepository eduStudentRepository;

    private final EduVipTypeRepository eduVipTypeRepository;

    private final EduWxpayRepository eduWxpayRepository;

    private final WxPayService wxPayService;

    public EduStudentBuyVipServiceImpl(EduStudentBuyVipRepository eduStudentBuyVipRepository, EduStudentRepository eduStudentRepository, EduVipTypeRepository eduVipTypeRepository, EduWxpayRepository eduWxpayRepository, WxPayService wxPayService) {
        this.eduStudentBuyVipRepository = eduStudentBuyVipRepository;
        this.eduStudentRepository = eduStudentRepository;
        this.eduVipTypeRepository = eduVipTypeRepository;
        this.eduWxpayRepository = eduWxpayRepository;
        this.wxPayService = wxPayService;
    }

    /**
     * 购买VIP
     * @param id
     * @param studentToken
     * @param ipAddress
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EduStudentBuyVip buyVip(Long id, String studentToken, String ipAddress) {
        EduStudentBuyVip tempEduStudentBuyVip = new EduStudentBuyVip();
        try {
            if("0.0.0.0".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress) || "localhost".equals(ipAddress) || "127.0.0.1".equals(ipAddress)){
                ipAddress = "127.0.0.1";
            }
            if(!"127.0.0.1".equals(ipAddress)){
                IpInfo ipInfo = XuedenUtil.getCityInfo(ipAddress);
                if(null!=ipInfo){
                    tempEduStudentBuyVip.setArea(ipInfo.getRegion());
                    tempEduStudentBuyVip.setProvince(ipInfo.getProvince());
                    tempEduStudentBuyVip.setCity(ipInfo.getCity());
                    tempEduStudentBuyVip.setIsp(ipInfo.getIsp());
                }
            }

            // 获取学员信息
            DecodedJWT decodedJWT = JWTUtil.verify(studentToken);
            String studentId= decodedJWT.getClaim("studentId").asString();
            EduStudent tempEduStudent = null;
            if(null==studentId){
                throw new BadRequestException("购买失败，请先登录！");
            }else {
                Long dbStudentId = Long.parseLong(studentId);
                // 判断学员是否已经下单了
                EduStudentBuyVip dbEduStudentBuyVip = eduStudentBuyVipRepository.findByStudentId(dbStudentId);
                if(dbEduStudentBuyVip!=null){
                    // 待付款
                    if(dbEduStudentBuyVip.getIsPayment()==0){
                        // 未付款，重新生成订单编号
                        String orderNo= XuedenUtil.createOrderNumber();
                        dbEduStudentBuyVip.setOrderNo(orderNo);
                        eduStudentBuyVipRepository.save(dbEduStudentBuyVip);
                        return dbEduStudentBuyVip;
                    }else {
                        throw new BadRequestException("您已经购买过了，无需重复购买！");
                    }
                // 未下过单
                }else {
                    // 生成订单编号
                    String orderNo= XuedenUtil.createOrderNumber();
                    tempEduStudentBuyVip.setIsPayment(0);
                    tempEduStudentBuyVip.setStudentId(dbStudentId);
                    tempEduStudentBuyVip.setOrderNo(orderNo);
                    tempEduStudentBuyVip.setVipId(id);
                    tempEduStudentBuyVip.setCreateBy(dbStudentId);
                    tempEduStudentBuyVip.setUpdateBy(dbStudentId);

                    // 获取VIP类型的价格
                    EduVipType dbEduVipType = eduVipTypeRepository.getReferenceById(id);

                    tempEduStudentBuyVip.setPrice(BigDecimal.valueOf(dbEduVipType.getVipMoney()));
                    tempEduStudentBuyVip.setPayChannel("wxpay");
                    tempEduStudentBuyVip.setRemarks("用户加入VIP【"+dbEduVipType.getVipName()+"】");
                    eduStudentBuyVipRepository.save(tempEduStudentBuyVip);
                    return tempEduStudentBuyVip;
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 根据订单编号获取订单详情
     * @param orderNo
     * @return
     */
    @Override
    public EduStudentBuyVip getOrderInfo(String orderNo) {
        return eduStudentBuyVipRepository.getByOrderNo(orderNo);
    }

    /**
     * 去支付生成支付二维码链接
     * @param orderNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String payBuy(String orderNo) {
        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        // 根据订单号获取订单信息
        EduStudentBuyVip dbEduStudentBuyVip = eduStudentBuyVipRepository.getByOrderNo(orderNo);
        if(dbEduStudentBuyVip==null){
            throw new BadRequestException("付款失败，请稍候再试！");
        }else {
            // 生成付款链接
            AmountDto amount = new AmountDto();
            BigDecimal num1 = new BigDecimal(100);
            BigDecimal result3 = num1.multiply(dbEduStudentBuyVip.getPrice());
            // 单位是分
            amount.setTotal(result3.intValue());
            WxOrderDto wxOrderDto = new WxOrderDto();
            wxOrderDto.setAmount(amount);
            wxOrderDto.setOut_trade_no(""+orderNo+"");
            wxOrderDto.setDescription("用户购买【vip全站会员】");
            wxOrderDto.setNotify_url(dbEduWxpay.getNotifyVipUrl());
            try {
                String code=wxPayService.CreateNativeOrder(wxOrderDto,dbEduWxpay);
                return code;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePayment(EduStudentBuyVip pay) {
        eduStudentBuyVipRepository.save(pay);
    }

    /**
     * 根据学员ID查询记录
     * @param studentId
     * @return
     */
    @Override
    public EduStudentBuyVip findByStudentId(Long studentId) {
        return eduStudentBuyVipRepository.findByStudentId(studentId);
    }


}
