package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduCourseChapter;

/**功能描述：课程大章业务接口
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduCourseChapterService {
    /**
     * 根据课程ID获取相应的课程大纲数据
     * @param courseId
     * @return
     */
    Object getEduCourseChapterListByCourseId(Long courseId);

    /**
     * 保存课程大纲
     * @param eduCourseChapter
     */
    void addEduChapter(EduCourseChapter eduCourseChapter);

    /**
     * 根据ID获取大章信息
     * @param id
     * @return
     */
    EduCourseChapter getById(Long id);

    /**
     * 删除课程大章
     * @param id
     */
    void removeById(Long id);
}
