package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：代金券发放记录实体类
 * @author:梁志杰
 * @date:2023/9/7
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_coupon_grant_record")
@org.hibernate.annotations.Table(appliesTo = "edu_coupon_grant_record",comment = "代金券发放记录表")
public class EduCouponGrantRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 代金券id 说明：发放给用户的代金券id
     */
    @Column
    private String stuCouponId;

    /**
     * 创建批次的商户号
     */
    @Column
    private String stockCreatorMchid;

    /**
     * 批次号 说明：批次id
     */
    @Column
    private String stockId;

    /**
     * 代金券名称 说明：代金券名称
     */
    @Column
    private String couponName;

    /**
     * 代金券状态 说明：代金券状态：SENDED-可用，USED-已实扣，EXPIRED-已过期
     */
    @Column
    private String status;

    /**
     * 可用开始时间 说明：可用开始时间
     */
    @Column
    private String availableBeginTime;

    /**
     * 可用结束时间 说明：可用结束时间
     */
    @Column
    private String availableEndTime;

    /**
     * 面额 说明：面额，单位分
     */
    @Column
    private Long couponAmount;

    /**
     * 门槛 说明：使用券金额门槛，单位分
     */
    @Column
    private Long transactionMinimum;

    /**
     * 学员ID
     */
    @Column
    private Long studentId;
}
