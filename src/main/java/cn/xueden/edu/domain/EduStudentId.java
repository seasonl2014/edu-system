package cn.xueden.edu.domain;

import jakarta.persistence.*;
import lombok.Data;

/**功能描述：学生学号
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_student_id")
@org.hibernate.annotations.Table(appliesTo = "edu_student_id",comment = "学生学号表")
public class EduStudentId {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "student_id",nullable = false)
    private Long studentId;

    /**
     * 状态 0表示未选，1表示已选
     */
    private Integer status;
}
