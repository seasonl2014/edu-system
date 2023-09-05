package cn.xueden.edu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**功能描述：VIP订单视图模型
 * @author:梁志杰
 * @date:2023/9/5
 * @description:cn.xueden.edu.vo
 * @version:1.0
 */
@Data
public class StudentBuyVipModel {

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 学员编号
     */
    private String studentNo;

    /**
     * 会员名称
     */
    private String vipName;

    /**
     * 会员价格
     */
    private Double vipPrice;

    /**
     * 购买金额
     */
    private BigDecimal price;

    /**
     * 下单时间
     */
    private Timestamp createTime;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;
}
