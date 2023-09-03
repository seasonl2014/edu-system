package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduWxpay;

/**功能描述：微信支付信息配置业务接口
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduWxpayService {

    /**
     * 获取一条记录
     * @return
     */
    EduWxpay getOne();

    /**
     * 保存信息
     * @param eduWxpay
     */
    void save(EduWxpay eduWxpay);
}
