package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduSubject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：课程分类持久层
 * @author Administrator
 */
public interface EduSubjectRepository extends JpaRepository<EduSubject, Long>, JpaSpecificationExecutor<EduSubject> {
    /**
     * 获取父节点
     * @param parentId
     * @return
     */
    EduSubject getByParentId(Long parentId);

    /**
     * 根据父节点分页获取数据
     * @param parentId
     * @param subPageable
     * @return
     */
    List<EduSubject> findByParentId(Long parentId, Pageable subPageable);
}