package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCouponGrantRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：代金券发放记录持久层
 * @author Administrator
 */
public interface EduCouponGrantRecordRepository extends JpaRepository<EduCouponGrantRecord, Long>, JpaSpecificationExecutor<EduCouponGrantRecord> {
}