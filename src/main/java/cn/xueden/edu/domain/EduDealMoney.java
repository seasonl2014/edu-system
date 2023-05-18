package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**功能描述：成交金额汇总实体类
 * @author:梁志杰
 * @date:2023/5/18
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_deal_money")
@org.hibernate.annotations.Table(appliesTo = "edu_deal_money",comment = "成交金额汇总信息表")
public class EduDealMoney extends BaseEntity {
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
     * 购买金额
     */
    private BigDecimal price;

    /**
     * 购买类型，0表示普通会员购买，其他表示VIP购买
     */
    private Integer buyType;

    /**
     * 支付渠道（ wxpay表示微信，alipay表示支付宝
     */
    private String payChannel;


}
