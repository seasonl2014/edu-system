package cn.xueden.edu.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：阿里云对象存储实体类
 * @author:梁志杰
 * @date:2023/9/3
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Data
@Entity
@Table(name = "edu_ali_oss")
@org.hibernate.annotations.Table(appliesTo = "edu_ali_oss",comment="阿里云OSS配置信息表")
public class EduAliOss extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    /**
     * 阿里云accessKeyID
     */
    @Column
    private String accessKeyID;

    /**
     * 阿里云accessKeySecret
     */
    @Column
    private String accessKeySecret;

    /**
     * 文件存放区域
     */
    @Column
    private String endpoint;

    /**
     * 存放图片文件夹
     */
    @Column
    private String fileHost;

    /**
     * 存放课程资料文件夹
     */
    @Column
    private String fileHostCourse;

    /**
     * OSS Bucket存放图片名称
     */
    @Column
    private String bucketName;

    /**
     * OSS Bucket存放课程资料名称
     */
    @Column
    private String bucketCourseName;

    /**
     * OSS绑定域名
     */
    @Column
    private String hostPath;
}
