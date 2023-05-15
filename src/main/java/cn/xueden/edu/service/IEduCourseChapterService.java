package cn.xueden.edu.service;

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
}
