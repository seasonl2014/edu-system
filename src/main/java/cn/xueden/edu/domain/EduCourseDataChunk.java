package cn.xueden.edu.domain;

import jakarta.persistence.*;
import lombok.*;

/**功能描述：文件分片临时表
 * @author:梁志杰
 * @date:2023/6/23
 * @description:cn.xueden.edu.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "edu_course_data_chunk")
@org.hibernate.annotations.Table(appliesTo = "edu_course_data_chunk",comment = "课程资料分片临时表")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class EduCourseDataChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "f_md5",nullable = false)
    @NonNull
    private String md5;

    @Column(name = "f_index",nullable = false)
    @NonNull
    private Integer index;

}
