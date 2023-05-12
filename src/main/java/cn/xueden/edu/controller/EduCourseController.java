package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduCourse;

import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.dto.EduCourseQueryCriteria;

import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public EduCourseController(IEduCourseService eduCourseService) {
        this.eduCourseService = eduCourseService;
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
}
