package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：学生学号查询条件
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduCouponQueryCriteria {


    /**
     * 批次编号
     */
    @EnableXuedenQuery
    private String stockId;

    /**
     * 状态
     */
    @EnableXuedenQuery
    private Integer status;
}
