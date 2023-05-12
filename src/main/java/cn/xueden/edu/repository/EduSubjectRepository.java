package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduSubject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：课程分类持久层
 * @author Administrator
 */
public interface EduSubjectRepository extends JpaRepository<EduSubject, Long>, JpaSpecificationExecutor<EduSubject> {
}