package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduWxpay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：微信支付信息配置持久层
 * @author 梁志杰
 */
public interface EduWxpayRepository extends JpaRepository<EduWxpay, Integer>, JpaSpecificationExecutor<EduWxpay> {
}