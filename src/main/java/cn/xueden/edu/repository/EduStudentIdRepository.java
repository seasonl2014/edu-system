package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduStudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：学生学号持久层
 * @author Administrator
 */
public interface EduStudentIdRepository extends JpaRepository<EduStudentId, Long>, JpaSpecificationExecutor<EduStudentId> {

    /**
     * 根据状态获取一条记录
     * @param Status
     * @return
     */
    EduStudentId findFirstByStatus(int Status);
}