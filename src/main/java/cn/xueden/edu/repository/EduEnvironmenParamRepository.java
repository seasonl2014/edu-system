package cn.xueden.edu.repository;

import cn.xueden.edu.domain.EduEnvironmenParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**功能描述：课程开发环境参数持久层
 * @author Administrator
 */
public interface EduEnvironmenParamRepository extends JpaRepository<EduEnvironmenParam, Long>, JpaSpecificationExecutor<EduEnvironmenParam> {
    /**
     * 根据课程ID获取相应的开发环境参数数据
     * @param courseId
     * @return
     */
    List<EduEnvironmenParam> getListByCourseId(Long courseId);
}