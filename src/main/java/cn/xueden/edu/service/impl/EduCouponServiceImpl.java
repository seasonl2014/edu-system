package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCouponStock;

import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduCouponStockRepository;
import cn.xueden.edu.repository.EduStudentRepository;
import cn.xueden.edu.repository.EduWxpayRepository;
import cn.xueden.edu.service.IEduCouponService;
import cn.xueden.edu.service.dto.EduCouponQueryCriteria;
import cn.xueden.edu.wechat.service.WxPayService;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import cn.xueden.utils.XuedenUtil;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.SendCouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：代金券业务接口实现
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduCouponServiceImpl implements IEduCouponService {

    private final EduCouponStockRepository eduCouponRepository;

    private final WxPayService wxPayService;

    private final EduWxpayRepository eduWxpayRepository;

    private final EduStudentRepository eduStudentRepository;

    public EduCouponServiceImpl(EduCouponStockRepository eduCouponRepository, WxPayService wxPayService, EduWxpayRepository eduWxpayRepository, EduStudentRepository eduStudentRepository) {
        this.eduCouponRepository = eduCouponRepository;
        this.wxPayService = wxPayService;
        this.eduWxpayRepository = eduWxpayRepository;
        this.eduStudentRepository = eduStudentRepository;
    }

    /**
     * 创建代金券批次
     * @param eduCoupon
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createCouponStock(EduCouponStock eduCoupon) {
        eduCoupon.setNoCash(true);
        eduCoupon.setStatus(0);
        eduCouponRepository.save(eduCoupon);
    }

    /**
     * 获取代金券列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduCouponQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduCouponStock> page = eduCouponRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 生成代金券批次
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void generateCouponStock(Long id) {
        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        EduCouponStock dbEduCoupon = eduCouponRepository.getReferenceById(id);
        dbEduCoupon.setOutRequestNo(XuedenUtil.createOrderNumber());
        CreateCouponStockResponse response = wxPayService.createCouponStock(dbEduWxpay,dbEduCoupon);
        if(response!=null){
            dbEduCoupon.setStatus(1);
            dbEduCoupon.setStockId(response.getStockId());
            eduCouponRepository.save(dbEduCoupon);
        }else {
            throw new BadRequestException("生成失败！");
        }
    }

    /**
     * 发放代金券
     * @param couponId
     * @param studentNo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void sendCoupon(Long couponId, String studentNo) {
        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        // 代金券详情
        EduCouponStock dbEduCoupon = eduCouponRepository.getReferenceById(couponId);
        // 学员信息
        EduStudent dbEduStudent = eduStudentRepository.findByStuNo(studentNo);
        // 发送单据号
        String outRequestNo = XuedenUtil.createOrderNumber();
        SendCouponResponse response = wxPayService.sendCoupon(outRequestNo,dbEduStudent.getSpOpenid(),dbEduCoupon,dbEduWxpay);
        if(response!=null){
            // 保存到发放记录表
            // 发放给学员的代金券id
            String stuCouponId = response.getCouponId();
            System.out.println("stuCouponId:"+stuCouponId);

        }else {
            throw new BadRequestException("发放失败！");
        }
    }

    /**
     * 激活开启批次
     * @param couponId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void startStock(Long couponId) {
        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        // 代金券详情
        EduCouponStock dbEduCoupon = eduCouponRepository.getReferenceById(couponId);
        wxPayService.startStock(dbEduCoupon,dbEduWxpay);
    }
}
