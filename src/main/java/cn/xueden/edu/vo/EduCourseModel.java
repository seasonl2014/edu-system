package cn.xueden.edu.vo;

import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduTeacher;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**功能描述：课程视图模型
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class EduCourseModel extends EduCourse {

    /**
     * 教师信息
     */
     private EduTeacher eduTeacher;

    /**
     * 讲师对应的课程
     */
    private List<EduCourse> teacherCourses;

    /**
     * 观看视频权限
     */
    private boolean viewVideo;

}
