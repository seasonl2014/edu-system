package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：VIP类型实体类
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_vip_type")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "edu_vip_type",comment = "vip类型信息表")
public class EduVipType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * vip名称
     */
    @Column(name = "vip_name",nullable = false)
    private String vipName;

    /**
     * VIP售价
     */
    @Column(name = "vip_money",nullable = false)
    private Double vipMoney;

    /**
     * 加入人数
     */
    @Column(name = "nums",nullable = false)
    private Integer nums;
}
