package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.service.IEduSubjectService;
import cn.xueden.edu.service.dto.EduStudentQueryCriteria;
import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**功能描述：课程分类前端控制器
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("subject")
public class EduSubjectController {

    private final IEduSubjectService eduSubjectService;

    public EduSubjectController(IEduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    @EnableSysLog("【后台】获取课程分类树形结构")
    @GetMapping("/categoryTree")
    public ResponseEntity<Object> getList(EduSubjectQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduSubjectService.categoryTree(queryCriteria, pageVo), HttpStatus.OK);
    }

}
