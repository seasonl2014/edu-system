package cn.xueden.edu.service;

import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduCourseVideo;

/**功能描述：视频业务接口
 * @author:梁志杰
 * @date:2023/5/19
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduCourseVideoService {
    /**
     * 根据视频ID获取视频信息
     * @param id
     * @return
     */
    EduCourseVideo findById(Long id);

    /**
     * 根据文件KEY查找记录
     * @param fileKey
     * @return
     */
    EduCourseVideo findByFileKey(String fileKey);

    /**
     * 更新课程大纲小节
     * @param eduVideoVO
     */
    void save(EduCourseVideo eduVideoVO);

    /**
     * 删除课程大纲小节
     * @param id
     */
    void removeById(Long id);

    /**
     * 更新视频源ID和时长
     * @param id
     * @param dbEduCourseVideo
     */
    void updateByCourseVideo(Long id, EduCourseVideo dbEduCourseVideo);

    /**
     * 极速秒传保存视频
     * @param id 章节ID
     * @param dbEduCourseVideo 已经存在的视频对象
     */
    void saveUpload(Long id, EduCourseVideo dbEduCourseVideo);
}
