package cn.xueden.edu.wechat.dto;

import lombok.Data;

/**订单金额信息
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class Amount {
    /**
     * 总金额
     * 订单总金额，单位为分。
     */
    private int total;
    /**
     * 用户支付金额
     * 用户支付金额，单位为分。
     */
    private int payer_total;
    /**
     * 货币类型
     * CNY：人民币，境内商户号仅支持人民币。
     */
    private String currency;
    /**
     * 用户支付币种
     * 用户支付币种
     */
    private String payer_currency;
}
