package cn.xueden.edu.wechat.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.Map;

/**发送的模板消息对象
 * @author:梁志杰
 * @date:2023/9/9
 * @description:cn.xueden.edu.wechat.dto
 * @version:1.0
 */
@Data
public class TemplateMessageDto {

    private String openid;//粉丝id
    private String templateId;//模板id
    private String url;//链接
    private String color = "#173177";//颜色
    private Map<String,String> dataMap;//参数数据
    @Override
    public String toString(){
        JSONObject jsObj = new JSONObject();
        jsObj.put("touser", openid);
        jsObj.put("template_id", templateId);
        jsObj.put("url", url);

        JSONObject data = new JSONObject();
        if(dataMap != null){
            for(String key : dataMap.keySet()){
                JSONObject item = new JSONObject();
                item.put("value", dataMap.get(key));
                data.put(key,item);
            }
        }
        jsObj.put("data", data);
        return jsObj.toString();
    }
}
