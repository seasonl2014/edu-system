package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.service.IEduBannerService;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.IEduSubjectService;
import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.edu.vo.EduSubjectModel;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**功能描述：前台首页前端控制器
 * @author:梁志杰
 * @date:2023/5/13
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("edu/front/index")
public class EduIndexController {

    private final IEduSubjectService eduSubjectService;

    private final IEduBannerService eduBannerService;

    private final IEduCourseService eduCourseService;

    public EduIndexController(IEduSubjectService eduSubjectService, IEduBannerService eduBannerService, IEduCourseService eduCourseService) {
        this.eduSubjectService = eduSubjectService;
        this.eduBannerService = eduBannerService;
        this.eduCourseService = eduCourseService;
    }

    @EnableSysLog("【前台】获取栏目和课程")
    @GetMapping("getIndexColumnCourses")
    public BaseResult getIndexColumnCourses(EduSubjectQueryCriteria queryCriteria){
        int pageNum = 1;
        int pageSize = 6;
        Pageable pageable = PageRequest.of(pageNum-1, pageSize,
                Sort.Direction.DESC,"sort");
        List<EduSubjectModel> eduSubjectModels = eduSubjectService.getIndexColumnCourses(queryCriteria,pageable);
        return BaseResult.success(eduSubjectModels);
    }

    @EnableSysLog("【前台】首页获取幻灯片")
    @GetMapping("getBanner")
    public BaseResult getBanner(PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"level");
        return BaseResult.success(eduBannerService.findByStatus(pageable));
    }

    @EnableSysLog("【前台】获取首页各类别课程")
    @GetMapping("findIndexCourseList")
    public BaseResult findIndexCourseList(PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return BaseResult.success(eduCourseService.findIndexCourseList(pageable));
    }

}
