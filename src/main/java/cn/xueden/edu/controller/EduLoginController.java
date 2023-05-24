package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.service.IEduStudentIdService;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.utils.XuedenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
