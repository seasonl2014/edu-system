package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;

/**
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
public class EduTeacherQueryCriteria {

    /**
     * 根据教师姓名、邮箱、手机号、QQ模糊查询
     */
    @EnableXuedenQuery(blurry = "name,email,phone,qq")
    private String searchValue;

}
