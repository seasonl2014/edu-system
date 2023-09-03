package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduEmail;
import cn.xueden.edu.service.IEduEmailService;
import org.springframework.web.bind.annotation.*;

/**功能描述：邮箱设置前端控制器
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("email")
public class EduEmailController {

    private final IEduEmailService eduEmailService;

    public EduEmailController(IEduEmailService eduEmailService) {
        this.eduEmailService = eduEmailService;
    }

    @EnableSysLog("【后台】获取邮箱配置信息")
    @GetMapping
    public BaseResult getEmailInfo(){
        return BaseResult.success(eduEmailService.getOne());
    }

    @PostMapping
    @EnableSysLog("【后台】保存邮箱配置信息")
    public BaseResult saveOrUpdate(@RequestBody EduEmail eduEmail){
        eduEmailService.saveOrUpdate(eduEmail);
        return BaseResult.success("保存成功");
    }
}
