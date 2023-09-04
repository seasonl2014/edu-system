package cn.xueden.edu.wechat.dto;

import lombok.Data;

/**功能描述：微信支付下单请求参数
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class WxOrderDto {

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商户订单号
     */
    private String out_trade_no;

    /**
     * 通知地址
     */
    private String notify_url;

    /**
     * 金额
     */
    private AmountDto amount;
}
