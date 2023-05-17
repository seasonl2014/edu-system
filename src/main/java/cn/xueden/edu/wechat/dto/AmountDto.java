package cn.xueden.edu.wechat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**功能描述：微信支付金额对象
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmountDto {
    /**
     * 订单总金额，单位为分
     */
    private Integer total;

    /**
     * 货币类型,CNY：人民币
     */
    private String currency;
}
