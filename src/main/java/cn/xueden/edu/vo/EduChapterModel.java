package cn.xueden.edu.vo;

import cn.xueden.edu.domain.EduCourseVideo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**功能描述：章节视图模型
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class EduChapterModel {

    private Long id;

    private Long courseId;

    private String title;

    private String remarks;

    private Float duration;

    private Integer sort;

    private Date createDate;

    private Date updateDate;

    private List<EduCourseVideo> children;
}
