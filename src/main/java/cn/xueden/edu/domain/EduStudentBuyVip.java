package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**功能描述：学员购买VIP实体类
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_student_buy_vip")
@org.hibernate.annotations.Table(appliesTo = "edu_student_buy_vip",comment = "学生购买VIP信息表")
public class EduStudentBuyVip extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 学员ID
     */
    private Long studentId;

    /**
     * 会员类型ID
     */
    private Long vipId;

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
     * 微信支付平台内部生成的订单号
     */
    private String transactionId;

    /**
     * 代金券编号
     */
    private String couponId;

    /**
     * 代金券所属批次号
     */
    private String stockId;


}
