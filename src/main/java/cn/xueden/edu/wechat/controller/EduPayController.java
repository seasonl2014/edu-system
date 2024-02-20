package cn.xueden.edu.wechat.controller;



import cn.xueden.edu.domain.*;
import cn.xueden.edu.service.*;


import cn.xueden.edu.wechat.utils.NotifyResult;
import cn.xueden.edu.wechat.utils.WxPayUtil;
import cn.xueden.websocket.WebSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.wechat.pay.java.service.payments.model.PromotionDetail;
import com.wechat.pay.java.service.payments.model.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    private final IEduStudentBuyVipService studentBuyVipService;

    private final IEduStudentBuyCourseService studentBuyCourseService;

    private final IEduDealMoneyService eduDealMoneyService;

    private final IEduCourseService eduCourseService;

    private final IEduTeacherIncomeDetailsService eduTeacherIncomeDetailsService;

    private final IEduWxpayService eduWxpayService;

    private final IEduStudentService eduStudentService;


    public EduPayController(IEduStudentBuyVipService studentBuyVipService, IEduStudentBuyCourseService studentBuyCourseService, IEduDealMoneyService eduDealMoneyService, IEduCourseService eduCourseService, IEduTeacherIncomeDetailsService eduTeacherIncomeDetailsService, IEduWxpayService eduWxpayService, IEduStudentService eduStudentService) {
        this.studentBuyVipService = studentBuyVipService;
        this.studentBuyCourseService = studentBuyCourseService;
        this.eduDealMoneyService = eduDealMoneyService;
        this.eduCourseService = eduCourseService;
        this.eduTeacherIncomeDetailsService = eduTeacherIncomeDetailsService;
        this.eduWxpayService = eduWxpayService;
        this.eduStudentService = eduStudentService;
    }

    /**
     * 购买VIP微信支付返回通知
     * 通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m
     */
    @RequestMapping("/wxVipCallback")
    public NotifyResult wxVipCallback(HttpServletRequest request){
        log.info("购买VIP微信支付开始返回通知");
        // 获取微信支付信息
        EduWxpay dbWxpay =  eduWxpayService.getOne();
        // 从Transaction中获取解密报文
        Transaction transaction = WxPayUtil.verificationAndDecryption(request,dbWxpay);
        if(transaction==null){
            return NotifyResult.create().fail();
        }
        log.info("购买VIP返回值{}",transaction.getTradeState().name());
        // 支付成功
        if("SUCCESS".equals(transaction.getTradeState().name())){
            // 学灯网平台订单编号
            String Out_trade_no = transaction.getOutTradeNo();
            // 微信支付内部生成的订单号
            String transactionId = transaction.getTransactionId();
            if(Out_trade_no!=null){
                EduStudentBuyVip pay=studentBuyVipService.getOrderInfo(Out_trade_no);
                if(pay!=null&&pay.getIsPayment()!=0){
                    return NotifyResult.create().success();
                }
                // 判断是否使用代金券购买
                List<PromotionDetail> promotionDetailList = transaction.getPromotionDetail();
                // 代金券金额(默认单位是分)
                int couponAmount = 0;
                // 代金券编号
                String strCouponId = null;
                // 代金券批次号
                String strStockId = null;
                // 使用代金券
                if (promotionDetailList.size()>0){
                    couponAmount = promotionDetailList.stream().mapToInt(PromotionDetail::getAmount).sum();
                    strCouponId = promotionDetailList.stream().map(o-> (String)"'"+o.getCouponId()+"'").collect(Collectors.joining(","));
                    strStockId = promotionDetailList.stream().map(o-> (String)"'"+o.getStockId()+"'").collect(Collectors.joining(","));
                }

                //获取成交金额记录
                EduDealMoney eduDealMoney = eduDealMoneyService.getByOrderNumber(Out_trade_no);
                // 插入一条记录
                if(eduDealMoney==null){
                    eduDealMoney = new EduDealMoney();
                    eduDealMoney.setOrderNo(Out_trade_no);
                    eduDealMoney.setArea(pay.getArea());
                    eduDealMoney.setProvince(pay.getProvince());
                    eduDealMoney.setCity(pay.getCity());
                    eduDealMoney.setIsp(pay.getIsp());
                    eduDealMoney.setBuyType(1);
                    eduDealMoney.setStudentId(pay.getStudentId());
                    // 实际收入需要减去代金券金额
                    if (couponAmount>0){
                        // 先转换成元
                        int tempAmount = couponAmount/100;
                        BigDecimal tempAmountBigDecimal = new BigDecimal(tempAmount);
                        eduDealMoney.setPrice(pay.getPrice().subtract(tempAmountBigDecimal));
                    }else {
                        eduDealMoney.setPrice(pay.getPrice());
                    }

                    eduDealMoney.setCreateBy(pay.getStudentId());
                    eduDealMoney.setUpdateBy(pay.getStudentId());
                    eduDealMoney.setRemarks(pay.getRemarks());
                    eduDealMoney.setPayChannel(pay.getPayChannel());
                    eduDealMoneyService.save(eduDealMoney);
                }

                //更新状态，已付款
                pay.setIsPayment(1);
                // 微信支付订单编号
                pay.setTransactionId(transactionId);
                pay.setCouponId(strCouponId);
                pay.setStockId(strStockId);
                studentBuyVipService.updatePayment(pay);
                try {
                    Map<String, Object> map = new HashMap<String, Object>();
                    ObjectMapper mapper = new ObjectMapper();
                    map.put("status", 1);
                    map.put("msg", "支付成功,恭喜您成为网站的会员！");
                    String json = mapper.writeValueAsString(map);
                    WebSocketServer.sendInfo(json,Out_trade_no);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }
        log.info("购买VIP会员微信支付返回处理结果："+transaction.getTradeStateDesc());
        return NotifyResult.create().success();
    }

    /**
     * 购买课程微信支付返回通知
     * 通知频率为15s/15s/30s/3m/10m/20m/30m/30m/30m/60m/3h/3h/3h/6h/6h - 总计 24h4m
     */
    @RequestMapping("/wxCourseCallback")
    public NotifyResult wxCallback(HttpServletRequest request){
            log.info("购买课程微信支付开始返回通知");
            // 获取微信支付信息
            EduWxpay dbWxpay =  eduWxpayService.getOne();
            // 从transaction中获取解密报文
            Transaction transaction = WxPayUtil.verificationAndDecryption(request,dbWxpay);
            if(transaction==null){
                return NotifyResult.create().fail();
            }
            // 支付成功
            if("SUCCESS".equals(transaction.getTradeState().name())){
                String Out_trade_no = transaction.getOutTradeNo();
                if(Out_trade_no!=null){
                    // 购买课程成功后，处理相关业务
                    updateStudentBuyCourse(Out_trade_no);
                    // 获取购买者微信公众平台下的openid
                    getSpOpenId(transaction);
                }

            }
        log.info("购买课程微信支付返回处理结果：{}",transaction.getTradeStateDesc());
        return NotifyResult.create().success();
    }

    /**
     * 购买课程成功后，处理相关业务
     * @param orderNo
     */
    private void updateStudentBuyCourse(String orderNo){
        EduStudentBuyCourse pay=studentBuyCourseService.getByOrderNumber(orderNo);
        // 判断订单状态是否付款
        if(pay!=null&&pay.getIsPayment()==0){
            //更新状态 已付款
            pay.setIsPayment(1);
            studentBuyCourseService.updatePayment(pay);
            // 处理成交金额业务
            addEduDealMoneyByBuyCourse(orderNo,pay);
            //更新课程购买数量
            updateBuyCount(pay.getCourseId());
            // 计算讲师收益
            teacherIncome(orderNo);
        }

        try {
            Map<String, Object> map = new HashMap<String, Object>();
            ObjectMapper mapper = new ObjectMapper();
            map.put("status", 1);
            map.put("msg", "支付成功,课程资料请到课程详情页面下载！");
            String json = mapper.writeValueAsString(map);
            WebSocketServer.sendInfo(json,orderNo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 购买课程成功后处理成交金额汇总数据
     * @param Out_trade_no
     */
    private void addEduDealMoneyByBuyCourse(String Out_trade_no,EduStudentBuyCourse pay){
        //获取成交金额记录
        EduDealMoney eduDealMoney = eduDealMoneyService.getByOrderNumber(Out_trade_no);
        // 插入一条记录
        if(eduDealMoney==null){
            eduDealMoney = new EduDealMoney();
            eduDealMoney.setOrderNo(Out_trade_no);
            eduDealMoney.setArea(pay.getArea());
            eduDealMoney.setProvince(pay.getProvince());
            eduDealMoney.setCity(pay.getCity());
            eduDealMoney.setIsp(pay.getIsp());
            eduDealMoney.setBuyType(0);
            eduDealMoney.setStudentId(pay.getStudentId());
            eduDealMoney.setPrice(pay.getPrice());
            eduDealMoney.setCreateBy(pay.getStudentId());
            eduDealMoney.setUpdateBy(pay.getStudentId());
            eduDealMoney.setRemarks(pay.getRemarks());
            eduDealMoney.setPayChannel(pay.getPayChannel());
            eduDealMoneyService.save(eduDealMoney);

        }
    }

    /**
     * 更新课程购买数量
     * @param courseId
     */
    private void updateBuyCount(Long courseId){
       // 根据课程ID更新课程购买数量
        eduCourseService.updateBuyCount(courseId);
    }

    /**
     * 计算讲师收益
     * @param orderNo
     */
    private void teacherIncome(String orderNo){
        eduTeacherIncomeDetailsService.teacherIncome(orderNo);
    }

    /**
     * 获取微信公众平台下的用户openid
     * @param transaction
     */
    private void getSpOpenId(Transaction transaction){
        // 获取订单详情
        EduStudentBuyCourse pay=studentBuyCourseService.getByOrderNumber(transaction.getOutTradeNo());
        EduStudent dbEduStudent = eduStudentService.getById(pay.getStudentId());
        if(dbEduStudent!=null&&dbEduStudent.getSpOpenid()==null){
            dbEduStudent.setSpOpenid(transaction.getPayer().getOpenid());
            eduStudentService.editStudent(dbEduStudent);
        }

    }

}
