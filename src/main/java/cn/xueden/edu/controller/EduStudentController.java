package cn.xueden.edu.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.service.dto.EduStudentQueryCriteria;
import cn.xueden.exception.BadRequestException;


import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：学生信息管理前端控制器
 * @author:梁志杰
 * @date:2023/2/17
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("student")
public class EduStudentController {

    private final IEduStudentService studentService;

    public EduStudentController(IEduStudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 获取学生列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    public ResponseEntity<Object> getList(EduStudentQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(studentService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加学生信息
     * @param student
     * @return
     */
    @PostMapping
    public BaseResult addStudent(@RequestBody EduStudent student){
        boolean result = studentService.addStudent(student);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取学生详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        EduStudent dbStudent = studentService.getById(id);
        return BaseResult.success(dbStudent);
    }

    /**
     * 更新学生信息
     * @param student
     * @return
     */
    @PutMapping
    public BaseResult editStudent(@RequestBody EduStudent student){
        studentService.editStudent(student);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据id删除学生信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        studentService.deleteById(id);
        return BaseResult.success("删除成功");
    }
}
