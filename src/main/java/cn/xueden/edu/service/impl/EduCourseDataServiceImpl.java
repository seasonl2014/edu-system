package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCourseData;
import cn.xueden.edu.repository.EduCourseDataRepository;
import cn.xueden.edu.service.IEduCourseDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：课程资料业务接口实现
 * @author:梁志杰
 * @date:2023/5/19
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduCourseDataServiceImpl implements IEduCourseDataService {

    private final EduCourseDataRepository eduCourseDataRepository;

    public EduCourseDataServiceImpl(EduCourseDataRepository eduCourseDataRepository) {
        this.eduCourseDataRepository = eduCourseDataRepository;
    }

    /**
     * 根据课程ID获取相应的课程资料数据
     * @param courseId
     * @return
     */
    @Override
    public List<EduCourseData> getCourseDataByCourseId(Long courseId) {
        return eduCourseDataRepository.getCourseDataByCourseId(courseId);
    }

    /**
     * 根据ID获取课程资料
     * @param courseDataId
     * @return
     */
    @Override
    public EduCourseData getById(Long courseDataId) {
        return eduCourseDataRepository.findById(courseDataId).orElseGet(EduCourseData::new);
    }
}
