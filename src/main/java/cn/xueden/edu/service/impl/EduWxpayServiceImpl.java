package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.repository.EduWxpayRepository;
import cn.xueden.edu.service.IEduWxpayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：微信支付信息配置业务接口实现类
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduWxpayServiceImpl implements IEduWxpayService {

    private final EduWxpayRepository eduWxpayRepository;

    public EduWxpayServiceImpl(EduWxpayRepository eduWxpayRepository) {
        this.eduWxpayRepository = eduWxpayRepository;
    }

    /**
     * 获取一条记录
     * @return
     */
    @Override
    public EduWxpay getOne() {
        return eduWxpayRepository.findAll().get(0);
    }
}
