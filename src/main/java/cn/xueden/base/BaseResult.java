package cn.xueden.base;

import lombok.Data;

import java.io.Serializable;

/**功能描述：统一返回结果集
 * @author:梁志杰
 * @date:2022/12/31
 * @description:cn.xueden.base
 * @version:1.0
 */
@Data
public class BaseResult implements Serializable {

    public static final int STATUS_SUCCESS = 200;

    public static final int STATUS_FALL = 500;

    /**
     * 返回状态码
     */
    private int status;

    /**
     * 返回提示消息
     */
    private String message;

    /**
     * 返回数据
     */
    private Object result;

    /**
     * 返回时间
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 创建返回对象
     * @param status
     * @param message
     * @param result
     * @return
     */
    private static BaseResult createResult(int status,String message,Object result){
        BaseResult baseResult = new BaseResult();
        baseResult.setStatus(status);
        baseResult.setMessage(message);
        baseResult.setResult(result);
        return baseResult;
    }

    /**
     * 默认返回成功方法
     * @return
     */
    public static BaseResult success(){
        return  BaseResult.createResult(STATUS_SUCCESS,"成功",null);
    }

    /**
     * 返回成功带消息
     * @param message
     * @return
     */
    public static BaseResult success(String message){
        return BaseResult.createResult(STATUS_SUCCESS,message,null);
    }

    /**
     * 返回成功带数据
     * @param result
     * @return
     */
    public static BaseResult success(Object result){
        return BaseResult.createResult(STATUS_SUCCESS,"成功",result);
    }

    /**
     * 返回成功带消息和数据
     * @param result
     * @return
     */
    public static BaseResult success(String message,Object result){
        return BaseResult.createResult(STATUS_SUCCESS,message,result);
    }

    /**
     * 默认返回失败
     * @return
     */
    public static BaseResult fail(){
        return BaseResult.createResult(STATUS_FALL,"失败",null);
    }

    /**
     * 默认返回失败带消息
     * @return
     */
    public static BaseResult fail(String message){
        return BaseResult.createResult(STATUS_FALL,message,null);
    }

    /**
     * 返回失败带消息和数据
     * @param message
     * @param result
     * @return
     */
    public static BaseResult fail(String message,Object result){
        return BaseResult.createResult(STATUS_FALL,message,result);
    }


}
