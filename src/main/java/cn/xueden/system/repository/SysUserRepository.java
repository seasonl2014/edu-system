package cn.xueden.system.repository;

import cn.xueden.system.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**功能描述：系统用户持久层
 * @author:梁志杰
 * @date:2023/1/27
 * @description:cn.xueden.student.repository
 * @version:1.0
 */
public interface SysUserRepository extends JpaRepository<SysUser,Long>, JpaSpecificationExecutor<SysUser> {

    /**
     * 根据登录名查找用户信息
     * @param username
     * @return
     */
    SysUser findByUsername(String username);

}
