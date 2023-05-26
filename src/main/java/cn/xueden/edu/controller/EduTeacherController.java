package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduTeacher;
import cn.xueden.edu.service.IEduTeacherService;
import cn.xueden.edu.service.dto.EduTeacherQueryCriteria;
import cn.xueden.exception.BadRequestException;

import cn.xueden.utils.PageVo;
import cn.xueden.utils.ResultVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.stream.Collectors;

/**功能描述：讲师模块前端控制器
 * @author:梁志杰
 * @date:2023/2/24
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("teacher")
public class EduTeacherController {

    private final IEduTeacherService teacherService;

    public EduTeacherController(IEduTeacherService teacherService) {
        this.teacherService = teacherService;
    }

    /**
     * 获取教师列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(EduTeacherQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(teacherService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加教师信息
     * @param teacher
     * @return
     */
    @PostMapping
    public BaseResult addTeacher(@RequestBody EduTeacher teacher){
        boolean result = teacherService.addEduTeacher(teacher);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据id获取详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
       EduTeacher dbTeacher = teacherService.getById(id);
        return BaseResult.success(dbTeacher);
    }

    /**
     * 更新教师信息
     * @param teacher
     * @return
     */
    @PutMapping
    public BaseResult editTeacher(@RequestBody EduTeacher teacher){
        teacherService.editEduTeacher(teacher);
        return BaseResult.success("更新成功");
    }

    /**
     * 删除教师信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if (null==id){
            throw new BadRequestException("删除信息失败");
        }
        teacherService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    @EnableSysLog("【后台】获取所有讲师")
    @GetMapping("all")
    public BaseResult all(){
        List<EduTeacher> list = teacherService.getAll();
        List<ResultVo> resultVoList = list.stream().map(temp-> {
            ResultVo obj = new ResultVo();
            obj.setName(temp.getName());
            obj.setId(temp.getId());
            return obj;
        }).collect(Collectors.toList());

        return BaseResult.success(resultVoList);
    }

}
