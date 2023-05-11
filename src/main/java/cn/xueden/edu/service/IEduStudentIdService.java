package cn.xueden.edu.service;

import cn.xueden.edu.service.dto.EduStudentIdQueryCriteria;
import org.springframework.data.domain.Pageable;

/**功能描述：学生学号业务接口
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduStudentIdService {

    /**
     * 根据条件分页获取学生学号列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduStudentIdQueryCriteria queryCriteria, Pageable pageable);
}
