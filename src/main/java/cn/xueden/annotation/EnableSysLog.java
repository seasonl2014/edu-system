package cn.xueden.annotation;

import java.lang.annotation.*;

/**功能描述:系统日志注解
 * @author:梁志杰
 * @date:2023/3/20
 * @description:cn.xueden.annotation
 * @version:1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableSysLog {
    String value() default "";
}
