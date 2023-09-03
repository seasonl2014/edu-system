package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduWxCode;
import cn.xueden.edu.service.IEduWxCodeService;
import org.springframework.web.bind.annotation.*;

/**功能描述：微信扫码登录配置信息前端控制器
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("wxCode")
public class EduWxCodeController {

    private IEduWxCodeService eduWxCodeService;

    public EduWxCodeController(IEduWxCodeService eduWxCodeService) {
        this.eduWxCodeService = eduWxCodeService;
    }

    /**
     * 获取微信扫码登录配置信息
     * @return
     */
    @EnableSysLog("获取微信扫码登录配置信息")
    @GetMapping
    public BaseResult getWxCodeInfo(){
        return BaseResult.success(eduWxCodeService.getOne());
    }

    /**
     * 保存微信扫码登录配置信息
     * @param eduWxCode
     * @return
     */
    @EnableSysLog("保存微信扫码登录配置信息")
    @PostMapping
    public BaseResult saveOrUpdate(@RequestBody EduWxCode eduWxCode){
        eduWxCodeService.saveOrUpdate(eduWxCode);
        return BaseResult.success("保存成功");
    }
}
