package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：退款记录搜索参数
 * @author:梁志杰
 * @date:2023/9/5
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduRefundQueryCriteria {

    /**
     * 关键字搜索
     */
    @EnableXuedenQuery(blurry = "refundId,outRefundNo,transactionId,outTradeNo")
    private String searchValue;

    /**
     * 退款类型，0表示课程，其他表示VIP退款
     */
    @EnableXuedenQuery
    private Integer refundType;
}
