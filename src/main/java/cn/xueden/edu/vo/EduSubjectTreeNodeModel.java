package cn.xueden.edu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**功能描述：课程分类树形视图对象
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class EduSubjectTreeNodeModel {

    private Long id;

    private String name;

    private Long cateId;

    private Integer sort;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Long parentId;

    private int lev;

    private List<EduSubjectTreeNodeModel> children;


    /**
     * 排序,根据order排序
     * @return
     */
    public static Comparator<EduSubjectTreeNodeModel> order(){
        Comparator<EduSubjectTreeNodeModel> comparator = (o1, o2) -> {
            if(!o1.getSort().equals(o2.getSort())){
                return (int) (o1.getSort() - o2.getSort());
            }
            return 0;
        };
        return comparator;
    }
}
