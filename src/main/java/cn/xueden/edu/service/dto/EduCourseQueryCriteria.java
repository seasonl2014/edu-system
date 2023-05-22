package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：课程查询条件类
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduCourseQueryCriteria {

    /**
     * 二级分类栏目
     */
    @EnableXuedenQuery
    private Long subjectId;
    /**
     * 一级分类栏目
     */
    @EnableXuedenQuery
    private Long subjectParentId;

    /**
     * 课程难易程度
     */
    @EnableXuedenQuery
    private Integer difficulty;
    /**
     * 课程类型 ，0表示新手入门，1表示新上好课，2表示技能提高3，表示实战
     */
    @EnableXuedenQuery
    private Integer courseType;


    private String courseSort;

    /**
     * 关键字搜索
     */
    @EnableXuedenQuery(blurry = "courseDesc,shortTitle,title")
    private String searchValue;


}
