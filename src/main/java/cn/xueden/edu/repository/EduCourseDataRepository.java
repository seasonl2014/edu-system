package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCourseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：课程资料持久层
 * @author Administrator
 */
public interface EduCourseDataRepository extends JpaRepository<EduCourseData, Long>, JpaSpecificationExecutor<EduCourseData> {
    /**
     * 根据课程ID获取相应的课程资料数据
     * @param courseId
     * @return
     */
    List<EduCourseData> getCourseDataByCourseId(Long courseId);
}