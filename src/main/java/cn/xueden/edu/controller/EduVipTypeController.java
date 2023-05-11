package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;

import cn.xueden.edu.domain.EduVipType;
import cn.xueden.edu.service.IEduVipTypeService;

import cn.xueden.edu.service.dto.EduVipTypeQueryCriteria;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：VIP类别前端控制器
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("vipType")
public class EduVipTypeController {

    private final IEduVipTypeService eduVipTypeService;

    public EduVipTypeController(IEduVipTypeService eduVipTypeService) {
        this.eduVipTypeService = eduVipTypeService;
    }

    @EnableSysLog("【后台】获取VIP类别列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduVipTypeQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduVipTypeService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】添加类别数据")
    @PostMapping
    public BaseResult addEduVipType(@RequestBody EduVipType eduVipType){
        eduVipTypeService.addEduVipType(eduVipType);
        return BaseResult.success("添加成功");
    }

}
