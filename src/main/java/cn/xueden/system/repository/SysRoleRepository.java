package cn.xueden.system.repository;

import cn.xueden.system.domain.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：系统角色信息持久层
 * @author:梁志杰
 * @date:2023/2/6
 * @description:cn.xueden.student.repository
 * @version:1.0
 */
public interface SysRoleRepository extends JpaRepository<SysRole,Long>, JpaSpecificationExecutor<SysRole> {
}
