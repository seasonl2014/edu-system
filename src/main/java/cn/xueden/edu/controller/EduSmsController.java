package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduSms;
import cn.xueden.edu.service.IEduSmsService;
import org.springframework.web.bind.annotation.*;

/**功能描述：短信设置前端控制器
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("sms")
public class EduSmsController {

    private final IEduSmsService eduSmsService;

    public EduSmsController(IEduSmsService eduSmsService) {
        this.eduSmsService = eduSmsService;
    }

    @EnableSysLog("【后台】获取短信设置信息")
    @GetMapping
    public BaseResult getSmsInfo(){
        return BaseResult.success(eduSmsService.getOne());
    }

    @EnableSysLog("【后台】保存短信设置信息")
    @PostMapping
    public BaseResult saveOrUpdate(@RequestBody EduSms eduSms){
        eduSmsService.saveOrUpdate(eduSms);
        return BaseResult.success("保存成功");
    }
}
