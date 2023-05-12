package cn.xueden.edu.service;

import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.Pageable;

/**功能描述：课程分类业务接口
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduSubjectService {

    /**
     * 获取课程分类树形结构
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    Object categoryTree(EduSubjectQueryCriteria queryCriteria, PageVo pageVo);
}
