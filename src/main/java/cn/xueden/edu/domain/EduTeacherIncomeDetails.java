package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：讲师收入明细实体类
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_teacher_income_details")
@org.hibernate.annotations.Table(appliesTo = "edu_teacher_income_details",comment = "讲师收入明细信息表")
public class EduTeacherIncomeDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 学员ID
     */
    private Long studentId;

    /**
     * 课程讲师ID
     */
    private Long teacherId;


    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 课程销售价格
     */
    private Double price;

    /**
     * 课程收益
     */
    private Double income;
}
