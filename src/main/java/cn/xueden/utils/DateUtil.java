package cn.xueden.utils;

import java.text.ParseException;
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

    /**
     * 2024-02-20T00:00:00+08:00 转 2024-02-20 08:34:38
     * 将带T字符串日期转换为字符串yyyy-MM-dd HH:mm:ss
     * @param dateTime
     * @return
     */
    public static String changeTStringTOStr(String dateTime) throws ParseException {

        // String dateTime = "2024-02-20T00:00:00+08:00";

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        Date parsedDateTime = inputFormat.parse(dateTime);

        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = outputFormat.format(parsedDateTime);

        return formattedDateTime;
    }
}
