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

    /**
     * 公众号APPid
     */
    @Column(name = "app_id", nullable = false, length = 50)
    private String appId;

    /**
     * 公众号APPSecret
     */
    @Column(name = "app_secret",nullable = false,length = 200)
    private String appSecret;

    @Column(name = "merchant_id", nullable = false, length = 50)
    private String merchantId;

    @Column(name = "merchant_serialnumber")
    private String merchantSerialnumber;

    @Column(name = "merchant_privatekey",length = 2000)
    private String merchantPrivatekey;

    @Column(name = "api_v3")
    private String apiV3;


    /**
     * 微信支付异步回调通知
     */
    @Column
    private String notifyWxPayUrl;


    /**
     * 代金券核销（或公众号平台）回调通知
     */
    @Column
    private String notifySpUrl;

}
