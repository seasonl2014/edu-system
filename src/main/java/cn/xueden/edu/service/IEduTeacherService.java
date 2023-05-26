package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduTeacher;
import cn.xueden.edu.service.dto.EduTeacherQueryCriteria;

import org.springframework.data.domain.Pageable;

import java.util.List;

/**功能描述：教师模块业务接口
 * @author:梁志杰
 * @date:2023/2/24
 * @description:cn.xueden.student.service
 * @version:1.0
 */
public interface IEduTeacherService {

    /**
     * 获取教师列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduTeacherQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加教师信息
     * @param teacher
     * @return
     */
    boolean addEduTeacher(EduTeacher teacher);

    /**
     * 根据ID获取详情信息
     * @param id
     * @return
     */
    EduTeacher getById(Long id);

    /**
     * 更新教师信息
     * @param teacher
     */
    void editEduTeacher(EduTeacher teacher);

    /**
     * 根据ID删除教师信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 统计教师人数
     * @return
     */
    long getCount();

    /**
     * 获取所有讲师
     * @return
     */
    List<EduTeacher> getAll();
}
