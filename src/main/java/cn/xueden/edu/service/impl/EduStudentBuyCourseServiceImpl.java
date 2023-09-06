package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.*;
import cn.xueden.edu.repository.*;
import cn.xueden.edu.service.IEduStudentBuyCourseService;

import cn.xueden.edu.service.dto.EduOrderCourseQueryCriteria;
import cn.xueden.edu.vo.RefundOrderCourseModel;
import cn.xueden.edu.vo.StudentBuyCourseModel;
import cn.xueden.edu.wechat.dto.AmountDto;
import cn.xueden.edu.wechat.dto.WxOrderDto;
import cn.xueden.edu.wechat.service.WxPayService;
import cn.xueden.exception.BadRequestException;

import cn.xueden.utils.*;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wechat.pay.java.service.refund.model.Refund;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

/**功能描述：学员购买课程业务接口实现类
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduStudentBuyCourseServiceImpl implements IEduStudentBuyCourseService {

    private final EduStudentBuyCourseRepository eduStudentBuyCourseRepository;

    private final EduCourseRepository eduCourseRepository;

    private final EduWxpayRepository eduWxpayRepository;

    private final WxPayService wxPayService;

    private final EduStudentRepository eduStudentRepository;

    private final EduRefundRepository eduRefundRepository;

    public EduStudentBuyCourseServiceImpl(EduStudentBuyCourseRepository eduStudentBuyCourseRepository, EduCourseRepository eduCourseRepository, EduWxpayRepository eduWxpayRepository, WxPayService wxPayService, EduStudentRepository eduStudentRepository, EduRefundRepository eduRefundRepository) {
        this.eduStudentBuyCourseRepository = eduStudentBuyCourseRepository;
        this.eduCourseRepository = eduCourseRepository;
        this.eduWxpayRepository = eduWxpayRepository;
        this.wxPayService = wxPayService;
        this.eduStudentRepository = eduStudentRepository;
        this.eduRefundRepository = eduRefundRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public EduStudentBuyCourse buy(Long id, String token, String ipAddress) {
        EduStudentBuyCourse tempEduStudentBuyCourse = new EduStudentBuyCourse();
        // 根据IP地址获取地理位置
        try {
            if("0.0.0.0".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress) || "localhost".equals(ipAddress) || "127.0.0.1".equals(ipAddress)){
                ipAddress = "127.0.0.1";
            }
            if(!"127.0.0.1".equals(ipAddress)){
                IpInfo ipInfo = XuedenUtil.getCityInfo(ipAddress);
                if(null!=ipInfo){
                    tempEduStudentBuyCourse.setArea(ipInfo.getRegion());
                    tempEduStudentBuyCourse.setProvince(ipInfo.getProvince());
                    tempEduStudentBuyCourse.setCity(ipInfo.getCity());
                    tempEduStudentBuyCourse.setIsp(ipInfo.getIsp());
                }
            }
            // 获取学员信息
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            String studentId= decodedJWT.getClaim("studentId").asString();
            String phone= decodedJWT.getClaim("phone").asString();
            if(null==studentId){
                throw new BadRequestException("购买失败，请先登录！");
            }else {
                 Long dbStudentId = Long.parseLong(studentId);
                // 判断学员是否已经购买过该课程
                EduStudentBuyCourse dbEduStudentBuyCourse = eduStudentBuyCourseRepository.findByStudentIdAndCourseId(dbStudentId,id);
                if(dbEduStudentBuyCourse!=null){
                    // 待付款
                    if(dbEduStudentBuyCourse.getIsPayment()==0){
                        // 未付款，重新生成订单编号
                        String orderNo= XuedenUtil.createOrderNumber();
                        dbEduStudentBuyCourse.setOrderNo(orderNo);
                        eduStudentBuyCourseRepository.save(dbEduStudentBuyCourse);
                        return dbEduStudentBuyCourse;
                    }else {
                        throw new BadRequestException("您已经购买过了，无需重复购买！");
                    }
                    // 未下过单
                }else {
                    // 根据id获取课程信息
                    EduCourse dhEduCourse = eduCourseRepository.getReferenceById(id);
                    // 生成订单编号
                    String orderNo= XuedenUtil.createOrderNumber();
                    tempEduStudentBuyCourse.setCourseId(id);
                    tempEduStudentBuyCourse.setPrice(dhEduCourse.getPrice());
                    tempEduStudentBuyCourse.setIsPayment(0);
                    tempEduStudentBuyCourse.setStudentId(dbStudentId);
                    tempEduStudentBuyCourse.setOrderNo(orderNo);
                    tempEduStudentBuyCourse.setCreateBy(dbStudentId);
                    tempEduStudentBuyCourse.setUpdateBy(dbStudentId);
                    tempEduStudentBuyCourse.setTeacherId(dhEduCourse.getTeacherId());
                    tempEduStudentBuyCourse.setRemarks("学员购买课程【"+dhEduCourse.getShortTitle()+"】");
                    eduStudentBuyCourseRepository.save(tempEduStudentBuyCourse);
                }

            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return tempEduStudentBuyCourse;
    }

    /**
     * 根据订单编号获取订单信息
     * @param orderNo
     * @return
     */
    @Override
    public EduStudentBuyCourse getByOrderNumber(String orderNo) {
        return eduStudentBuyCourseRepository.getByOrderNo(orderNo);
    }

    /**
     * 购买课程立即付款
     * @param orderNo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Object pay(String orderNo) {

        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        // 获取订单详情
        EduStudentBuyCourse eduStudentBuyCourse = eduStudentBuyCourseRepository.getByOrderNo(orderNo);
        // 生成付款链接
        AmountDto amount = new AmountDto();
        BigDecimal num1 = new BigDecimal(100);
        BigDecimal result3 = num1.multiply(eduStudentBuyCourse.getPrice());
        // 单位是分
        amount.setTotal(result3.intValue());
        WxOrderDto wxOrderDto = new WxOrderDto();
        wxOrderDto.setAmount(amount);
        wxOrderDto.setOut_trade_no(""+orderNo+"");

        wxOrderDto.setDescription(eduStudentBuyCourse.getRemarks());
        wxOrderDto.setNotify_url(dbEduWxpay.getNotifyCourseUrl());
        try {
            String code=wxPayService.CreateNativeOrder(wxOrderDto,dbEduWxpay);
            return code;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 更新购买状态
     * @param pay
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updatePayment(EduStudentBuyCourse pay) {
        eduStudentBuyCourseRepository.save(pay);
    }

    /**
     * 根据课程ID和学员ID查找记录
     * @param courseId
     * @param studentId
     * @return
     */
    @Override
    public EduStudentBuyCourse findByCourseIdAndStudentId(Long courseId, Long studentId) {
        return eduStudentBuyCourseRepository.findByCourseIdAndStudentId(courseId,studentId);
    }

    /**
     * 获取课程订单明细列表数据
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduOrderCourseQueryCriteria queryCriteria,Pageable pageable) {
        Page<EduStudentBuyCourse> page = eduStudentBuyCourseRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 根据id获取课程订单详情信息
     * @return
     */
    @Override
    public StudentBuyCourseModel getById(Long id) {
        StudentBuyCourseModel tempStudentBuyCourseModel = new StudentBuyCourseModel();
        // 课程订单详情
        EduStudentBuyCourse dbEduStudentBuyCourse = eduStudentBuyCourseRepository.getReferenceById(id);
        // 课程详情
        EduCourse dbEduCourse = eduCourseRepository.getReferenceById(dbEduStudentBuyCourse.getCourseId());
        // 购买者详情
        EduStudent dbEduStudent = eduStudentRepository.getReferenceById(dbEduStudentBuyCourse.getStudentId());

        // 订单ID
        tempStudentBuyCourseModel.setOrderId(dbEduStudentBuyCourse.getId());
        // 订单编号
        tempStudentBuyCourseModel.setOrderNo(dbEduStudentBuyCourse.getOrderNo());
        // 订单金额
        tempStudentBuyCourseModel.setPrice(dbEduStudentBuyCourse.getPrice());
        // 下单时间
        tempStudentBuyCourseModel.setCreateTime(dbEduStudentBuyCourse.getCreateTime());
        // 省份
        tempStudentBuyCourseModel.setProvince(dbEduStudentBuyCourse.getProvince());
        // 城市
        tempStudentBuyCourseModel.setCity(dbEduStudentBuyCourse.getCity());
        // 课程名称
        tempStudentBuyCourseModel.setCourseName(dbEduCourse.getTitle());
        // 课程原价
        tempStudentBuyCourseModel.setOriginalPrice(dbEduCourse.getOriginalPrice());
        // 学员编号
        tempStudentBuyCourseModel.setStudentNo(dbEduStudent.getStuNo());
        return tempStudentBuyCourseModel;
    }

    /**
     * 课程订单退款
     * @param refundOrderCourseModel
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refundCourseOrder(RefundOrderCourseModel refundOrderCourseModel) {
        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        // 课程订单详情
        EduStudentBuyCourse dbEduStudentBuyCourse = eduStudentBuyCourseRepository.getReferenceById(refundOrderCourseModel.getOrderId());

        // 退款比例
        BigDecimal bigDecimal = new BigDecimal(0.85);
        // 转换成分
        BigDecimal bigDecimal1 = new BigDecimal(100);
        // 退款金额 转换成分
        BigDecimal price = dbEduStudentBuyCourse.getPrice().multiply(bigDecimal).multiply(bigDecimal1);
        // 原订单金额 ，转化成分
        BigDecimal oldPrice = dbEduStudentBuyCourse.getPrice().multiply(bigDecimal1);
        // 调用微信退款接口
        Refund refund = wxPayService.refundOrder(dbEduWxpay,dbEduStudentBuyCourse.getOrderNo(),refundOrderCourseModel.getRemarks(),price,oldPrice);
        if(refund==null){
            throw new BadRequestException("退款失败！");
        }else {
            // 修改课程订单状态,2表示已退款
            dbEduStudentBuyCourse.setIsPayment(2);
            eduStudentBuyCourseRepository.save(dbEduStudentBuyCourse);
            // 退款并封禁学员
            if(refundOrderCourseModel.getType()>0){
                EduStudent dbEduStudent = eduStudentRepository.getReferenceById(dbEduStudentBuyCourse.getStudentId());
                dbEduStudent.setStatus(0);
                eduStudentRepository.save(dbEduStudent);
            }
            // 保存数据到退款记录表
            EduRefund tempEduRefund = new EduRefund();
            tempEduRefund.setRefundId(refund.getRefundId());
            tempEduRefund.setOutRefundNo(refund.getOutRefundNo());
            tempEduRefund.setOutTradeNo(refund.getOutTradeNo());
            tempEduRefund.setTransactionId(refund.getTransactionId());
            tempEduRefund.setRefundType(0);
            tempEduRefund.setRefundTotal(refund.getAmount().getRefund());
            tempEduRefund.setStudentId(dbEduStudentBuyCourse.getStudentId());
            eduRefundRepository.save(tempEduRefund);
        }

    }
}
