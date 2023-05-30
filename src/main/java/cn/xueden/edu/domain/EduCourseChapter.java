package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：课程大章实体类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_course_chapter")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer"})
@org.hibernate.annotations.Table(appliesTo = "edu_course_chapter",comment = "课程大章信息表")
public class EduCourseChapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 章节名称
     */
    private String title;

    /**
     * 章节时长
     */
    private Float duration;

    /**
     * 显示排序
     */
    private Integer sort;

}
