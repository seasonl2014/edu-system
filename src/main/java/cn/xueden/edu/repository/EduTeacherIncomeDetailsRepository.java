package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduTeacherIncomeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：讲师收入持久层
 * @author Administrator
 */
public interface EduTeacherIncomeDetailsRepository extends JpaRepository<EduTeacherIncomeDetails, Long>, JpaSpecificationExecutor<EduTeacherIncomeDetails> {
}