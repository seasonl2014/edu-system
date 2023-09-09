package cn.xueden.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**时间工具类
 * @author:梁志杰
 * @date:2023/9/9
 * @description:cn.xueden.utils
 * @version:1.0
 */
public class DateUtil {

    // 将date类型转换为字符串yyyy-MM-dd HH:mm:ss
    public static String changeDateTOStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return getDateToStr(date, sdf);
    }

    private static String getDateToStr(Date date, SimpleDateFormat sdf) {
        String dateStr = null;
        try {
            dateStr = sdf.format(date);
            return dateStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
