package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduCourse;

import cn.xueden.edu.domain.EduCourseData;
import cn.xueden.edu.domain.EduEnvironmenParam;
import cn.xueden.edu.service.IEduCourseDataService;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.IEduEnvironmenParamService;
import cn.xueden.edu.service.dto.EduCourseQueryCriteria;

import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**功能描述：课程前端控制器
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("course")
public class EduCourseController {
    
    private final IEduCourseService eduCourseService;

    private final IEduEnvironmenParamService eduEnvironmenParamService;

    private final IEduCourseDataService eduCourseDataService;

    public EduCourseController(IEduCourseService eduCourseService, IEduEnvironmenParamService eduEnvironmenParamService, IEduCourseDataService eduCourseDataService) {
        this.eduCourseService = eduCourseService;
        this.eduEnvironmenParamService = eduEnvironmenParamService;
        this.eduCourseDataService = eduCourseDataService;
    }

    @EnableSysLog("【后台】获取课程列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduCourseQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduCourseService.getList(queryCriteria,pageable), HttpStatus.OK);
    }


    @EnableSysLog("【后台】添加课程信息")
    @PostMapping
    public BaseResult addCourse(@RequestBody EduCourse eduCourse){
        eduCourseService.addCourse(eduCourse);
        return BaseResult.success("添加成功");
    }

    @EnableSysLog("【后台】根据ID获取课程详情信息")
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        EduCourse dbCourse = eduCourseService.getById(id);
        return BaseResult.success(dbCourse);
    }


    @EnableSysLog("【后台】更新课程信息")
    @PutMapping
    public BaseResult editCourse(@RequestBody EduCourse eduCourse){
        eduCourseService.editCourse(eduCourse);
        return BaseResult.success("更新成功");
    }


    @EnableSysLog("【后台】根据id删除课程信息")
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        eduCourseService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    @EnableSysLog("【后台】更新课程状态")
    @PutMapping("updateStatus")
    public BaseResult updateStatus(@RequestParam("courseId") Long courseId,
                                   @RequestParam("status") String status){
        eduCourseService.updateStatus(courseId,status);
        return BaseResult.success("更新课程状态成功");

    }

    @EnableSysLog("【后台】保存或更新课程环境")
    @PostMapping("saveOrUpdateEnvironmen")
    public BaseResult saveOrUpdateEnvironmen(@RequestBody List<EduEnvironmenParam> eduEnvironmenParamList){
        eduEnvironmenParamService.saveOrUpdateEnvironmen(eduEnvironmenParamList);
        return BaseResult.success("保存课程环境成功");
    }

    @EnableSysLog("【后台】根据课程ID获取环境参数列表数据")
    @GetMapping("getEnvironmenList/{courseId}")
    public BaseResult getEnvironmenList(@PathVariable Long courseId){
       return BaseResult.success(eduEnvironmenParamService.getEduEnvironmenParamListByCourseId(courseId)) ;
    }

    @EnableSysLog("【后台】根据课程ID获取课程资料数列表数据")
    @GetMapping("getCourseDataList/{courseId}")
    public BaseResult getCourseDataList(@PathVariable Long courseId){
        return BaseResult.success(eduCourseDataService.getCourseDataByCourseId(courseId)) ;
    }

    @EnableSysLog("【后台】保存或更新课程配套资料")
    @PostMapping("saveOrUpdateCourseData")
    public BaseResult saveOrUpdateCourseData(@RequestBody List<EduCourseData> eduCourseDataList){
        eduCourseDataService.saveOrUpdateCourseData(eduCourseDataList);
        return BaseResult.success("保存课程资料成功");
    }



}
