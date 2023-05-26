package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduStudentBuyCourse;
import cn.xueden.edu.domain.EduStudentId;
import cn.xueden.edu.repository.EduCourseRepository;
import cn.xueden.edu.repository.EduStudentBuyCourseRepository;
import cn.xueden.edu.repository.EduStudentIdRepository;
import cn.xueden.edu.repository.EduStudentRepository;
import cn.xueden.edu.service.IEduStudentService;

import cn.xueden.edu.service.dto.EduStudentQueryCriteria;

import cn.xueden.edu.vo.PassWordModel;
import cn.xueden.edu.vo.UpdateStudentInfoModel;
import cn.xueden.exception.BadRequestException;
import cn.xueden.sms.SendSmsService;
import cn.xueden.utils.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**功能描述：学生信息业务接口实现类
 * @author:梁志杰
 * @date:2023/2/17
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class EduStudentServiceImpl implements IEduStudentService {

    private final EduStudentRepository studentRepository;

    private final EduStudentIdRepository eduStudentIdRepository;

    private final EduStudentBuyCourseRepository eduStudentBuyCourseRepository;

    private final EduCourseRepository eduCourseRepository;

    public EduStudentServiceImpl(EduStudentRepository studentRepository, EduStudentIdRepository eduStudentIdRepository, EduStudentBuyCourseRepository eduStudentBuyCourseRepository, EduCourseRepository eduCourseRepository) {
        this.studentRepository = studentRepository;
        this.eduStudentIdRepository = eduStudentIdRepository;
        this.eduStudentBuyCourseRepository = eduStudentBuyCourseRepository;
        this.eduCourseRepository = eduCourseRepository;
    }

    /**
     * 获取学生列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduStudentQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduStudent> page = studentRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加学生信息
     * @param student
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudent(EduStudent student) {
        // 获取一个学号
        EduStudentId dbEduStudentId =  eduStudentIdRepository.findFirstByStatus(0);
        if(dbEduStudentId==null){
            throw new BadRequestException("添加失败，学生编号已经用完，请先生成学号!");
        }else {
            // 获取学号
            student.setStuNo(dbEduStudentId.getStudentId().toString());
            // 学生状态
            student.setStatus(1);
            student.setPassword(Md5Util.Md5(student.getPassword()));
            studentRepository.save(student);
            // 把学号状态更新为1，已使用
            dbEduStudentId.setStatus(1);
            eduStudentIdRepository.save(dbEduStudentId);
        }

    }

    /**
     * 根据ID获取学生详情信息
     * @param id
     * @return
     */
    @Override
    public EduStudent getById(Long id) {
        return studentRepository.findById(id).orElseGet(EduStudent::new);
    }

    /**
     * 更新学生信息
     * @param student
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editStudent(EduStudent student) {
        EduStudent dbStudent =studentRepository.findById(student.getId()).orElseGet(EduStudent::new);
        BeanUtil.copyProperties(student,dbStudent, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        studentRepository.save(dbStudent);
    }

    /**
     * 根据ID删除学生信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    /**
     * 统计人数
     * @return
     */
    @Override
    public long getCount() {
        return studentRepository.count();
    }

    @Override
    public BaseResult login(EduStudent student,String ipAddress) {
        // 根据手机号获取学员信息
        EduStudent dbEduStudent = studentRepository.findByPhone(student.getPhone());
        if(dbEduStudent==null){
            return BaseResult.fail("登录失败，该手机号未注册！");
        }else if (!dbEduStudent.getPassword().equals(Md5Util.Md5(student.getPassword()))){
            return BaseResult.fail("登录失败，输入密码不正确！");
        }else if(dbEduStudent.getStatus()==0){
            return BaseResult.fail("登录失败，该账号已被冻结，请联系客服！");
        }else {
           // 更新登录次数
            Integer loginTimes = dbEduStudent.getLoginTimes()==null?1:dbEduStudent.getLoginTimes()+1;
            dbEduStudent.setLoginTimes(loginTimes);
            try {
                IpInfo ipInfo = XuedenUtil.getCityInfo(ipAddress);
                if(null!=ipInfo){
                    dbEduStudent.setArea(ipInfo.getRegion());
                    dbEduStudent.setProvince(ipInfo.getProvince());
                    dbEduStudent.setCity(ipInfo.getCity());
                    dbEduStudent.setIsp(ipInfo.getIsp());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            studentRepository.save(dbEduStudent);
        }
         log.info("------------------开始生成token");
        // 生成token
        Map<String, String> map = new HashMap<>();
        map.put("studentId",dbEduStudent.getId().toString());
        map.put("phone",dbEduStudent.getPhone());
        String token = JWTUtil.getToken(map);
        log.info("------------------生成token结束",token);
        dbEduStudent.setStudentToken(token);
        return BaseResult.success(dbEduStudent);
    }


    /**
     * 根据openid获取学员信息
     * @param openid
     * @return
     */
    @Override
    public EduStudent getByOpenid(String openid) {
        return studentRepository.getByWxOpenId(openid);
    }

    /**
     * 学员修改个人信息
     * @param studentId
     * @param studentInfoModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long studentId, UpdateStudentInfoModel studentInfoModel) {
        if(studentId.equals(studentInfoModel.getId())){
            EduStudent dbEduStudent = studentRepository.findById(studentId).orElseGet(EduStudent::new);
            BeanUtils.copyProperties(studentInfoModel,dbEduStudent);
            studentRepository.saveAndFlush(dbEduStudent);
        }else {
            throw new BadRequestException("修改失败，只能修改自己的个人信息");
        }
    }

    /**
     * 个人中心获取学员我的课程
     * @param studentId
     * @param pageable
     * @return
     */
    @Override
    public Object getMyCourseList(Long studentId, Pageable pageable) {
        // 1表示已经付款成功的课程
        Page<EduStudentBuyCourse> page = eduStudentBuyCourseRepository.findListByStudentIdAndIsPayment(studentId,1,pageable);
        setEduCourse(page.stream().toList());
        return PageUtil.toPage(page);
    }

    private List<EduStudentBuyCourse> setEduCourse(List<EduStudentBuyCourse> eduStudentBuyCourseList){
        for (EduStudentBuyCourse eduStudentBuyCourse: eduStudentBuyCourseList){
            EduCourse tempEduCourse = eduCourseRepository.getReferenceById(eduStudentBuyCourse.getCourseId());
            eduStudentBuyCourse.setEduCourse(tempEduCourse);
        }
        return eduStudentBuyCourseList;
    }

    /**
     * 绑定邮箱
     * @param email
     * @param studentId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindEmail(String email, Long studentId) {
        // 获取学员信息
        EduStudent eduStudent = studentRepository.getReferenceById(studentId);
        eduStudent.setEmail(email);
        studentRepository.save(eduStudent);
    }

    /**
     * 个人中心发送手机验证码
     * @param phone
     * @param studentId
     */
    @Override
    public Integer sendSms(String phone, Long studentId) {
        // 随机生成6位数数
        Integer code = XuedenUtil.randomSixNums();
        SendSmsService.SendCodeByPhone(code.toString(),phone);
        return code;
    }

    /**
     * 更换手机
     * @param phone
     * @param studentId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePhone(String phone, Long studentId) {
        EduStudent eduStudent = studentRepository.getReferenceById(studentId);
        eduStudent.setPhone(phone);
        studentRepository.save(eduStudent);
    }

    /**
     * 个人中心更改密码
     * @param passWordModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePassWord(PassWordModel passWordModel, Long studentId) {
        EduStudent dbEduStudent = studentRepository.getReferenceById(studentId);
        if(passWordModel==null){
            throw new BadRequestException("更改失败，请填写完整信息！");
        }else if(!passWordModel.getNewPassWord().equalsIgnoreCase(passWordModel.getResNewPassWord())){
            throw new BadRequestException("更改失败,两次输入密码不一致！");
        }else if(!Md5Util.Md5(passWordModel.getPassWord()).equalsIgnoreCase(dbEduStudent.getPassword())){
            throw new BadRequestException("更改失败,原密码不正确！");
        }
        dbEduStudent.setPassword(Md5Util.Md5(passWordModel.getResNewPassWord()));
        studentRepository.save(dbEduStudent);
    }
}
