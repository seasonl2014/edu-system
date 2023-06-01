package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.service.IEduStudentIdService;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.vo.PassWordModel;
import cn.xueden.utils.XuedenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**功能描述：学生登录前端控制器
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("edu/front")
public class EduLoginController {

    private final IEduStudentService studentService;

    public EduLoginController(IEduStudentService studentService) {
        this.studentService = studentService;
    }

    @EnableSysLog("【前台】学员登录")
    @PostMapping("login")
    public BaseResult login(@RequestBody EduStudent student,
                            HttpServletRequest request){
        // 获取用户IP地址
        String ipAddress = XuedenUtil.getClientIp(request);
        return studentService.login(student,ipAddress);
    }

    @EnableSysLog("【前台】找回密码发送手机验证码")
    @PostMapping("findPwd/sendSms/{phone}")
    public BaseResult findPwdSendSms(@PathVariable String phone, HttpServletRequest request){
        if(request.getServletContext().getAttribute("phoneCode")==null){
            Integer phoneCode =  studentService.findPwdSendSms(phone);
            request.getServletContext().setAttribute("phoneCode",phoneCode.toString());
        }
        return  BaseResult.success("发送手机验证码成功，请注意查询");
    }

    @EnableSysLog("【前台】保存重新设置的密码")
    @PutMapping("saveFindPassWord")
    public BaseResult saveFindPassWord(@RequestBody PassWordModel passWordModel, HttpServletRequest request){
        if(request.getServletContext().getAttribute("phoneCode")==null){
            return BaseResult.fail("保存密码失败，手机验证码失效");
        }else{
            String servletContextCode = (String) request.getServletContext().getAttribute("phoneCode");
            if(!servletContextCode.equals(passWordModel.getCode())){
                return BaseResult.fail("手机验证码不正确，请重新输入！");
            }
        }
        studentService.saveFindPassWord(passWordModel);
        return  BaseResult.success("保存密码成功");
    }



}
