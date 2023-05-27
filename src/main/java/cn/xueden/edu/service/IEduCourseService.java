package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.service.dto.EduCourseQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**功能描述：课程业务接口
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduCourseService {
    /**
     * 获取课程列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduCourseQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加课程信息
     * @param eduCourse
     */
    void addCourse(EduCourse eduCourse);

    /**
     * 根据ID获取课程详情信息
     * @param id
     * @return
     */
    EduCourse getById(Long id);

    /**
     * 更新课程信息
     * @param eduCourse
     */
    void editCourse(EduCourse eduCourse);

    /**
     * 根据id删除课程信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 获取首页各类别课程
     * @param pageable
     * @return
     */
    Object findIndexCourseList(Pageable pageable);

    /**
     * 获取指定教师的十门课程
     * @param id
     * @param pageable
     * @return
     */
    List<EduCourse> findListByTeacherId(Long id, Pageable pageable);

    /**
     * 根据课程ID更新课程购买数量
     * @param courseId
     */
    void updateBuyCount(Long courseId);

    /**
     * 保存课程封面
     * @param courseId
     * @param urlPath
     */
    void uploadCover(Long courseId, String urlPath);

    /**
     * 更新课程状态
     * @param courseId
     * @param status
     */
    void updateStatus(Long courseId, String status);
}
