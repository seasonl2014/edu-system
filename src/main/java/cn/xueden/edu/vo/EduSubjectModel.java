package cn.xueden.edu.vo;

import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.domain.EduSubject;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**功能描述：课程分类视图对象
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class EduSubjectModel {

    private Long id;

    private String name;

    /** 对应阿里云点播类目ID*/
    private Long cateId;

    private Integer sort;

    /**备注*/
    private String remarks;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date updateTime;

    /** 父级分类id*/
    private Long parentId;

     /**   子栏目*/
    private List<EduSubject> childrens;

    /**对应课程*/
    private List<EduCourse> eduCourseList;
}
