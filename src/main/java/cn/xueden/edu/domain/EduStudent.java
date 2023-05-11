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
     * 学号
     */
    @Column(name = "stu_no",nullable = false)
    private String stuNo;

    /**
     * 学生姓名
     */
    @Column(name = "name",nullable = false)
    private String name;


    /**
     * 性别
     */
    @Column(name = "sex",nullable = false)
    private String sex;

    /**
     * 手机号
     */
    @Column(name = "phone",nullable = false)
    private String phone;

    /**
     * QQ
     */
    @Column(name = "qq")
    private String qq;

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
