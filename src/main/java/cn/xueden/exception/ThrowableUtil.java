package cn.xueden.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**异常处理
 * @author:梁志杰
 * @date:2022/12/31
 * @description:cn.xueden.exception
 * @version:1.0
 */
public class ThrowableUtil {

    /**
     * 获取堆栈信息
     * @param throwable
     * @return
     */
    public static String getStackTrace(Throwable throwable){
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)){
            throwable.printStackTrace();;
            return sw.toString();
        }
    }
}
