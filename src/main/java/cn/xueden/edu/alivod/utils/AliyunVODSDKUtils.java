package cn.xueden.edu.alivod.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

/**阿里云视频点播sdk初始化操作
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.alivod.utils
 * @version:1.0
 */
public class AliyunVODSDKUtils {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域上海
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);

        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
