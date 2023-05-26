package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;

import cn.xueden.edu.domain.EduTeacher;
import cn.xueden.edu.repository.EduTeacherRepository;
import cn.xueden.edu.service.IEduTeacherService;
import cn.xueden.edu.service.dto.EduTeacherQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：教师模块业务接口实现类
 * @author:梁志杰
 * @date:2023/2/24
 * @description:cn.xueden.student.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduTeacherServiceImpl implements IEduTeacherService {

    private final EduTeacherRepository eduTeacherRepository;

    public EduTeacherServiceImpl(EduTeacherRepository eduTeacherRepository) {
        this.eduTeacherRepository = eduTeacherRepository;
    }

    /**
     * 获取教师列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduTeacherQueryCriteria queryCriteria, Pageable pageable) {
       Page<EduTeacher> page = eduTeacherRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加教师信息
     * @param EduTeacher
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEduTeacher(EduTeacher EduTeacher) {
       EduTeacher dbEduTeacher = eduTeacherRepository.save(EduTeacher);
        return dbEduTeacher.getId()!=null;
    }

    /**
     * 根据ID获取详情信息
     * @param id
     * @return
     */
    @Override
    public EduTeacher getById(Long id) {
        return eduTeacherRepository.findById(id).orElseGet(EduTeacher::new);
    }

    /**
     * 更新教师信息
     * @param eduTeacher
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editEduTeacher(EduTeacher eduTeacher) {
       EduTeacher dbEduTeacher = eduTeacherRepository.getReferenceById(eduTeacher.getId());
        BeanUtil.copyProperties(eduTeacher,dbEduTeacher,
                CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        eduTeacherRepository.save(dbEduTeacher);
    }

    /**
     * 根据ID删除教师信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        eduTeacherRepository.deleteById(id);
    }

    /**
     * 统计教师人数
     * @return
     */
    @Override
    public long getCount() {
        return eduTeacherRepository.count();
    }

    /**
     * 获取所有讲师
     * @return
     */
    @Override
    public List<EduTeacher> getAll() {
        return eduTeacherRepository.findAll();
    }
}
