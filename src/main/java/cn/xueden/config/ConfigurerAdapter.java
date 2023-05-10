package cn.xueden.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**功能描述：配置类
 * @author:梁志杰
 * @date:2022/12/31
 * @description:cn.xueden.config
 * @version:1.0
 */
@Configuration
public class ConfigurerAdapter implements WebMvcConfigurer {

    @Value("${user.icon}")
    private String userIcon;

    /**
     * 配置跨域
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对哪些请求路径进行跨域处理
       registry.addMapping("/**")
               // 允许的请求头，默认允许所有的请求头
               .allowedHeaders("*")
               // 允许的方法（get、post...）
               .allowedMethods("*")
               // 探测请求有效时间，单位是秒
               .maxAge(1800)
               // 是否允许证书（cookies）
               .allowCredentials(true)
               // 支持的域
               .allowedOriginPatterns("*");
    }

    /**
     * 静态资源文件路径映射
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       String pathUrl = "file:"+userIcon.replace("\\","/");
       registry.addResourceHandler("/uploadFile/**").addResourceLocations(pathUrl).setCachePeriod(0);
    }
}
