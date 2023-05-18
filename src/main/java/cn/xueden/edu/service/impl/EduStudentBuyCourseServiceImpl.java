package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduStudentBuyCourse;
import cn.xueden.edu.repository.EduCourseRepository;
import cn.xueden.edu.repository.EduStudentBuyCourseRepository;
import cn.xueden.edu.service.IEduStudentBuyCourseService;
import cn.xueden.edu.wechat.config.WechatConfig;
import cn.xueden.edu.wechat.dto.AmountDto;
import cn.xueden.edu.wechat.dto.WxOrderDto;
import cn.xueden.edu.wechat.service.WxPayService;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.HutoolJWTUtil;
import cn.xueden.utils.IpInfo;
import cn.xueden.utils.XuedenUtil;
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

    private final WechatConfig wechatConfig;

    private final WxPayService wxPayService;

    public EduStudentBuyCourseServiceImpl(EduStudentBuyCourseRepository eduStudentBuyCourseRepository, EduCourseRepository eduCourseRepository, WechatConfig wechatConfig, WxPayService wxPayService) {
        this.eduStudentBuyCourseRepository = eduStudentBuyCourseRepository;
        this.eduCourseRepository = eduCourseRepository;
        this.wechatConfig = wechatConfig;
        this.wxPayService = wxPayService;
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
            Long studentId = HutoolJWTUtil.parseToken(token);
            if(null==studentId){
                throw new BadRequestException("购买失败，请先登录！");
            }else {

                // 判断学员是否已经购买过该课程
                EduStudentBuyCourse dbEduStudentBuyCourse = eduStudentBuyCourseRepository.findByStudentIdAndCourseId(studentId,id);
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
                    tempEduStudentBuyCourse.setStudentId(studentId);
                    tempEduStudentBuyCourse.setOrderNo(orderNo);
                    tempEduStudentBuyCourse.setCreateBy(studentId);
                    tempEduStudentBuyCourse.setUpdateBy(studentId);
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
        wxOrderDto.setNotify_url(wechatConfig.getNotifyCourseUrl());
        wxOrderDto.setMchid(wechatConfig.getMchId());
        wxOrderDto.setAppid(wechatConfig.getAppId());
        try {
            String code=wxPayService.CreateNativeOrder(wxOrderDto);
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
}
