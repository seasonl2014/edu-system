package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.service.IEduStudentIdService;
import cn.xueden.edu.service.dto.EduStudentIdQueryCriteria;
import cn.xueden.edu.service.dto.EduStudentQueryCriteria;
import cn.xueden.edu.vo.StudentIdModel;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：学生学号前端控制器
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("studentId")
public class EduStudentIdController {

    private final IEduStudentIdService eduStudentIdService;

    public EduStudentIdController(IEduStudentIdService eduStudentIdService) {
        this.eduStudentIdService = eduStudentIdService;
    }

    @EnableSysLog("【后台】获取学生学号列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduStudentIdQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduStudentIdService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】生成学生学号")
    @PostMapping
    public BaseResult addStudentId(@RequestBody StudentIdModel studentIdModel){
        eduStudentIdService.addStudentId(studentIdModel);
        return BaseResult.success("生成成功");
    }
}
