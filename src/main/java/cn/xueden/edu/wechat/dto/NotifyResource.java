package cn.xueden.edu.wechat.dto;


import lombok.Data;

/**微信支付处理结果 解析结果类
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class NotifyResource {

    /**
     * 商户号
     */
    private String mchid;
    /**
     * 应用ID
     */
    private String appid;
    /**
     * 原始订单号
     */
    private String out_trade_no;
    /**
     * 微信支付订单号
     * 微信支付系统生成的订单号。
     */
    private String transaction_id;
    /**
     * 交易类型
     */
    private String trade_type;
    /**
     * 交易状态
     */
    private String trade_state;
    /**
     * 交易状态描述
     */
    private String trade_state_desc;
    /**
     * 付款银行
     */
    private String bank_type;
    /**
     * 附加数据
     */
    private String attach;
    /**
     * 支付完成时间
     */
    private String success_time;
    /**
     * 支付者
     */
    private Payer payer;
    /**
     * 订单金额
     */
    private Amount amount;

}
