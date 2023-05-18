package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCourseVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：课程视频持久层
 * @author Administrator
 */
public interface EduCourseVideoRepository extends JpaRepository<EduCourseVideo, Long>, JpaSpecificationExecutor<EduCourseVideo> {
    /**
     * 根据课程大章ID获取课程视频小节
     * @param id
     * @return
     */
    List<EduCourseVideo> findByChapterIdOrderBySortAsc(Long id);
}