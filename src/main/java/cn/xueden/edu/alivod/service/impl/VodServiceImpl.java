package cn.xueden.edu.alivod.service.impl;

import cn.xueden.edu.alivod.AliOssUploadVideoLocalFileService;
import cn.xueden.edu.alivod.service.IVodService;
import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduCourseChapter;
import cn.xueden.edu.domain.EduCourseVideo;
import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.repository.EduCourseChapterRepository;
import cn.xueden.edu.repository.EduCourseRepository;
import cn.xueden.edu.repository.EduCourseVideoRepository;
import cn.xueden.edu.repository.EduSubjectRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author:梁志杰
 * @date:2023/5/30
 * @description:cn.xueden.edu.alivod.service.impl
 * @version:1.0
 */
@Service
public class VodServiceImpl implements IVodService {

    private final EduCourseChapterRepository eduCourseChapterRepository;

    private final EduCourseRepository eduCourseRepository;

    private final EduSubjectRepository eduSubjectRepository;

    private final EduCourseVideoRepository eduCourseVideoRepository;

    private final AliOssUploadVideoLocalFileService aliOssUploadVideoLocalFileService;

    public VodServiceImpl(EduCourseChapterRepository eduCourseChapterRepository, EduCourseRepository eduCourseRepository, EduSubjectRepository eduSubjectRepository, EduCourseVideoRepository eduCourseVideoRepository, AliOssUploadVideoLocalFileService aliOssUploadVideoLocalFileService) {
        this.eduCourseChapterRepository = eduCourseChapterRepository;
        this.eduCourseRepository = eduCourseRepository;
        this.eduSubjectRepository = eduSubjectRepository;
        this.eduCourseVideoRepository = eduCourseVideoRepository;
        this.aliOssUploadVideoLocalFileService = aliOssUploadVideoLocalFileService;
    }

    /**
     * 根据章节id实现上传视频到阿里云服务器的方法
     * @param file 文件
     * @param id 大章ID
     * @param request
     * @param fileKey 文件唯一标志
     * @return
     */
    @Override
    public String batchUploadAliyunVideoById(MultipartFile file, Long id, HttpServletRequest request, String fileKey) {
        try {
            //获取大章信息
            EduCourseChapter eduChapter = eduCourseChapterRepository.getReferenceById(id);
            if(null==eduChapter){
                return null;
            }
            //根据课程ID获取课程信息
            EduCourse eduCourse = eduCourseRepository.getReferenceById(eduChapter.getCourseId());
            if(null==eduCourse){
                return null;
            }
            // 根据课程类别ID获取课程分类信息
            EduSubject eduSubject = eduSubjectRepository.getReferenceById(eduCourse.getSubjectId());
            if(null==eduSubject){
                return null;
            }
            EduCourseVideo eduCourseVideo = aliOssUploadVideoLocalFileService.uploadVideoLocalFile(file,eduSubject.getCateId(),request,eduChapter.getId(),fileKey);
            if(eduCourseVideo!=null){
                eduCourseVideo.setCourseId(eduCourse.getId());
                eduCourseVideo.setChapterId(eduChapter.getId());
                eduCourseVideo.setSort(0);
                eduCourseVideo.setPlayCount(0L);
                eduCourseVideo.setIsFree(1);
                eduCourseVideo.setVersion(1L);
                eduCourseVideoRepository.save(eduCourseVideo);
                return eduCourseVideo.getVideoSourceId();
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传单条视频到阿里云服务器的方法
     * @param file 待上传的文件
     * @param id 课程小节ID
     * @param request
     * @param fileKey
     * @return
     */
    @Override
    public String singleUploadAliyunVideoById(MultipartFile file, Long id, HttpServletRequest request, String fileKey) {
        try {
            //获取课程小节信息
            EduCourseVideo dbEduCourseVideo = eduCourseVideoRepository.getReferenceById(id);
            if(null==dbEduCourseVideo){
                return null;
            }
            //根据课程ID获取课程信息
            EduCourse eduCourse = eduCourseRepository.getReferenceById(dbEduCourseVideo.getCourseId());
            if(null==eduCourse){
                return null;
            }
            // 根据课程类别ID获取课程分类信息
            EduSubject eduSubject = eduSubjectRepository.getReferenceById(eduCourse.getSubjectId());
            if(null==eduSubject){
                return null;
            }
            EduCourseVideo eduCourseVideo = aliOssUploadVideoLocalFileService.uploadVideoLocalFile(file,eduSubject.getCateId(),request,dbEduCourseVideo.getId(),fileKey);
            if(eduCourseVideo!=null){
                dbEduCourseVideo.setVideoSourceId(eduCourseVideo.getVideoSourceId());
                dbEduCourseVideo.setDuration(eduCourseVideo.getDuration());
                eduCourseVideoRepository.save(dbEduCourseVideo);
                return eduCourseVideo.getVideoSourceId();
            }else {
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
