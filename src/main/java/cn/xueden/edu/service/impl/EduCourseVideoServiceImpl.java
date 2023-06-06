package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCourseChapter;
import cn.xueden.edu.domain.EduCourseVideo;
import cn.xueden.edu.repository.EduCourseChapterRepository;
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

    private final EduCourseChapterRepository eduCourseChapterRepository;

    public EduCourseVideoServiceImpl(EduCourseVideoRepository eduCourseVideoRepository, EduCourseChapterRepository eduCourseChapterRepository) {
        this.eduCourseVideoRepository = eduCourseVideoRepository;
        this.eduCourseChapterRepository = eduCourseChapterRepository;
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
        return eduCourseVideoRepository.findFirstByFileKey(fileKey);
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

    /**
     * 极速秒传保存数据
     * @param id 章节ID
     * @param dbEduCourseVideo 已经存在的视频对象
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUpload(Long id, EduCourseVideo dbEduCourseVideo) {

        //获取大章信息
        EduCourseChapter eduChapter = eduCourseChapterRepository.getReferenceById(id);

        EduCourseVideo tempEduCourseVideo = new EduCourseVideo();
        tempEduCourseVideo.setCourseId(eduChapter.getCourseId());
        tempEduCourseVideo.setChapterId(id);
        tempEduCourseVideo.setFileKey(dbEduCourseVideo.getFileKey());
        tempEduCourseVideo.setDuration(dbEduCourseVideo.getDuration());
        tempEduCourseVideo.setVideoSourceId(dbEduCourseVideo.getVideoSourceId());
        tempEduCourseVideo.setIsFree(1);
        tempEduCourseVideo.setSort(0);
        tempEduCourseVideo.setVersion(1L);
        tempEduCourseVideo.setPlayCount(0L);
        tempEduCourseVideo.setSize(dbEduCourseVideo.getSize());
        tempEduCourseVideo.setTitle(dbEduCourseVideo.getTitle());
        tempEduCourseVideo.setVideoOriginalName(dbEduCourseVideo.getVideoOriginalName());
        eduCourseVideoRepository.save(tempEduCourseVideo);
    }
}
