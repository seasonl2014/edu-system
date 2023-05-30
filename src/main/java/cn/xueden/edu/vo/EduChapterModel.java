package cn.xueden.edu.vo;

import cn.xueden.edu.domain.EduCourseVideo;
import lombok.Data;

import java.util.Comparator;
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

    private int lev;

    private List<EduCourseVideo> children;

    /*
     * 排序,根据order排序
     */
    public static Comparator<EduChapterModel> order(){
        Comparator<EduChapterModel> comparator = (o1, o2) -> {
            if(!o1.getSort().equals(o2.getSort())){
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }
}
