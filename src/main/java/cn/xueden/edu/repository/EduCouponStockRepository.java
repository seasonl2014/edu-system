package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCouponStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：代金券批次持久层
 * @author Administrator
 */
public interface EduCouponStockRepository extends JpaRepository<EduCouponStock, Long>, JpaSpecificationExecutor<EduCouponStock> {
}