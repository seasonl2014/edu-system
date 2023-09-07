package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：代金券批次实体类
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_coupon_stock")
@org.hibernate.annotations.Table(appliesTo = "edu_coupon_stock",comment = "代金券批次信息表")
public class EduCouponStock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 批次号 说明：批次号
     * */
    @Column
    private String stockId;

    /**
     * 批次名称 说明：批次名称
     */
    @Column
    private String stockName;

    /**
     * 状态 0表示未生成，1表示已生成
     */
    @Column
    private Integer status;

    /**
     * 归属商户号 说明：批次归属商户号
     */
    @Column
    private String belongMerchant;

    /**
     * 开始时间 说明：批次开始时间
     */
    @Column
    private String availableBeginTime;

    /**
     * 结束时间 说明：批次结束时间
     */
    @Column
    private String availableEndTime;

    /**
     * 是否无资金流 说明：是否无资金流，true-是；false-否
     */
    @Column
    private Boolean noCash;

    /**
     * 批次类型 说明：批次类型，NORMAL-固定面额满减券批次；
     * DISCOUNT-折扣券批次；EXCHAHGE-换购券批次；
     * RANDOM-千人千面券批次
     */
    @Column
    private String stockType;

    /**
     * 商户单据号 说明：商户创建批次凭据号（格式：商户id+日期+流水号），商户侧需保持唯一性
     */
    @Column
    private String outRequestNo;

    /**
     * 发放总上限 说明：最大发券数
     */
    @Column
    private Long maxCoupons;

    /**
     * 总预算 说明：总消耗金额，单位分
     */
    @Column
    private Long maxAmount;

    /**
     * 单天发放上限金额 说明：单天最高消耗金额，单位分
     */
    @Column
    private Long maxAmountByDay;

    /**
     * 单个用户可领个数 说明：单个用户可领个数
     */
    @Column
    private Integer maxCouponsPerUser;

    /**
     * 是否开启自然人限制 说明：true-是；false-否，默认否
     */
    @Column
    private Boolean naturalPersonLimit;

    /**
     *api发券防刷 说明：true-是；false-否，默认否
     */
    @Column
    private Boolean preventApiAbuse;

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



}
