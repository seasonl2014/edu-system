package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduCourse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：课程持久层
 * @author Administrator
 */
public interface EduCourseRepository extends JpaRepository<EduCourse, Long>, JpaSpecificationExecutor<EduCourse> {

    /**
     * 根据父节点分页获取数据
     * @param id
     * @param subCoursePageable
     * @return
     */
    List<EduCourse> findBySubjectParentId(Long id, Pageable subCoursePageable);


    /**
     * 根据课程状态和课程类型分页获取数据
     * @param status
     * @param courseType
     * @param pageable
     * @return
     */
    List<EduCourse> findByStatusAndCourseType(String status, int courseType, Pageable pageable);
}