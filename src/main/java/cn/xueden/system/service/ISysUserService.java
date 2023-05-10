package cn.xueden.system.service;

import cn.xueden.system.domain.SysUser;
import cn.xueden.system.service.dto.UserQueryCriteria;

import cn.xueden.system.vo.ModifyPwdModel;
import org.springframework.data.domain.Pageable;

/**功能描述：系统用户业务接口
 * @author:梁志杰
 * @date:2023/1/27
 * @description:cn.xueden.student.service
 * @version:1.0
 */
public interface ISysUserService {

    SysUser login(SysUser sysUser);

    /**
     * 获取用户列表数据
     * @param pageable
     * @return
     */
    Object getList(UserQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加用户信息
     * @param sysUser
     * @return
     */
    boolean addUser(SysUser sysUser);

    /**
     * 根据Id获取用户详情信息
     * @param id
     * @return
     */
    SysUser getById(Long id);

    /**
     * 更新用户信息
     * @param sysUser
     */
    void editUser(SysUser sysUser);

    /**
     * 根据ID删除用户信息
     * @param id
     */
    void deleteById(Long id);


    /**
     * 更新个人密码
     * @param modifyPwdModel
     * @return
     */
    boolean updatePwd(ModifyPwdModel modifyPwdModel);
}
