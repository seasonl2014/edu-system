package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：短信设置实体类
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "edu_sms")
@org.hibernate.annotations.Table(appliesTo = "edu_sms",comment="短信配置信息表")
public class EduSms extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 区域ID
     */
    @Column
    private String regionId;

    /**
     *短信签名
     */
    @Column
    private String signName;

    /**
     * 短信模板
     */
    @Column
    private String templateCode;
}
