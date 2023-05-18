package cn.xueden.edu.service.impl;

import cn.xueden.edu.repository.EduEnvironmenParamRepository;
import cn.xueden.edu.service.IEduEnvironmenParamService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：开发环境参数业务接口实现类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduEnvironmenParamServiceImpl implements IEduEnvironmenParamService {

    private final EduEnvironmenParamRepository eduEnvironmenParamRepository;

    public EduEnvironmenParamServiceImpl(EduEnvironmenParamRepository eduEnvironmenParamRepository) {
        this.eduEnvironmenParamRepository = eduEnvironmenParamRepository;
    }

    /**
     * 根据课程ID获取相应的开发环境参数数据
     * @param courseId
     * @return
     */
    @Override
    public Object getEduEnvironmenParamListByCourseId(Long courseId) {
        return eduEnvironmenParamRepository.getListByCourseId(courseId);
    }
}