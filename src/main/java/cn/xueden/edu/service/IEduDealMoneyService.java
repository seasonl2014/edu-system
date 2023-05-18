package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduDealMoney;

/**功能描述：成交金额汇总业务接口
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduDealMoneyService {
    /**
     * 根据订单编号获取成绩金额汇总数据
     * @param out_trade_no
     * @return
     */
    EduDealMoney getByOrderNumber(String out_trade_no);

    /**
     * 保存记录
     * @param eduDealMoney
     */
    void save(EduDealMoney eduDealMoney);
}
