package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：课程视频实体类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_course_video")
@org.hibernate.annotations.Table(appliesTo = "edu_course_video",comment = "课程视频信息表")
public class EduCourseVideo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 课程ID
     */
    private Long courseId;

    /**
     * 章节ID
     */
    private Long chapterId;

    /**
     * 节点名称
     */
    private String title;

    /**
     * 排序字段
     */
    private Integer sort;

    /**
     * 播放次数
     */
    private Long playCount;

    /**
     * 是否可以试看：0免费 1收费
     */
    private Integer isFree;

    /**
     * 视频资源
     */
    private String videoSourceId;

    /**
     * 视频时长（秒）
     */
    private Float duration;

    /**
     * 预览时长（秒）
     */
    private Integer previewDuration;

    /**
     * 视频状态:见阿里云文档
     */
    private String status;

    /**
     * 视频源文件大小（字节）
     */
    private Long size;

    /**
     * 乐观锁
     */
    private Long version;

    /**
     * 云服务器上存储的视频文件名称
     */
    private String videoOriginalName;

    /**
     * 文件唯一标志
     */
    private String fileKey;
}
