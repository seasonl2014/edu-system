package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduDealMoney;
import cn.xueden.edu.repository.EduDealMoneyRepository;
import cn.xueden.edu.service.IEduDealMoneyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：成交金额汇总业务接口
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduDealMoneyServiceImpl implements IEduDealMoneyService {

    private final EduDealMoneyRepository eduDealMoneyRepository;

    public EduDealMoneyServiceImpl(EduDealMoneyRepository eduDealMoneyRepository) {
        this.eduDealMoneyRepository = eduDealMoneyRepository;
    }

    /**
     * 根据订单编号获取成绩金额汇总数据
     * @param out_trade_no
     * @return
     */
    @Override
    public EduDealMoney getByOrderNumber(String out_trade_no) {
        return eduDealMoneyRepository.getByOrderNo(out_trade_no);
    }

    /**
     * 保存记录
     * @param eduDealMoney
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(EduDealMoney eduDealMoney) {
        eduDealMoneyRepository.save(eduDealMoney);
    }
}
