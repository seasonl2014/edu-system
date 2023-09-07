package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduCouponStock;
import cn.xueden.edu.service.dto.EduCouponQueryCriteria;
import org.springframework.data.domain.Pageable;

/**功能描述：代金券业务接口
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduCouponService {
    /**
     * 创建代金券批次
     * @param eduCoupon
     */
    void createCouponStock(EduCouponStock eduCoupon);

    /**
     * 获取代金券列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduCouponQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 生成代金券批次
     * @param id
     */
    void generateCouponStock(Long id);

    /**
     * 发放代金券
     * @param couponId
     * @param studentNo
     */
    void sendCoupon(Long couponId, String studentNo);

    /**
     * 激活开启批次
     * @param couponId
     */
    void startStock(Long couponId);
}
