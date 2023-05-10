package cn.xueden.system.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：系统用户实体类
 * @author:梁志杰
 * @date:2023/1/26
 * @description:cn.xueden.student.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "sys_user")
@org.hibernate.annotations.Table(appliesTo = "sys_user",comment = "系统用户信息表")
public class SysUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 登录用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 登录密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 真实姓名
     */
    @Column(name = "realname")
    private String realname;

    /**
     * 用户性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 用户状态
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 邮箱
     */
    @Column(name="email")
    private String email;

    /**
     * 用户头像
     */
    @Column(name = "user_icon")
    private String userIcon;

    /**
     * 所属角色
     */
    @OneToOne
    @JoinColumn(name = "role_id",referencedColumnName = "id")
    private SysRole sysRole;

}
