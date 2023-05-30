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

    /**
     * 根据文件KEY查找记录
     * @param fileKey
     * @return
     */
    @Override
    public EduCourseVideo findByFileKey(String fileKey) {
        return eduCourseVideoRepository.findByFileKey(fileKey);
    }

    /**
     * 更新课程大纲小节
     * @param eduVideoVO
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(EduCourseVideo eduVideoVO) {
        eduCourseVideoRepository.save(eduVideoVO);
    }

    /**
     * 删除课程大纲小节
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(Long id) {
        eduCourseVideoRepository.deleteById(id);
    }

    /**
     * 更新视频源ID和时长
     * @param id
     * @param tempEduCourseVideo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateByCourseVideo(Long id, EduCourseVideo tempEduCourseVideo) {
        // 根据ID获取视频信息
        EduCourseVideo dbEduCourseVideo = eduCourseVideoRepository.getReferenceById(id);
        if(dbEduCourseVideo!=null){
            dbEduCourseVideo.setVideoSourceId(tempEduCourseVideo.getVideoSourceId());
            dbEduCourseVideo.setDuration(tempEduCourseVideo.getDuration());
            eduCourseVideoRepository.save(dbEduCourseVideo);
        }
    }
}
