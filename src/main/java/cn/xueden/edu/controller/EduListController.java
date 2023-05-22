package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.IEduSubjectService;
import cn.xueden.edu.service.dto.EduCourseQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**功能描述：前台列表前端控制器
 * @author:梁志杰
 * @date:2023/5/20
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("/edu/front/list")
public class EduListController {
    /**
     * 排序 1表示按销量排序
     */
    private final static String SORTTYPE = "1";

    private final IEduSubjectService eduSubjectService;

    private final IEduCourseService eduCourseService;

    public EduListController(IEduSubjectService eduSubjectService, IEduCourseService eduCourseService) {
        this.eduSubjectService = eduSubjectService;
        this.eduCourseService = eduCourseService;
    }

    @EnableSysLog("【前台】列表页获取数据")
    @GetMapping("get")
    public BaseResult get(EduCourseQueryCriteria queryCriteria, PageVo pageVo){

        Map<String,Object> resultMap = new HashMap<>();
        // 获取一级分类
        List<EduSubject> eduSubjectList = eduSubjectService.getListByParentId(0L);
        resultMap.put("eduSubjectList",eduSubjectList);

        List<Long> idList = eduSubjectList.stream().map(subject-> subject.getId()).collect(Collectors.toList());
        //判断前台传过来的subjectId是否是父节点，如果是则获取该节点下的子节点
        if(idList.contains(queryCriteria.getSubjectParentId())){
            List<EduSubject> sonSubjectList = eduSubjectService.getListByParentId(queryCriteria.getSubjectParentId());
            resultMap.put("sonSubjectList",sonSubjectList);
        }else { // 获取所有子分类,不包含父节点
            List<EduSubject> sonSubjectList = eduSubjectService.getListByParentIdNot(0L);
            resultMap.put("sonSubjectList",sonSubjectList);
        }
        resultMap.put("courseList",getCourseList(queryCriteria,pageVo));
        return BaseResult.success(resultMap);
    }

    /**
     * 获取课程数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    private Object getCourseList(EduCourseQueryCriteria queryCriteria, PageVo pageVo){

        String courseSort = "id";

        // 按销量排序
        if(queryCriteria.getCourseSort()!=null&&SORTTYPE.equals(queryCriteria.getCourseSort())){
            courseSort = "buyCount";
        }

        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,courseSort);
        // 表示查询全部
        if (queryCriteria.getCourseType()==-1){
            queryCriteria.setCourseType(null);
        }
        // 表示查询全部
        if(queryCriteria.getDifficulty()==-1){
            queryCriteria.setDifficulty(null);
        }
        // 表示查询二级分类全部
        if(queryCriteria.getSubjectId()!=null&&queryCriteria.getSubjectId()==0){
            queryCriteria.setSubjectId(null);
        }

        // 表示查询一级分类全部
        if(queryCriteria.getSubjectParentId()!=null&&queryCriteria.getSubjectParentId()==0){
            queryCriteria.setSubjectParentId(null);
        }

       return eduCourseService.getList(queryCriteria,pageable);
    }
}
