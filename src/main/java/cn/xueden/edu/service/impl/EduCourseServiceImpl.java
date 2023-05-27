package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.domain.EduCourse;

import cn.xueden.edu.repository.EduCourseRepository;
import cn.xueden.edu.repository.EduCourseVideoRepository;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.dto.EduCourseQueryCriteria;
import cn.xueden.exception.BadRequestException;
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

    private final EduCourseVideoRepository eduCourseVideoRepository;

    public EduCourseServiceImpl(EduCourseRepository eduCourseRepository, EduCourseVideoRepository eduCourseVideoRepository) {
        this.eduCourseRepository = eduCourseRepository;
        this.eduCourseVideoRepository = eduCourseVideoRepository;
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
        // 初始化数据
        eduCourse.setBuyCount(0L);
        eduCourse.setViewCount(0L);
        eduCourse.setVipCount(0L);
        eduCourse.setVersion(1L);
        eduCourse.setStatus("Draft");
       eduCourseRepository.save(eduCourse);
    }

    /**
     * 根据ID获取课程详情信息
     * @param id
     * @return
     */
    @Override
    public EduCourse getById(Long id) {
        return eduCourseRepository.findById(id).orElseGet(EduCourse::new);
    }

    /**
     * 更新课程信息
     * @param eduCourse
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editCourse(EduCourse eduCourse) {
        // 根据课程ID获取课程信息
        EduCourse dbEduCourse = getById(eduCourse.getId());
        BeanUtil.copyProperties(eduCourse,dbEduCourse, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        eduCourseRepository.save(dbEduCourse);
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

    /**
     * 获取指定教师的十门课程
     * @param id
     * @param pageable
     * @return
     */
    @Override
    public List<EduCourse> findListByTeacherId(Long id, Pageable pageable) {
        return eduCourseRepository.findListByTeacherId(id,pageable);
    }

    /**
     * 根据课程ID更新课程购买数量
     * @param courseId
     */
    @Override
    public void updateBuyCount(Long courseId) {
        EduCourse  eduCourse = getById(courseId);
        eduCourse.setBuyCount(eduCourse.getBuyCount()==null?1:eduCourse.getBuyCount()+1);
        eduCourseRepository.save(eduCourse);
    }

    /**
     * 保存课程封面
     * @param courseId
     * @param urlPath
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void uploadCover(Long courseId, String urlPath) {
        // 根据课程ID获取课程信息
       EduCourse dbEduCourse = eduCourseRepository.getReferenceById(courseId);
       if(dbEduCourse!=null){
           dbEduCourse.setCover(urlPath);
           eduCourseRepository.save(dbEduCourse);
       }
    }

    /**
     * 更新课程状态
     * @param courseId
     * @param status
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(Long courseId, String status) {
        // 根据课程ID获取课程信息
        EduCourse dbEduCourse = eduCourseRepository.getReferenceById(courseId);
        if(dbEduCourse!=null){
            dbEduCourse.setStatus(status);
            // 如果是发布课程，则需要统计课时
            if(status.equals("Normal")){
                Integer count = eduCourseVideoRepository.countByCourseId(dbEduCourse.getId());
                dbEduCourse.setLessonNum(count);
            }
            eduCourseRepository.save(dbEduCourse);
        }else {
            throw new BadRequestException("更新课程状态失败！");
        }
    }
}
