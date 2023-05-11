package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.repository.EduStudentRepository;
import cn.xueden.edu.service.IEduStudentService;

import cn.xueden.edu.service.dto.EduStudentQueryCriteria;

import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;

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

    public EduStudentServiceImpl(EduStudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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
    public boolean addStudent(EduStudent student) {
        EduStudent dbStudent = studentRepository.save(student);
        return dbStudent.getId()!=null;
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
}
