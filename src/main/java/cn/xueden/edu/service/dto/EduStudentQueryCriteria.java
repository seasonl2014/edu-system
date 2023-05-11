package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：学生信息查询条件
 * @author:梁志杰
 * @date:2023/2/18
 * @description:cn.xueden.student.service.dto
 * @version:1.0
 */
@Data
public class EduStudentQueryCriteria {

    /**
     * 根据学生姓名、学号、手机号、QQ模糊查询
     */
    @EnableXuedenQuery(blurry = "name,stuNo,phone,qq")
    private String searchValue;
}
