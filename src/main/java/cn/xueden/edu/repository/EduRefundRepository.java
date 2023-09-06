package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduRefund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：退款记录持久层
 * @author Administrator
 */
public interface EduRefundRepository extends JpaRepository<EduRefund, Integer>, JpaSpecificationExecutor<EduRefund> {
}