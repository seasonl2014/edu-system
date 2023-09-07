package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCouponGrantRecord;
import cn.xueden.edu.domain.EduCouponStock;

import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduCouponGrantRecordRepository;
import cn.xueden.edu.repository.EduCouponStockRepository;
import cn.xueden.edu.repository.EduStudentRepository;
import cn.xueden.edu.repository.EduWxpayRepository;
import cn.xueden.edu.service.IEduCouponStockService;
import cn.xueden.edu.service.dto.EduCouponQueryCriteria;
import cn.xueden.edu.wechat.service.WxPayService;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import cn.xueden.utils.XuedenUtil;
import com.wechat.pay.java.service.cashcoupons.model.CreateCouponStockResponse;
import com.wechat.pay.java.service.cashcoupons.model.SendCouponResponse;
import com.wechat.pay.java.service.cashcoupons.model.Stock;
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
public class EduCouponStockServiceImpl implements IEduCouponStockService {

    private final EduCouponStockRepository eduCouponRepository;

    private final WxPayService wxPayService;

    private final EduWxpayRepository eduWxpayRepository;

    private final EduStudentRepository eduStudentRepository;

    private final EduCouponGrantRecordRepository eduCouponGrantRecordRepository;

    public EduCouponStockServiceImpl(EduCouponStockRepository eduCouponRepository, WxPayService wxPayService, EduWxpayRepository eduWxpayRepository, EduStudentRepository eduStudentRepository, EduCouponGrantRecordRepository eduCouponGrantRecordRepository) {
        this.eduCouponRepository = eduCouponRepository;
        this.wxPayService = wxPayService;
        this.eduWxpayRepository = eduWxpayRepository;
        this.eduStudentRepository = eduStudentRepository;
        this.eduCouponGrantRecordRepository = eduCouponGrantRecordRepository;
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
            // 保存记录到代金券发放记录表
            EduCouponGrantRecord tempEduCouponGrantRecord = new EduCouponGrantRecord();
            // 发放给学员的代金券id
            tempEduCouponGrantRecord.setStuCouponId(response.getCouponId());
            // 创建批次的商户号
            tempEduCouponGrantRecord.setStockCreatorMchid(dbEduWxpay.getMerchantId());
            // 批次号
            tempEduCouponGrantRecord.setStockId(dbEduCoupon.getStockId());
            // 代金券名称
            tempEduCouponGrantRecord.setCouponName(dbEduCoupon.getStockName());
            // 代金券状态
            tempEduCouponGrantRecord.setStatus("SENDED");
            // 发放学员ID
            tempEduCouponGrantRecord.setStudentId(dbEduStudent.getId());
            eduCouponGrantRecordRepository.save(tempEduCouponGrantRecord);
            // 更新代金券批次信息
            queryStock(couponId);
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
        queryStock(couponId);
    }

    /**
     * 查询指定批次详情
     * @param couponId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void queryStock(Long couponId) {
        // 获取微信支付配置信息
        EduWxpay dbEduWxpay = eduWxpayRepository.findFirstByOrderByIdDesc();
        // 代金券详情
        EduCouponStock dbEduCoupon = eduCouponRepository.getReferenceById(couponId);
        Stock stock = wxPayService.queryStock(dbEduCoupon,dbEduWxpay);
        // 批次状态 0表示未生成，1 未激活，2表示审核中，3表示运行中 4表示已停止，5表示暂停发放
        if(stock.getStatus().equals("unactivated")){
            dbEduCoupon.setStatus(1);
        }else if (stock.getStatus().equals("audit")){
            dbEduCoupon.setStatus(2);
        }else if (stock.getStatus().equals("running")){
            dbEduCoupon.setStatus(3);
        }else if (stock.getStatus().equals("stoped")){
            dbEduCoupon.setStatus(4);
        }else if (stock.getStatus().equals("paused")){
            dbEduCoupon.setStatus(5);
        }
        // 已发券数量
        dbEduCoupon.setDistributedCoupons(stock.getDistributedCoupons());
        eduCouponRepository.save(dbEduCoupon);
        System.out.println("查询结果："+stock);
    }
}
