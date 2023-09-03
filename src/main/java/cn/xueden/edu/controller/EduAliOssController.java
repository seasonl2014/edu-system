package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduAliOss;
import cn.xueden.edu.service.IEduAliOssService;
import org.springframework.web.bind.annotation.*;

/**功能描述：阿里云对象存储OSS前端控制器
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("aliOss")
public class EduAliOssController {

    private final IEduAliOssService eduAliOssService;

    public EduAliOssController(IEduAliOssService eduAliOssService) {
        this.eduAliOssService = eduAliOssService;
    }

    @EnableSysLog("【后台】获取阿里云对象存储OSS配置信息")
    @GetMapping
    public BaseResult getAliOssInfo(){
        return BaseResult.success(eduAliOssService.getOne());
    }

    @EnableSysLog("【后台】保存阿里云对象存储OSS配置信息")
    @PostMapping
    public BaseResult saveOrUpdate(@RequestBody EduAliOss eduAliOss){
        eduAliOssService.saveOrUpdate(eduAliOss);
        return BaseResult.success("保存成功");
    }
}
