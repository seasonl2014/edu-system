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
}
