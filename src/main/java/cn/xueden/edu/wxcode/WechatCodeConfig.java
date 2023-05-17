package cn.xueden.edu.wxcode;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**微信开放平台配置文件
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wxcode
 * @version:1.0
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "wxcode")
public class WechatCodeConfig {

    private String appId;

    private String appSecret;

    private String redirectUri;

    private String frontUrl;
}
