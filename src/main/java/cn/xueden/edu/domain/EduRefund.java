package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：退款记录实体类
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "edu_refund")
@org.hibernate.annotations.Table(appliesTo = "edu_refund",comment="退款记录信息表")
public class EduRefund extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 退款单号
     */
    @Column
    private String refundId;

    /**
     * 商户退款单号
     */
    @Column
    private String outRefundNo;

    /**
     * 交易单号
     */
    @Column
    private String transactionId;

    /**
     * 商户单号
     */
    @Column
    private String outTradeNo;

    /**
     * 退款金额
     */
    @Column
    private Long refundTotal;

    /**
     * 退款类型，0表示课程退款，1表示VIP退款
     */
    @Column
    private Integer refundType;

    /**
     * 学生ID
     */
    @Column
    private Long studentId;
}
