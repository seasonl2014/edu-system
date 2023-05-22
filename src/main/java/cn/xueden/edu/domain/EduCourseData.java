package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：课程资料实体类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_course_data")
@org.hibernate.annotations.Table(appliesTo = "edu_course_data",comment = "课程资料信息表")
public class EduCourseData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 资料名称
     */
    private String name;

    /**
     * 资料下载地址
     */
    private String downloadAddress;

    /**
     * 文件标志
     */
    private String fileKey;

}
