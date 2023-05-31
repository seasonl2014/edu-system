package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduBanner;
import cn.xueden.edu.service.IEduBannerService;
import cn.xueden.edu.service.dto.EduBannerQueryCriteria;

import cn.xueden.utils.PageVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**功能描述：轮播图（走马灯）前端控制器
 * @author:梁志杰
 * @date:2023/5/31
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("banner")
public class EduBannerController {

    private final IEduBannerService eduBannerService;

    public EduBannerController(IEduBannerService eduBannerService) {
        this.eduBannerService = eduBannerService;
    }

    /**
     * 获取学生列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @EnableSysLog("【后台】获取轮播图列表数据")
    @GetMapping
    public ResponseEntity<Object> getList(EduBannerQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(eduBannerService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    @EnableSysLog("【后台】根据ID获取轮播图数据")
    @GetMapping("{id}")
    public BaseResult detail(@PathVariable Long id){
        return BaseResult.success(eduBannerService.findById(id));
    }

    @EnableSysLog("【后台】更新轮播图数据")
    @PutMapping
    public BaseResult edit(@RequestBody EduBanner eduBanner){
        eduBannerService.updateById(eduBanner);
        return BaseResult.success("更新成功！");
    }

    @EnableSysLog("【后台】新增轮播图数据")
    @PostMapping
    public BaseResult add(@RequestBody EduBanner eduBanner){
        eduBannerService.add(eduBanner);
        return BaseResult.success("新增成功！");
    }


}
