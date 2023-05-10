package cn.xueden.system.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.exception.BadRequestException;
import cn.xueden.system.domain.SysRole;
import cn.xueden.system.service.IRoleService;
import cn.xueden.system.service.dto.RoleQueryCriteria;
import cn.xueden.utils.PageVo;
import cn.xueden.utils.ResultVo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**功能描述：系统角色前端控制器
 * @author:梁志杰
 * @date:2023/2/7
 * @description:cn.xueden.student.controller
 * @version:1.0
 */
@RestController
@RequestMapping("role")
public class RoleController {

    private final IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 获取角色列表数据
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @GetMapping
    @EnableSysLog("获取角色列表数据")
    public ResponseEntity<Object> getList(RoleQueryCriteria queryCriteria,
                                          PageVo pageVo){
        Pageable pageable = PageRequest.of(pageVo.getPageIndex()-1, pageVo.getPageSize(),
                Sort.Direction.DESC,"id");
        return new ResponseEntity<>(roleService.getList(queryCriteria,pageable), HttpStatus.OK);
    }

    /**
     * 添加角色信息
     * @param sysRole
     * @return
     */
    @PostMapping
    @EnableSysLog("添加角色信息")
    public BaseResult addRole(@RequestBody SysRole sysRole){
        boolean result = roleService.addRole(sysRole);
        if (result){
            return BaseResult.success("添加角色成功");
        }else {
            return BaseResult.fail("添加角色失败");
        }
    }

    /**
     * 根据ID获取角色详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @EnableSysLog("根据ID获取角色详情")
    public BaseResult detail(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("获取信息失败");
        }
        SysRole dbSysRole = roleService.getById(id);
        return BaseResult.success(dbSysRole);
    }

    /**
     * 更新角色信息
     * @param sysRole
     * @return
     */
    @PutMapping
    @EnableSysLog("更新角色信息")
    public BaseResult editRole(@RequestBody SysRole sysRole){
        roleService.editRole(sysRole);
        return BaseResult.success("更新成功");
    }

    /**
     * 根据ID删除角色信息
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @EnableSysLog("根据ID删除角色信息")
    public BaseResult delete(@PathVariable Long id){
        if(null==id){
            throw new BadRequestException("删除信息失败");
        }
        roleService.deleteById(id);
        return BaseResult.success("删除成功");
    }

    /**
     * 获取所有角色信息列表
     * @return
     */
    @GetMapping("/all")
    @EnableSysLog("获取所有角色信息列表")
    public BaseResult getAll(){
        List<SysRole> list = roleService.queryAll();
        List<ResultVo> result = list.stream().map(temp-> {
            ResultVo obj = new ResultVo();
            obj.setId(temp.getId());
            obj.setName(temp.getName());
            return  obj;
        }).collect(Collectors.toList());
        return BaseResult.success(result);
    }

}
