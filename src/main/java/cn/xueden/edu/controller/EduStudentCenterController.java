package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.domain.EduStudent;
import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.service.IEduStudentBuyVipService;
import cn.xueden.edu.service.IEduStudentService;
import cn.xueden.edu.vo.PassWordModel;
import cn.xueden.edu.vo.UpdateStudentInfoModel;

import cn.xueden.system.domain.SysUser;
import cn.xueden.utils.JWTUtil;
import cn.xueden.utils.PageVo;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @EnableSysLog("【前台】个人中心获取学员我的课程")
    @GetMapping("getMyCourseList")
    public BaseResult getMyCourseList(PageVo pageVo, HttpServletRequest request){

        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");

        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
           return BaseResult.success(eduStudentService.getMyCourseList(studentId,pageable));
        }else {
            return BaseResult.fail("获取数据失败，请先登录！");
        }
    }

    @EnableSysLog("【前台】个人中心获取我的VIP记录")
    @GetMapping("getMyVipList")
    public BaseResult getMyVipList(HttpServletRequest request){
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            return BaseResult.success(eduStudentBuyVipService.findByStudentId(studentId));
        }else {
            return BaseResult.fail("获取数据失败，请先登录！");
        }
    }

    @PutMapping("bindEmail")
    @EnableSysLog("【前台】个人中心绑定邮箱")
    public BaseResult bindEmail(@RequestParam("code")Integer code,
                                  @RequestParam("email")String email,
                                  HttpServletRequest request){

        if(null==code||null==email){
            return BaseResult.fail("验证码不存在或者邮箱不存在");
        }
        Integer contextCode = (Integer) request.getServletContext().getAttribute("code");
        if(null==contextCode){
            return BaseResult.fail("验证码已过期");
        }
        if(!contextCode.equals(code)){
            return BaseResult.fail("验证码输入不正确，请重新输入！");
        }

        // 登录用户ID
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            eduStudentService.bindEmail(email,studentId);
            return  BaseResult.success("绑定邮箱成功");
        }else {
            return BaseResult.fail("获取数据失败，请先登录！");
        }

    }

    @EnableSysLog("【前台】个人中心发送手机验证码")
    @PostMapping("sendSms/{phone}")
    public BaseResult sendSms(@PathVariable String phone,HttpServletRequest request){
        // 登录用户ID
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            Integer phoneCode =  eduStudentService.sendSms(phone,studentId);
            request.getServletContext().setAttribute("phoneCode",phoneCode);
            return  BaseResult.success("发送手机验证码成功，请注意查询");
        }else {
            return BaseResult.fail("发送失败，请先登录！");
        }

    }

    @EnableSysLog("【前台】个人中心更换手机")
    @PostMapping("updatePhone")
    public BaseResult updatePhone(@RequestParam("phone") String phone,
                                  @RequestParam("phoneCode") String phoneCode,
                                  HttpServletRequest request){
        // 登录用户ID
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            Integer contextPhoneCode = (Integer) request.getServletContext().getAttribute("phoneCode");
            if(null==contextPhoneCode){
                return BaseResult.fail("验证码已经过期");
            }
            if(!contextPhoneCode.equals(phoneCode)){
                return BaseResult.fail("验证码输入不正确，请重新输入");
            }
            eduStudentService.updatePhone(phone,studentId);
            return  BaseResult.success("发送手机验证码成功，请注意查询");
        }else {
            return BaseResult.fail("发送失败，请先登录！");
        }

    }

    @EnableSysLog("【前台】个人中心更改密码")
    @PutMapping("savePassWord")
    public BaseResult savePassWord(@RequestBody PassWordModel passWordModel,
                                   HttpServletRequest request){
        // 登录用户ID
        String token = request.getHeader("studentToken");
        if(token!= null && !token.equals("null")&& !token.equals("")) {
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId = Long.parseLong(decodedJWT.getClaim("studentId").asString());
            eduStudentService.savePassWord(passWordModel,studentId);
        }
        return BaseResult.success("修改成功");
    }

}
