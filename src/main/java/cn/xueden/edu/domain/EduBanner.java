package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：首页幻灯片实体类
 * @author:梁志杰
 * @date:2023/5/13
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_banner")
@org.hibernate.annotations.Table(appliesTo = "edu_banner",comment = "首页幻灯片信息表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class EduBanner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 链接地址
     */
    private String url;

    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String img;

    /**
     * 等级排序
     */
    private Integer level;

    /**
     * 状态，0表示禁用，1表示启用
     */
    private Integer status;
}
