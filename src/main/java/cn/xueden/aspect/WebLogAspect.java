package cn.xueden.aspect;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.xueden.annotation.EnableSysLog;
import cn.xueden.exception.GlobalExceptionHandler;

import cn.xueden.system.domain.SysLog;

import cn.xueden.system.repository.SysLogRepository;
import cn.xueden.utils.IpInfo;
import cn.xueden.utils.XuedenUtil;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;

/**功能描述：自定义日志切面
 * @author:梁志杰
 * @date:2023/3/20
 * @description:cn.xueden.aspect
 * @version:1.0
 */
@Aspect
@Order(5)
@Component
public class WebLogAspect {
    @Autowired
    private  SysLogRepository logRepository;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Autowired
    private GlobalExceptionHandler exceptionHandle;


    private SysLog sysLog = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("@annotation(cn.xueden.annotation.EnableSysLog)")
    public void webLog(){}

    /**
     * 前置通知，在方法执行之前执行
     */
    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws IOException {
        startTime.set(System.currentTimeMillis());
        //接收到请求，记录请求的内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpSession session = (HttpSession) attributes.resolveReference(RequestAttributes.REFERENCE_SESSION);
        sysLog = new SysLog();

        sysLog.setClassMethod(joinPoint.getSignature().getDeclaringTypeName()+"."+joinPoint.getSignature().getName());
        sysLog.setHttpMethod(request.getMethod());
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for(int i = 0;i<args.length;i++){
            Object o = args[i];
            if(o instanceof ServletRequest ||(o instanceof ServletResponse)|| o instanceof MultipartFile){
                args[i] = o.toString();
            }
        }
        String str = JSONUtil.toJsonStr(args);

        sysLog.setParams(str.length()>5000? JSONUtil.toJsonStr("请求参数数据过长不与显示"):str);

        String ip = XuedenUtil.getClientIp(request);
        if("0.0.0.0".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || "127.0.0.1".equals(ip)){
            ip = "127.0.0.1";
        }

        sysLog.setRemoteAddr(ip);
        sysLog.setRequestUri(request.getRequestURL().toString());
        if(session != null){
            sysLog.setSessionId(session.getId());
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EnableSysLog mylog = method.getAnnotation(EnableSysLog.class);
        if(mylog != null){
            //注解上的描述
            sysLog.setTitle(mylog.value());
        }

        Map<String,String> browserMap = XuedenUtil.getOsAndBrowserInfo(request);
        sysLog.setBrowser(browserMap.get("os")+"-"+browserMap.get("browser"));

        //根据Ip地址获取归属地（测试这个功能，必须要放到外网，要调用第三方归属地的接口）
        //ip = "180.137.111.52";
        if(!"127.0.0.1".equals(ip)){
            IpInfo ipInfo = XuedenUtil.getCityInfo(ip);
            if(null!=ipInfo){
                sysLog.setArea(ipInfo.getRegion());
                sysLog.setProvince(ipInfo.getProvince());
                sysLog.setCity(ipInfo.getCity());
                sysLog.setIsp(ipInfo.getIsp());
            }
        }

        sysLog.setType(XuedenUtil.isAjax(request)?"Ajax请求":"普通请求");

    }

    /**
     * 返回通知, 在方法返回结果之后执行
     * @param ret
     */
    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) {
        String retString = JSONUtil.toJsonStr(ret);
        sysLog.setResponse(retString.length()>5000? JSONUtil.toJsonStr("请求参数数据过长不与显示"):retString);
        sysLog.setUseTime(System.currentTimeMillis() - startTime.get());
        logRepository.save(sysLog);
    }

    /**
     * 环绕通知, 围绕着方法执行
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        try {
            Object obj = proceedingJoinPoint.proceed();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            sysLog.setException(e.getMessage());
            throw e;
        }
    }

}
