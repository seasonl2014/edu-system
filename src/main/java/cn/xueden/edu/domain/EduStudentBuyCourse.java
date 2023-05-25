package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**功能描述：学员购买课程信息实体类
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_student_buy_course")
@org.hibernate.annotations.Table(appliesTo = "edu_student_buy_course",comment = "学员购买课程信息表")
public class EduStudentBuyCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;


    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 学员ID
     */
    private Long studentId;

    /**
     * 讲师ID
     */
    private Long teacherId;

    /**
     * 购买金额
     */
    private BigDecimal price;


    /**
     * 是否付款，0表示未付款，1表示已付款，2表示已退款
     */
    private Integer isPayment;

    /**
     * 支付渠道（ wxpay表示微信，alipay表示支付宝
     */
    private String payChannel;

    /**
     * 课程
     */
    @Transient
    private EduCourse eduCourse;



}
