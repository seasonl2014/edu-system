package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduDealMoney;
import cn.xueden.edu.service.dto.EduDealMoneyQueryCriteria;
import org.springframework.data.domain.Pageable;

/**功能描述：成交金额汇总业务接口
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduDealMoneyService {
    /**
     * 根据订单编号获取金额汇总数据
     * @param out_trade_no
     * @return
     */
    EduDealMoney getByOrderNumber(String out_trade_no);

    /**
     * 保存记录
     * @param eduDealMoney
     */
    void save(EduDealMoney eduDealMoney);

    /**
     * 获取成交金额列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduDealMoneyQueryCriteria queryCriteria, Pageable pageable);
}
