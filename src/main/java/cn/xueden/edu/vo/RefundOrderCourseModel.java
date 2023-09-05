package cn.xueden.edu.vo;

import lombok.Data;

/**功能描述：课程订单退款参数
 * @author:梁志杰
 * @date:2023/9/5
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class RefundOrderCourseModel {
    /**
     * 订单ID
      */
    private Long orderId;

    /**
     * 退款原因
      */
    private String remarks;

    /**
     * 操作方式,0表示退款，1表示退款并封禁学员账号
     */
    private Integer type;
}
