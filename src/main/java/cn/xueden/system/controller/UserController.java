package cn.xueden.system.controller;


import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.email.MailService;
import cn.xueden.exception.BadRequestException;
import cn.xueden.system.domain.SysUser;
import cn.xueden.system.service.ISysUserService;
import cn.xueden.system.service.dto.UserQueryCriteria;

import cn.xueden.system.vo.ModifyPwdModel;
import cn.xueden.utils.HutoolJWTUtil;
import cn.xueden.utils.NativeFileUtil;
import cn.xueden.utils.PageVo;
import cn.xueden.utils.XuedenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**功能描述：系统用户前端控制器
 * @author:梁志杰
 * @date:2023/1/29
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Value("${user.icon}")
    private String userIcon;

    /**
     * 发送方
     */
    @Value("${spring.mail.username}")
    private String from;

    private final ISysUserService sysUserService;

    private final MailService mailService;

    public UserController(ISysUserService sysUserService, MailService mailService) {
        this.sysUserService = sysUserService;
        this.mailService = mailService;
    }

    /**
     * 获取用户列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    @EnableSysLog("获取用户列表数据")
    public ResponseEntity<Object> getList(UserQueryCriteria queryCriteria, PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1,pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(sysUserService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    @PostMapping
    @EnableSysLog("添加用户信息")
    public BaseResult addUser(@RequestBody SysUser sysUser){
        boolean result = sysUserService.addUser(sysUser);
        if(result){
            return BaseResult.success("添加成功");
        }else {
            return BaseResult.fail("添加失败");
        }
    }

    /**
     * 根据ID获取用户详情信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @EnableSysLog("根据ID获取用户详情信息")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        SysUser dbSysUser = sysUserService.getById(id);
        return BaseResult.success(dbSysUser);
    }

    /**
     * 更新用户信息
     * @param sysUser
     * @return
     */
    @PutMapping
    @EnableSysLog("更新用户信息")
    public BaseResult editUser(@RequestBody SysUser sysUser){
        sysUserService.editUser(sysUser);
        return BaseResult.success("更新成功");
    }

    /**
     * 删除用户信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @EnableSysLog("删除用户信息")
    public BaseResult delete(@PathVariable Long id){
        if (null==id){
            throw new BadRequestException("删除信息失败");
        }
        sysUserService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("userIcon")
    @EnableSysLog("上传头像")
    public BaseResult uploadFile(@RequestParam("fileResource")MultipartFile file){
        if(null==file){
            return BaseResult.fail("上传头像失败，文件不能为空");
        }
        try {
            String temFileResource = NativeFileUtil.uploadUserIcon(file,userIcon);
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("userIcon",temFileResource);
            return BaseResult.success(resultMap);
        }catch (Exception e){
            e.printStackTrace();
            return BaseResult.fail(e.getMessage());
        }
    }

    /**
     * 修改个人信息
     * @param sysUser
     * @return
     */
    @PutMapping("updateInfo")
    @EnableSysLog("修改个人信息")
    public BaseResult updateInfo(@RequestBody SysUser sysUser){
        // 获取登录用户ID
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = (String) request.getServletContext().getAttribute("token");
        Long userId = HutoolJWTUtil.parseToken(token);
        sysUser.setId(userId);
        sysUserService.editUser(sysUser);
        return BaseResult.success("更新成功");
    }

    /**
     * 发送邮件
     * @param email
     * @param request
     * @return
     */
    @GetMapping("sendEmail")
    @EnableSysLog("发送邮件")
    public BaseResult sendEmail(@RequestParam("email") String email,HttpServletRequest request){
        // 发送旧邮箱
        if(email==null || email==""){
            // 获取登录用户ID
            String token = (String) request.getServletContext().getAttribute("token");
            Long userId = HutoolJWTUtil.parseToken(token);
            SysUser dbSysUser = sysUserService.getById(userId);
            email = dbSysUser.getEmail();
        }
        int code = XuedenUtil.randomSixNums();
        String content = "验证码："+code+"此验证码用于更换邮箱绑定，请勿将验证码告知他人！";
        mailService.sendSimpleMail(from,email,email,"修改邮箱验证码",content);
        request.getServletContext().setAttribute("code",code);
        return BaseResult.success();
    }

    /**
     * 校验验证码
     * @param code
     * @param request
     * @return
     */
    @GetMapping("verifyCode")
    @EnableSysLog("校验验证码")
    public BaseResult verifyCode(@RequestParam("code")Integer code,
                                 HttpServletRequest request){
        if(null==code){
            return BaseResult.fail("验证码不存在");
        }

        Integer contextCode = (Integer) request.getServletContext().getAttribute("code");
        if(null==contextCode){
            return BaseResult.fail("验证码已经过期");
        }
        if(!contextCode.equals(code)){
            return BaseResult.fail("验证码输入不正确，请重新输入");
        }

        return BaseResult.success();

    }

    /**
     * 更改绑定邮箱
     * @param code
     * @param email
     * @param request
     * @return
     */
    @PutMapping("updateEmail")
    @EnableSysLog("更改绑定邮箱")
    public BaseResult updateEmail(@RequestParam("code")Integer code,
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
        String token = (String) request.getServletContext().getAttribute("token");
        Long userId = HutoolJWTUtil.parseToken(token);
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setEmail(email);
        sysUserService.editUser(sysUser);
        return BaseResult.success();

    }

    /**
     * 更改个人密码
     * @param modifyPwdModel
     * @param request
     * @return
     */
    @PutMapping("updatePwd")
    @EnableSysLog("更改个人密码")
    public BaseResult updatePwd(@RequestBody ModifyPwdModel modifyPwdModel,
                                HttpServletRequest request){
        if(modifyPwdModel==null){
            return BaseResult.fail("更新失败");
        }
        String token = (String) request.getServletContext().getAttribute("token");
        Long userId = HutoolJWTUtil.parseToken(token);
        modifyPwdModel.setUserId(userId);
        boolean result = sysUserService.updatePwd(modifyPwdModel);
        if(result){
            return BaseResult.success("更新成功");
        }else {
            return BaseResult.fail("更新失败");
        }

    }





}
