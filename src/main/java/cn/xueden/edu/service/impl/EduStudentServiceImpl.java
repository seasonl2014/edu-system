package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduStudentId;
import cn.xueden.edu.repository.EduStudentIdRepository;
import cn.xueden.edu.repository.EduStudentRepository;
import cn.xueden.edu.service.IEduStudentService;

import cn.xueden.edu.service.dto.EduStudentQueryCriteria;

import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.*;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：学生信息业务接口实现类
 * @author:梁志杰
 * @date:2023/2/17
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduStudentServiceImpl implements IEduStudentService {

    private final EduStudentRepository studentRepository;

    private final EduStudentIdRepository eduStudentIdRepository;

    public EduStudentServiceImpl(EduStudentRepository studentRepository, EduStudentIdRepository eduStudentIdRepository) {
        this.studentRepository = studentRepository;
        this.eduStudentIdRepository = eduStudentIdRepository;
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
    public BaseResult login(EduStudent student) {
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
            Integer loginTimes = student.getLoginTimes()==null?1:student.getLoginTimes()+1;
            dbEduStudent.setLoginTimes(loginTimes);
            studentRepository.save(dbEduStudent);
        }

        // 生成token
        String token = HutoolJWTUtil.createToken(dbEduStudent);
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
}
