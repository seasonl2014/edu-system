package cn.xueden.edu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**功能描述：课程订单视图模型
 * @author:梁志杰
 * @date:2023/9/5
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class StudentBuyCourseModel {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 下单时间
     */
    private Timestamp createTime;

    /**
     * 购买金额
     */
    private BigDecimal price;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 原来课程销售价格
     */
    private BigDecimal originalPrice;

    /**
     * 学员编号
     */
    private String studentNo;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;
}
