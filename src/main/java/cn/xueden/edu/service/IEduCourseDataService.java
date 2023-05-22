package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduCourseData;

import java.util.List;

/**功能描述：课程资料业务接口
 * @author:梁志杰
 * @date:2023/5/19
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduCourseDataService {
    /**
     * 根据课程ID获取相应的课程资料数据
     * @param courseId
     * @return
     */
    List<EduCourseData> getCourseDataByCourseId(Long courseId);

    /**
     * 根据ID获取课程资料
     * @param courseDataId
     * @return
     */
    EduCourseData getById(Long courseDataId);
}