package cn.xueden.edu.wechat.dto;

import lombok.Data;

/**支付者信息
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class Payer {
    /**
     * 用户标识
     * 用户在直连商户appid下的唯一标识。
     */
    private String openid;
}
