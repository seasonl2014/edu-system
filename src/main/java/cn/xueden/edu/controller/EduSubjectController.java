package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.service.IEduSubjectService;
import cn.xueden.edu.service.dto.EduStudentQueryCriteria;
import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.edu.vo.EduSubjectModel;
import cn.xueden.edu.vo.EduSubjectTreeNodeModel;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @EnableSysLog("【后台】添加课程分类")
    @PostMapping
    public BaseResult add(@RequestBody EduSubjectModel eduSubjectModel) {
        eduSubjectService.add(eduSubjectModel);
        return BaseResult.success("添加成功");
    }


    @GetMapping("/{id}")
    @EnableSysLog("【后台】根据id获取课程分类详情信息")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        EduSubject dbEduSubject = eduSubjectService.getById(id);
        return BaseResult.success(dbEduSubject);
    }

    @PutMapping
    @EnableSysLog("【后台】更新课程分类信息")
    public BaseResult EditRoom(@RequestBody EduSubject eduSubject){
        eduSubjectService.editSubject(eduSubject);
        return BaseResult.success("更新成功");
    }

    @EnableSysLog("【后台】新增或修改分类获取课程分类树形结构")
    @GetMapping("/getParentEduSubjectTreeNode")
    public BaseResult getParentEduSubjectTreeNode() {
        List<EduSubjectTreeNodeModel> parentTree = eduSubjectService.getParentEduSubjectTree();
        return BaseResult.success(parentTree);
    }


}
