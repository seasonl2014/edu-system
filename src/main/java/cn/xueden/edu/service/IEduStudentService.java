package cn.xueden.edu.service;

import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;

import cn.xueden.edu.service.dto.EduStudentQueryCriteria;

import org.springframework.data.domain.Pageable;

/**功能描述：学生管理业务接口
 * @author:梁志杰
 * @date:2023/2/17
 * @description:cn.xueden.student.service
 * @version:1.0
 */
public interface IEduStudentService {
    /**
     * 获取学生列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduStudentQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加学生信息
     * @param student
     * @return
     */
    void addStudent(EduStudent student);

    /**
     * 根据ID获取学生详情信息
     * @param id
     * @return
     */
    EduStudent getById(Long id);

    /**
     * 更新学生信息
     * @param student
     */
    void editStudent(EduStudent student);

    /**
     * 根据ID删除学生信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 统计学生人数
     * @return
     */
    long getCount();

    /**
     * 学员登录
     * @param student
     * @return
     */
    BaseResult login(EduStudent student);
}
