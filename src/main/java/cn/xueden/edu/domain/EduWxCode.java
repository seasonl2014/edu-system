package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：微信扫码登录配置信息实体类
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "edu_wxcode")
@org.hibernate.annotations.Table(appliesTo = "edu_wxcode",comment="微信扫码登录配置信息表")
public class EduWxCode extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 微信开放平台APPID
     */
    @Column
    private String appId;

    /**
     * 微信开放平台appSecret
     */
    @Column
    private String appSecret;

    /**
     * 重定向地址
     */
    @Column
    private String redirectUri;

    /**
     * 前台地址
     */
    @Column
    private String frontUrl;
}
