package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.repository.EduCourseRepository;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.dto.EduCourseQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**功能描述：课程业务接口实现
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class EduCourseServiceImpl implements IEduCourseService {

    private final EduCourseRepository eduCourseRepository;

    public EduCourseServiceImpl(EduCourseRepository eduCourseRepository) {
        this.eduCourseRepository = eduCourseRepository;
    }

    /**
     * 获取课程列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduCourseQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduCourse> page = eduCourseRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 添加课程信息
     * @param eduCourse
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addCourse(EduCourse eduCourse) {

    }

    /**
     * 根据ID获取课程详情信息
     * @param id
     * @return
     */
    @Override
    public EduCourse getById(Long id) {
        return eduCourseRepository.getReferenceById(id);
    }

    /**
     * 更新课程信息
     * @param eduCourse
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editCourse(EduCourse eduCourse) {

    }

    /**
     * 根据id删除课程信息
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        eduCourseRepository.deleteById(id);
    }

    /**
     * 获取首页各类别课程
     * @param pageable
     * @return
     */
    @Override
    public Object findIndexCourseList(Pageable pageable) {
        Map<String,Object> resultMap = new HashMap<>();
        // 已发布的课程
        String status = "Normal";
        // 根据条件分页获取入门课程
        int courseType = 0;
       List<EduCourse> startedCourses = eduCourseRepository.findByStatusAndCourseType(status,courseType,pageable);
       resultMap.put("startedCourses",startedCourses);

        // 根据条件分页获取新上好课
        courseType = 1;
        List<EduCourse> newCourses = eduCourseRepository.findByStatusAndCourseType(status,courseType,pageable);
        resultMap.put("newCourses",newCourses);
        // 根据条件分页获取技能提高
        courseType = 2;
        List<EduCourse> skillCourses = eduCourseRepository.findByStatusAndCourseType(status,courseType,pageable);
        resultMap.put("skillCourses",skillCourses);
        // 根据条件分页获取实战课程
        courseType = 3;
        List<EduCourse> actualCourses = eduCourseRepository.findByStatusAndCourseType(status,courseType,pageable);
        resultMap.put("actualCourses",actualCourses);
        return resultMap;
    }
}
