package cn.xueden.system.repository;

import cn.xueden.system.domain.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取所有未开通讲师的会员信息
     * @return
     */
    @Query(value = "select id,username  from sys_user where id not in (SELECT create_by FROM edu_teacher)",nativeQuery = true)
    List<Map<String,Object>> queryAllUserNotTeacher();
}
