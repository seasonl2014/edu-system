package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduStudentBuyVip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：学员购买VIP持久层
 * @author Administrator
 */
public interface EduStudentBuyVipRepository extends JpaRepository<EduStudentBuyVip, Long>, JpaSpecificationExecutor<EduStudentBuyVip> {
    /**
     * 根据学员ID获取购买信息
     * @param studentId
     * @return
     */
    EduStudentBuyVip findByStudentId(Long studentId);

    /**
     * 根据订单编号获取订单详情
     * @param orderNo
     * @return
     */
    EduStudentBuyVip getByOrderNo(String orderNo);
}