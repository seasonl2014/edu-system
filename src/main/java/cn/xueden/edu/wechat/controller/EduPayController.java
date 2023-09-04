package cn.xueden.edu.wechat.controller;



import cn.xueden.edu.domain.*;
import cn.xueden.edu.service.*;


import cn.xueden.edu.wechat.utils.NotifyResult;
import cn.xueden.edu.wechat.utils.WxPayUtil;
import cn.xueden.websocket.WebSocketServer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

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


    private final IEduStudentBuyVipService studentBuyVipService;

    private final IEduStudentBuyCourseService studentBuyCourseService;

    private final IEduDealMoneyService eduDealMoneyService;

    private final IEduCourseService eduCourseService;

    private final IEduTeacherIncomeDetailsService eduTeacherIncomeDetailsService;

    private final IEduWxpayService eduWxpayService;


    public EduPayController(IEduStudentBuyVipService studentBuyVipService, IEduStudentBuyCourseService studentBuyCourseService, IEduDealMoneyService eduDealMoneyService, IEduCourseService eduCourseService, IEduTeacherIncomeDetailsService eduTeacherIncomeDetailsService, IEduWxpayService eduWxpayService) {
        this.studentBuyVipService = studentBuyVipService;
        this.studentBuyCourseService = studentBuyCourseService;
        this.eduDealMoneyService = eduDealMoneyService;
        this.eduCourseService = eduCourseService;
        this.eduTeacherIncomeDetailsService = eduTeacherIncomeDetailsService;
        this.eduWxpayService = eduWxpayService;
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
        // 从notification中获取解密报文
        Transaction transaction = WxPayUtil.verificationAndDecryption(request,dbWxpay);
        if(transaction==null){
            return NotifyResult.create().fail();
        }
        log.info("购买VIP返回值{}",transaction.getTradeState().name());
        // 支付成功
        if("SUCCESS".equals(transaction.getTradeState().name())){
            String Out_trade_no = transaction.getOutTradeNo();;
            if(Out_trade_no!=null){
                EduStudentBuyVip pay=studentBuyVipService.getOrderInfo(Out_trade_no);
                if(pay!=null&&pay.getIsPayment()!=0){
                    return NotifyResult.create().success();
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
                    eduDealMoney.setPrice(pay.getPrice());
                    eduDealMoney.setCreateBy(pay.getStudentId());
                    eduDealMoney.setUpdateBy(pay.getStudentId());
                    eduDealMoney.setRemarks(pay.getRemarks());
                    eduDealMoney.setPayChannel(pay.getPayChannel());
                    eduDealMoneyService.save(eduDealMoney);
                }

                //更新状态
                pay.setIsPayment(1);//已付款
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
                    updateStudentBuyCourse(Out_trade_no);
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

}
