package cn.xueden.utils;




import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;

import java.io.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**工具类
 * @author:梁志杰
 * @date:2023/3/3
 * @description:cn.xueden.utils
 * @version:1.0
 */
public class XuedenUtil {
    private static final Pattern SPLIT_PATTERN = Pattern.compile("\\|");

    /**
     * 随机生成六位数
     * @return
     */
    public static int randomSixNums(){
        int code = (int) ((Math.random()*9+1)*100000);
        return code;
    }

    /**
     * 功能描述：根据两个日期算出天数字
     * @param one
     * @param two
     * @return
     */
    public static long daysBetween(Date one, Date two) {
        long difference =  (one.getTime()-two.getTime())/86400000;
        return Math.abs(difference);
    }

    /**
     * 功能描述：根据两个日期算出天数字
     * @param start
     * @param end
     * @return
     */
    public static long LocalDateBetween(LocalDate start, LocalDate end) {
        Period next = Period.between(start,end);
        return next.getDays();
    }

    /**
     * 生成订单号
     * @return
     */
    public static String createOrderNumber(){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String seconds = new SimpleDateFormat("HHmmss").format(new Date());
        String result = date+"00001000"+getTwo()+"00"+seconds+getTwo();
        return result;
    }

    /**
     * * 产生随机的2位数
     * * @return
     * */
    public static String getTwo(){
        Random rad=new Random();
        String result  = rad.nextInt(100) +"";
        if(result.length()==1){
            result = "0" + result;
        }
        return result;
    }

    /**
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 开始与结束之间的所以日期，包括起止
     */
    public static List<LocalDate> getMiddleDate(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateList = new ArrayList<>();
        long length = end.toEpochDay() - begin.toEpochDay();
        for (long i = length; i >= 0; i--) {
            localDateList.add(end.minusDays(i));
        }
        return localDateList;
    }
    /**
     * 获取客户端的ip信息
     *
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取操作系统,浏览器及浏览器版本信息
     * @param request
     * @return
     */
    public static Map<String,String> getOsAndBrowserInfo(HttpServletRequest request){
        Map<String,String> map = new HashMap();
        String  browserDetails  =   request.getHeader("User-Agent");
        String  userAgent       =   browserDetails;
        String  user            =   userAgent.toLowerCase();

        String os = "";
        String browser = "";

        //=================OS Info=======================
        if (userAgent.toLowerCase().contains("windows"))
        {
            os = "Windows";
        } else if(userAgent.toLowerCase().contains("mac"))
        {
            os = "Mac";
        } else if(userAgent.toLowerCase().contains("x11"))
        {
            os = "Unix";
        } else if(userAgent.toLowerCase().contains("android"))
        {
            os = "Android";
        } else if(userAgent.toLowerCase().contains("iphone"))
        {
            os = "IPhone";
        }else{
            os = "UnKnown, More-Info: "+userAgent;
        }
        //===============Browser===========================
        if (user.contains("edge"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Edge")).split(" ")[0]).replace("/", "-");
        } else if (user.contains("msie"))
        {
            String substring=userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
            browser=substring.split(" ")[0].replace("MSIE", "IE")+"-"+substring.split(" ")[1];
        } else if (user.contains("safari") && user.contains("version"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0]
                    + "-" +(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
        } else if ( user.contains("opr") || user.contains("opera"))
        {
            if(user.contains("opera")){
                browser=(userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0]
                        +"-"+(userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
            }else if(user.contains("opr")){
                browser=((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
                        .replace("OPR", "Opera");
            }

        } else if (user.contains("chrome"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
        } else if ((user.contains("mozilla/7.0")) || (user.contains("netscape6"))  ||
                (user.contains("mozilla/4.7")) || (user.contains("mozilla/4.78")) ||
                (user.contains("mozilla/4.08")) || (user.contains("mozilla/3")) )
        {
            browser = "Netscape-?";

        } else if (user.contains("firefox"))
        {
            browser=(userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
        } else if(user.contains("rv"))
        {
            String IEVersion = (userAgent.substring(userAgent.indexOf("rv")).split(" ")[0]).replace("rv:", "-");
            browser="IE" + IEVersion.substring(0,IEVersion.length() - 1);
        } else
        {
            browser = "UnKnown, More-Info: "+userAgent;
        }
        map.put("os",os);
        map.put("browser",browser);
        return map;
    }

    /**
     * 判断请求是否是ajax请求
     * @param request
     * @return
     */
    public static boolean isAjax(HttpServletRequest request){
        String accept = request.getHeader("accept");
        return accept != null && accept.contains("application/json") || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").contains("XMLHttpRequest"));
    }

    /**
     * 根据ip获取详细地址
     */
    public static IpInfo getCityInfo(String ip) throws IOException {
        // 服务器存放的路径
        String dbPath = "/usr/local/hotel/ip2region.xdb";
        // 本地电脑存放的路径
        //String dbPath = "G:\\hotel\\ip2region.xdb";
        Searcher searcher = null;
        IpInfo ipInfo = new IpInfo();
        try {
            searcher = Searcher.newWithFileOnly(dbPath);
        } catch (IOException e) {
            System.out.printf("failed to create searcher with `%s`: %s\n", dbPath, e);
        }

        // 2、查询
        try {
            long sTime = System.nanoTime();
            String region = searcher.search(ip);
            if (region == null) {
                return null;
            }

            String[] splitInfos = SPLIT_PATTERN.split(region);
            // 补齐5位
            if (splitInfos.length < 5) {
                splitInfos = Arrays.copyOf(splitInfos, 5);
            }
            ipInfo.setCountry(filterZero(splitInfos[0]));
            ipInfo.setRegion(filterZero(splitInfos[1]));
            ipInfo.setProvince(filterZero(splitInfos[2]));
            ipInfo.setCity(filterZero(splitInfos[3]));
            ipInfo.setIsp(filterZero(splitInfos[4]));

            long cost = TimeUnit.NANOSECONDS.toMicros((long) (System.nanoTime() - sTime));
            System.out.printf("{region: %s, ioCount: %d, took: %d μs}\n", region, searcher.getIOCount(), cost);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }

        // 3、关闭资源
        searcher.close();

        return ipInfo;
    }

    /**
     * 数据过滤，因为 ip2Region 采用 0 填充的没有数据的字段
     * @param info info
     * @return info
     */
    @Nullable
    private static String filterZero(@Nullable String info) {
        // null 或 0 返回 null
        if (info == null || BigDecimal.ZERO.toString().equals(info)) {
            return null;
        }
        return info;
    }

    public static void main(String[] args) throws IOException {
        //getMiddleDate(LocalDate.of(2023, 03, 10), LocalDate.now()).stream().forEach(System.out::println);
        getCityInfo("180.137.111.52");
    }


}
