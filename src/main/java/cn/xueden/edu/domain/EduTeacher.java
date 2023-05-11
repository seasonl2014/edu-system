package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：讲师实体类
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_teacher")
@org.hibernate.annotations.Table(appliesTo = "edu_teacher",comment = "讲师信息表")
public class EduTeacher extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;


    /**
     * 教师姓名
     */
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * 教师性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * QQ
     */
    @Column(name = "qq")
    private String qq;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;
}
