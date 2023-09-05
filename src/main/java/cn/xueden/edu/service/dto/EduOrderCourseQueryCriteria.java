package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：课程订单搜索参数
 * @author:梁志杰
 * @date:2023/9/4
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduOrderCourseQueryCriteria {

    /**
     * 关键字搜索
     */
    @EnableXuedenQuery(blurry = "orderNo")
    private String orderNo;

    /**
     * 订单状态
     */
    @EnableXuedenQuery
    private Integer isPayment;
}
