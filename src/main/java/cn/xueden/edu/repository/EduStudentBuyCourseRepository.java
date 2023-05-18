package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduStudentBuyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：学员购买课程持久层
 * @author Administrator
 */
public interface EduStudentBuyCourseRepository extends JpaRepository<EduStudentBuyCourse, Long>, JpaSpecificationExecutor<EduStudentBuyCourse> {
    /**
     * 根据学员ID获取购买课程信息
     * @param studentId
     * @return
     */
    EduStudentBuyCourse findByStudentId(Long studentId);

    /**
     * 根据订单编号获取订单信息
     * @param orderNo
     * @return
     */
    EduStudentBuyCourse getByOrderNo(String orderNo);

    /**
     * 根据学员ID和课程ID获取订单详细
     * @param studentId
     * @param courseId
     * @return
     */
    EduStudentBuyCourse findByStudentIdAndCourseId(Long studentId, Long courseId);
}