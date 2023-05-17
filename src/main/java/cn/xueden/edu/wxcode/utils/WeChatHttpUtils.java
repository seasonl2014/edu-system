package cn.xueden.edu.wxcode.utils;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * @author:梁志杰
 * @date:2023/5/17
 * @description:cn.xueden.edu.wxcode.utils
 * @version:1.0
 */
public class WeChatHttpUtils {

    public static CloseableHttpClient getClient(){
        HttpClientBuilder builder = HttpClientBuilder.create();
        return builder.build();
    }
}
