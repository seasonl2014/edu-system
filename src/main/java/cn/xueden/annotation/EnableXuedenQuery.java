package cn.xueden.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**功能描述：自定义注解 用于查询
 * @author:梁志杰
 * @date:2022/12/31
 * @description:cn.xueden.annotation
 * @version:1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableXuedenQuery {

    /**
     * 基本对象的属性名
     * @return
     */
    String propName() default "";

    /**
     * 查询方式
     * @return
     */
    Type type() default Type.EQUAL;

    /**
     * 连接查询的属性名，如User类中dept
      */

    String joinName() default "";

    /**
     * 默认左连接
     */
    Join join() default Join.LEFT;

    /**
     * 多字段模糊搜索，仅支持String类型字段，多个用逗号隔开
     */
    String blurry() default "";

    enum Type {
        // 相等
        EQUAL
        // 大于等于
        ,GREATER_THAN
        // 小于等于
        ,LESS_THAN
        // 中模糊查询
        ,INNER_LIKE
        // 左模糊查询
        ,LEFT_LIKE
        // 右模糊查询
        ,RIGHT_LIKE
        // 小于
        ,LESS_THAN_NQ
        // 包含
        ,IN
        // 不等于
        ,NOT_EQUAL
        // 之间
        ,BETWEEN
        // 不为空
        ,NOT_NULL

    }

    enum Join{
        // 左右连接
        LEFT,RIGHT
    }
}
