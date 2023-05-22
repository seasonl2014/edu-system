package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCourseVideo;
import cn.xueden.edu.repository.EduCourseVideoRepository;
import cn.xueden.edu.service.IEduCourseVideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：视频业务接口实现
 * @author:梁志杰
 * @date:2023/5/19
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduCourseVideoServiceImpl implements IEduCourseVideoService {

    private final EduCourseVideoRepository eduCourseVideoRepository;

    public EduCourseVideoServiceImpl(EduCourseVideoRepository eduCourseVideoRepository) {
        this.eduCourseVideoRepository = eduCourseVideoRepository;
    }

    /**
     * 根据视频ID获取视频信息
     * @param id
     * @return
     */
    @Override
    public EduCourseVideo findById(Long id) {
        return eduCourseVideoRepository.findById(id).orElseGet(EduCourseVideo::new);
    }
}
