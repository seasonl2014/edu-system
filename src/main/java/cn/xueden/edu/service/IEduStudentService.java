package cn.xueden.edu.service;

import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;

import cn.xueden.edu.service.dto.EduStudentQueryCriteria;

import cn.xueden.edu.vo.PassWordModel;
import cn.xueden.edu.vo.UpdateStudentInfoModel;
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
    BaseResult login(EduStudent student,String ipAddress);

    /**
     * 根据openid获取学员信息
     * @param openid
     * @return
     */
    EduStudent getByOpenid(String openid);

    /**
     * 学员修改个人信息
     * @param studentId
     * @param studentInfoModel
     */
    void update(Long studentId, UpdateStudentInfoModel studentInfoModel);


    /**
     * 个人中心获取学员我的课程
     * @param token
     * @param pageable
     * @return
     */
    Object getMyCourseList(Long studentId, Pageable pageable);

    /**
     * 绑定邮箱
     * @param email
     * @param studentId
     */
    void bindEmail(String email, Long studentId);

    /**
     * 个人中心发送手机验证码
     * @param phone
     * @param studentId
     */
    Integer sendSms(String phone, Long studentId);

    /**
     * 更换手机
     * @param phone
     * @param studentId
     */
    void updatePhone(String phone, Long studentId);

    /**
     * 个人中心更改密码
     * @param passWordModel
     */
    void savePassWord(PassWordModel passWordModel, Long studentId);

    /**
     * 找回密码发送手机验证码
     * @param phone
     * @return
     */
    Integer findPwdSendSms(String phone);

    /**
     * 保存重新设置的密码
     * @param passWordModel
     */
    void saveFindPassWord(PassWordModel passWordModel);
}
