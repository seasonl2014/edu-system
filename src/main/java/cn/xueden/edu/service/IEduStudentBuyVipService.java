package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduStudentBuyVip;

/**功能描述：学员购买VIP业务接口
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduStudentBuyVipService {
    /**
     * 购买VIP
     * @param id
     * @param studentToken
     * @param ipAddress
     * @return
     */
    EduStudentBuyVip buyVip(Long id, String studentToken, String ipAddress);

    /**
     * 根据订单编号获取订单详情
     * @param orderNo
     * @return
     */
    EduStudentBuyVip getOrderInfo(String orderNo);

    /**
     * 去支付生成支付二维码链接
     * @param orderNo
     * @return
     */
    String payBuy(String orderNo);
}
