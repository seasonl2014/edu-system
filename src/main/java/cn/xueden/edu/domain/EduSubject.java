package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：课程分类实体类
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_subject")
@org.hibernate.annotations.Table(appliesTo = "edu_subject",comment = "课程分类信息表")
public class EduSubject extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 分类名称
     */
    @Column(name = "name",nullable = false)
    private String name;

    /**
     * 对应阿里云视频点播分类
     */
    @Column(name = "cate_id",nullable = false)
    private Long cateId;

    /**
     * 排序
     */
    @Column(name = "sort",nullable = false)
    private Integer sort;

    /**
     * 父级分类
     */
    @Column(name = "parent_id",nullable = false)
    private Long parentId;


}
