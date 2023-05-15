package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

/**功能描述：课程实体类
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_course")
@org.hibernate.annotations.Table(appliesTo = "edu_course",comment = "课程信息表")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
public class EduCourse extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 课程讲师ID
     */
    private Long teacherId;

    /**
     * 课程栏目ID
     */
    private Long subjectId;

    /**
     * 课程长标题
     */
    private String title;

    /**
     * 课程短标题
     */
    private String shortTitle;


    /**
     * 课程销售优惠价格，设置为0则可免费观看
     */
    private BigDecimal price;

    /**
     * 原来课程销售价格
     */
    private BigDecimal originalPrice;

    /**
     * 总课时
     */
    private Integer lessonNum;

    /**
     * 课程封面图片路径
     */
    private String cover;

    /**
     * 销售数量
     */
    private Long buyCount;

    /**
     * VIP学员数量
     */
    private Long vipCount;

    /**
     * 浏览数量
     */
    private Long viewCount;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 课程状态 Draft未发布  Normal已发布
     */
    private String status;

    /**
     * 课程一级分类ID
     */
    private Long subjectParentId;

    /**
     * 课程难度，0表示入门，1表示初级，2表示中级，3表示高级
     */
    private Integer difficulty;

    /**
     * 课程类型 ，0表示新手入门，1表示免费好课，2表示技能提高3，表示实战
     */
    private Integer courseType;

    /**
     * 课程描述
     */
    private String courseDesc;

}
