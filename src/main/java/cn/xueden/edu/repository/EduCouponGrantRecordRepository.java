package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCouponGrantRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：代金券发放记录持久层
 * @author Administrator
 */
public interface EduCouponGrantRecordRepository extends JpaRepository<EduCouponGrantRecord, Long>, JpaSpecificationExecutor<EduCouponGrantRecord> {

    /**
     * 个人中心获取学员我的优惠券
     * @param studentId
     * @param pageable
     * @return
     */
    Page<EduCouponGrantRecord> findListByStudentId(Long studentId, Pageable pageable);
}