package cn.xueden.edu.service.dto;

import cn.xueden.annotation.EnableXuedenQuery;
import lombok.Data;

/**功能描述：成交金额搜索参数
 * @author:梁志杰
 * @date:2023/9/5
 * @description:cn.xueden.edu.service.dto
 * @version:1.0
 */
@Data
public class EduDealMoneyQueryCriteria {

    /**
     * 关键字搜索
     */
    @EnableXuedenQuery(blurry = "orderNo")
    private String orderNo;

    /**
     * 购买类型，0表示普通会员购买，其他表示VIP购买
     */
    @EnableXuedenQuery
    private Integer buyType;
}
