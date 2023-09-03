package cn.xueden.edu.controller;

import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduWxpay;
import cn.xueden.edu.service.IEduWxpayService;
import org.springframework.web.bind.annotation.*;

/**功能描述：微信支付设置前端控制器
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("wxPay")
public class EduWxPayController {

    private final IEduWxpayService eduWxpayService;

    public EduWxPayController(IEduWxpayService eduWxpayService) {
        this.eduWxpayService = eduWxpayService;
    }

    /**
     * 获取一条微信支付配置信息
     * @return
     */
    @GetMapping
    public BaseResult getWxPayInfo(){
        return BaseResult.success(eduWxpayService.getOne());
    }

    @PostMapping
    public BaseResult saveWxPayInfo(@RequestBody EduWxpay eduWxpay){
        eduWxpayService.save(eduWxpay);
        return BaseResult.success("保存成功");
    }
}
