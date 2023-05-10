package cn.xueden.system.service;

import cn.xueden.system.domain.SysRole;
import cn.xueden.system.service.dto.RoleQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**功能描述：系统角色接口
 * @author:梁志杰
 * @date:2023/2/7
 * @description:cn.xueden.student.service
 * @version:1.0
 */
public interface IRoleService {

    /**
     * 获取角色列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(RoleQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 新增角色信息
     * @param sysRole
     * @return
     */
    boolean addRole(SysRole sysRole);

    /**
     * 根据ID获取详情
     * @param id
     * @return
     */
    SysRole getById(Long id);

    /**
     * 更新角色信息
     * @param sysRole
     */
    void editRole(SysRole sysRole);

    /**
     * 根据ID角色信息
     * @param id
     */
    void deleteById(Long id);

    /**
     * 获取所有角色列表
     * @return
     */
    List<SysRole> queryAll();
}
