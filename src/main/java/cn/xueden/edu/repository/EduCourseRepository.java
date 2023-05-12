package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：课程持久层
 * @author Administrator
 */
public interface EduCourseRepository extends JpaRepository<EduCourse, Long>, JpaSpecificationExecutor<EduCourse> {
}