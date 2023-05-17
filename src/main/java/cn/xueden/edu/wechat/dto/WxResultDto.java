package cn.xueden.edu.wechat.dto;

import lombok.Data;

/**功能描述：微信返回通知结果集
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class WxResultDto {

    // 通知ID 通知的唯一ID
    private String id;

    // 通知创建时间
    private String create_time;

    // 通知类型 ，支付成功通知的类型为TRANSACTION.SUCCESS
    private String event_type;

    // 通知数据类型 通知的资源数据类型，支付成功通知为encrypt-resource
    private String resource_type;

    // 回调摘要
    private String summary;

    private WxResourceDto resource;

}
