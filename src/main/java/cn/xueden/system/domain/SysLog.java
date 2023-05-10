package cn.xueden.system.domain;

import cn.xueden.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

/**功能描述：系统日志实体类
 * @author:梁志杰
 * @date:2023/3/20
 * @description:cn.xueden.system.domain
 * @version:1.0
 */
@Entity
@Data
@Table(name = "sys_log")
@org.hibernate.annotations.Table(appliesTo = "sys_log",comment = "系统日志信息表")
public class SysLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    /**
     * 请求类型
     */
    @Column(name = "type")
    private String type;

    /**
     * 日志标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 操作IP地址
     */
    @Column(name = "remote_addr")
    private String remoteAddr;

    /**
     * 操作用户昵称
     */
    @Transient
    private String username;

    /**
     * 请求URI
     */
    @Column(name = "request_uri")
    private String requestUri;

    /**
     * 操作方式
     */
    @Column(name = "http_method")
    private String httpMethod;

    /**
     * 请求类型.方法
     */
    @Column(name = "class_method")
    private String classMethod;

    /**
     * 操作提交的数据
     */
    @Column(name = "params")
    private String params;

    /**
     * sessionId
     */
    @Column(name = "session_id")
    private String sessionId;

    /**
     * 返回内容
     */
    @Column(name = "response")
    private String response;

    /**
     * 方法执行时间
     */
    @Column(name = "use_time")
    private Long useTime;

    /**
     * 浏览器信息
     */
    @Column(name = "browser")
    private String browser;

    /**
     * 地区
     */
    @Column(name = "area")
    private String area;

    /**
     * 省
     */
    @Column(name = "province")
    private String province;

    /**
     * 市
     */
    @Column(name = "city")
    private String city;

    /**
     * 网络服务提供商
     */
    @Column(name = "isp")
    private String isp;

    /**
     * 异常信息
     */
    @Column(name = "exception")
    private String exception;
}
