package cn.xueden.edu.service.impl;

import cn.xueden.edu.converter.EduCourseChapterConverter;
import cn.xueden.edu.domain.EduCourseChapter;
import cn.xueden.edu.domain.EduCourseVideo;
import cn.xueden.edu.repository.EduCourseChapterRepository;
import cn.xueden.edu.repository.EduCourseVideoRepository;
import cn.xueden.edu.service.IEduCourseChapterService;
import cn.xueden.edu.vo.EduChapterModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：课程大章业务接口实现类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduCourseChapterServiceImpl implements IEduCourseChapterService {

    private final EduCourseChapterRepository eduCourseChapterRepository;

    private final EduCourseVideoRepository eduCourseVideoRepository;

    public EduCourseChapterServiceImpl(EduCourseChapterRepository eduCourseChapterRepository, EduCourseVideoRepository eduCourseVideoRepository) {
        this.eduCourseChapterRepository = eduCourseChapterRepository;
        this.eduCourseVideoRepository = eduCourseVideoRepository;
    }

    /**
     * 根据课程ID获取相应的课程大纲数据
     * @param courseId
     * @return
     */
    @Override
    public Object getEduCourseChapterListByCourseId(Long courseId) {
        // 根据课程ID获取课程大章
        List<EduCourseChapter> eduCourseChapters = eduCourseChapterRepository.findListByCourseId(courseId);
        List<EduChapterModel> eduChapterVOList = EduCourseChapterConverter.converterToVOList(eduCourseChapters);
        // 根据课程大章ID获取课程视频小节
        for (EduChapterModel chapterVO:eduChapterVOList){
            List<EduCourseVideo> eduVideoList = eduCourseVideoRepository.findByChapterIdOrderBySortAsc(chapterVO.getId());
            chapterVO.setChildren(eduVideoList);
        }
        return eduChapterVOList;
    }
}
