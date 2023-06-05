package cn.xueden.edu.wechat.utils;

import lombok.Data;

/**功能描述：向微信返回结果
 * @author:梁志杰
 * @date:2023/6/5
 * @description:cn.xueden.edu.wechat.utils
 * @version:1.0
 */
@Data
public class NotifyResult {

    private String code;
    private String message;

    public NotifyResult() {
    }
    public static NotifyResult create(){
        return new NotifyResult();
    }
    public NotifyResult success(){
        this.code = "SUCCESS";
        this.message = "成功";
        return this;
    }
    public NotifyResult fail(){
        this.code = "FAIL";
        this.message = "失败";
        return this;
    }
    public NotifyResult fail(String message){
        this.code = "FAIL";
        this.message = message;
        return this;
    }


}
