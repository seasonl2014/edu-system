package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：学生信息实体类
 * @author:梁志杰
 * @date:2023/2/17
 * @description:cn.xueden.student.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_student")
@org.hibernate.annotations.Table(appliesTo = "edu_student",comment = "学生信息表")
public class EduStudent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 登录账号
     */
    @Column(name = "login_name",nullable = false)
    private String loginName;

    /**
     * 登录密码
     */
    @Column(name = "password",nullable = false)
    private String password;

    /**
     * 学号
     */
    @Column(name = "stu_no",nullable = false)
    private String stuNo;

    /**
     * 学生姓名
     */
    @Column(name = "name")
    private String name;

    /**
     * 学员登录次数
     */
    @Column(name = "login_times")
    private Integer loginTimes;


    /**
     * 性别
     */
    @Column(name = "sex")
    private String sex;

    /**
     * 手机号
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * QQ
     */
    @Column(name = "qq")
    private String qq;

    /**
     * 绑定微信扫码登录
     */
    @Column(name = "wx_open_id")
    private String wxOpenId;

    /**
     * 状态，1表示启用，0表示禁用
     */
    @Column(name = "status",nullable = false)
    private Integer status;

    /**
     * 学员头像
     */
    @Column(name = "student_icon")
    private String studentIcon;

    /**
     * 学员登录后的token
     */
    @Transient
    private String studentToken;



    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", stuno='" + stuNo + '\'' +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", phone='" + phone + '\'' +
                ", qq='" + qq + '\'' +
                '}';
    }
}
