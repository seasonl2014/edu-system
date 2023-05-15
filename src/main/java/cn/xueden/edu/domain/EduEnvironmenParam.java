package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：开发环境实体类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_environmen_param")
@org.hibernate.annotations.Table(appliesTo = "edu_environmen_param",comment = "项目开发环境参数信息表")
public class EduEnvironmenParam extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数值
     */
    private String value;

}
