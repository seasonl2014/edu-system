package cn.xueden.edu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**功能描述：章节树形结构视图类
 * @author:梁志杰
 * @date:2023/5/29
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class EduChapterTreeNodeModel {
    private Long id;

    private String title;

    private Long courseId;

    private String videoSourceId;

    private Integer sort;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    private int lev;

    private Float duration;

    private List<EduChapterTreeNodeModel> children;

    /*
     * 排序,根据order排序
     */
    public static Comparator<EduChapterTreeNodeModel> order(){
        Comparator<EduChapterTreeNodeModel> comparator = (o1, o2) -> {
            if(!o1.getSort().equals(o2.getSort())){
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }
}
