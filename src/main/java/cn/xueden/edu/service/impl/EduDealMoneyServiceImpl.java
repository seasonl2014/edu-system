package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduDealMoney;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.repository.EduDealMoneyRepository;
import cn.xueden.edu.service.IEduDealMoneyService;
import cn.xueden.edu.service.dto.EduDealMoneyQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     * 获取成交金额列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduDealMoneyQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduDealMoney> page = eduDealMoneyRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }
}
