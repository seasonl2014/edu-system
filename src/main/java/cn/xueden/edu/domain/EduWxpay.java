package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：文件存储位置实体类
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "edu_wxpay")
@org.hibernate.annotations.Table(appliesTo = "edu_wxpay",comment="微信支付配置信息表")
public class EduWxpay extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "app_id", nullable = false, length = 50)
    private String appId;

    @Column(name = "merchant_id", nullable = false, length = 50)
    private String merchantId;

    @Column(name = "merchant_serialnumber")
    private String merchantSerialnumber;

    @Column(name = "merchant_privatekey",length = 2000)
    private String merchantPrivatekey;

    @Column(name = "api_v3")
    private String apiV3;


    /**
     * 购买VIP会员异步回调通知
     */
    @Column
    private String notifyVipUrl;

    /**
     * 购买课程异步回调通知
     */
    @Column
    private String notifyCourseUrl;

}
