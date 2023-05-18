package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduDealMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：成交金额汇总持久层
 * @author Administrator
 */
public interface EduDealMoneyRepository extends JpaRepository<EduDealMoney, Long>, JpaSpecificationExecutor<EduDealMoney> {
    /**
     * 根据订单编号获取成绩金额汇总数据
     * @param out_trade_no
     * @return
     */
    EduDealMoney getByOrderNo(String out_trade_no);
}