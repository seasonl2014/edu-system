package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.service.IEduStudentBuyVipService;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.vo.UpdateStudentInfoModel;

import cn.xueden.utils.JWTUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**功能描述：前台个人中前端控制器
 * @author:梁志杰
 * @date:2023/5/21
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("edu/front/student/center")
public class EduStudentCenterController {

    private final IEduStudentService eduStudentService;

    private final IEduStudentBuyVipService eduStudentBuyVipService;

    public EduStudentCenterController(IEduStudentService eduStudentService, IEduStudentBuyVipService eduStudentBuyVipService) {
        this.eduStudentService = eduStudentService;
        this.eduStudentBuyVipService = eduStudentBuyVipService;
    }

    @EnableSysLog("【前台】个人中心获取学员信息")
    @GetMapping("info")
    public BaseResult info(HttpServletRequest request){
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            EduStudent dbEduStudent = eduStudentService.getById(studentId);

            // 获取学员是否VIP会员
            EduStudentBuyVip dbEduStudentBuyVip = eduStudentBuyVipService.findByStudentId(studentId);
            if(dbEduStudentBuyVip!=null&&dbEduStudentBuyVip.getIsPayment()==1){
                dbEduStudent.setVipType("齐天大会员");
            }

            return BaseResult.success(dbEduStudent);
        }else {
            return BaseResult.fail("获取信息失败，请先登录！");
        }
    }

    @EnableSysLog("【前台】学员更新个人信息")
    @PostMapping("update")
    public BaseResult update(@RequestBody UpdateStudentInfoModel studentInfoModel, HttpServletRequest request){
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            eduStudentService.update(studentId,studentInfoModel);
            return BaseResult.success("更新成功！");
        }else {
            return BaseResult.fail("修改失败，请先登录！");
        }
    }

}
