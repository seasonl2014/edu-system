package cn.xueden.system.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.system.domain.SysUser;
import cn.xueden.system.service.ISysUserService;

import cn.xueden.utils.HutoolJWTUtil;
import cn.xueden.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;



/**功能描述：系统后台登录前端控制器
 * @author:梁志杰
 * @date:2023/1/27
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
public class LoginController {



    private final ISysUserService sysUserService;

    public LoginController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    /**
     * 系统登录
     * @param sysUser
     * @param request
     * @return
     */
    @PostMapping("login")
    @EnableSysLog("用户登录酒店后台管理系统")
    public BaseResult login(@RequestBody SysUser sysUser,
                            HttpServletRequest request){
        SysUser dbSysUser = sysUserService.login(sysUser);
        if(dbSysUser==null){
            return BaseResult.fail("登录失败，账号不存在");
        } else if (!dbSysUser.getPassword().equals(Md5Util.Md5(sysUser.getPassword()))) {
            return BaseResult.fail("登录失败，密码不正确");
        } else if (dbSysUser.getStatus()==0) {
            return BaseResult.fail("登录失败，账号被封禁");
        }else {
            // 生成token
            String token = HutoolJWTUtil.createToken(dbSysUser);
            request.getServletContext().setAttribute("token",token);

            // 返回登录用户信息
            Map<String,Object> resultMap = new HashMap<String,Object>();
            resultMap.put("username",dbSysUser.getUsername());
            resultMap.put("realname",dbSysUser.getRealname());
            resultMap.put("token",token);
            resultMap.put("email",dbSysUser.getEmail());
            resultMap.put("sex",dbSysUser.getSex());
            resultMap.put("createTime",dbSysUser.getCreateTime());
            resultMap.put("userIcon",dbSysUser.getUserIcon());
            resultMap.put("role",dbSysUser.getSysRole());

            return BaseResult.success("登录成功",resultMap);
        }
    }

    /**
     * 退出系统
     * @param request
     * @return
     */
    @GetMapping("logOut")
    @EnableSysLog("退出酒店后台系统")
    public BaseResult logOut(HttpServletRequest request){
       request.getServletContext().removeAttribute("token");
       return BaseResult.success("退出成功");
    }

}
