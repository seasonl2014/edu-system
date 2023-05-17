package cn.xueden.edu.wechat.dto;

import lombok.Data;

/**
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class WxCiphertextDto {
    // 商户订单号
    private String out_trade_no;

    // 交易状态 SUCCESS：支付成功 REFUND：转入退款 NOTPAY：未支付
    // CLOSED：已关闭 REVOKED：已撤销（付款码支付）
    // USERPAYING：用户支付中（付款码支付）
    //PAYERROR：支付失败(其他原因，如银行返回失败)
    private String trade_state;
}
