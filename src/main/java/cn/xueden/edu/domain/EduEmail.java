package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
 
/**功能描述：邮箱设置实体类
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "edu_email")
@org.hibernate.annotations.Table(appliesTo = "edu_email",comment="邮箱配置信息表")
public class EduEmail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 邮箱主机地址
     */
    @Column(name = "email_host")
    private String host;

    /**
     *邮箱端口号
     */
    @Column(name = "email_port")
    private String port;

    /**
     * 邮箱账号
     */
    @Column
    private String userName;

    /**
     * 邮箱授权密码
     */
    @Column
    private String password;
}
