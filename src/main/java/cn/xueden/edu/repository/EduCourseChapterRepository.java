package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCourseChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：课程大章持久层
 * @author Administrator
 */
public interface EduCourseChapterRepository extends JpaRepository<EduCourseChapter, Long>, JpaSpecificationExecutor<EduCourseChapter> {
    /**
     * 根据课程ID获取课程大章
     * @param courseId
     * @return
     */
    List<EduCourseChapter> findListByCourseId(Long courseId);
}