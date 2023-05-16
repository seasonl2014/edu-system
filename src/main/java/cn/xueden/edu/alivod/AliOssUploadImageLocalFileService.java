package cn.xueden.edu.alivod;

import cn.hutool.core.date.DateTime;
import cn.xueden.edu.alivod.utils.ConstantPropertiesUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**功能描述：阿里云视频点播本地上传图片
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.alivod
 * @version:1.0
 */
@Component
public class AliOssUploadImageLocalFileService {

    public Map<String, Object> uploadImageLocalFile(MultipartFile fileResource, String host) {
        // Endpoint (地域 如：上海)
        String endpoint = ConstantPropertiesUtil.END_POINT;
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String yourBucketName = ConstantPropertiesUtil.BUCKET_NAME;
        String hostpath = ConstantPropertiesUtil.HOST_PATH;
        try {
            //1、获取到上传文件MultipartFile file 名称
            String filename = fileResource.getOriginalFilename();
            //在文件名之前加上uuid，保证文件名称不重复（防止覆盖问题）
            String uuid = UUID.randomUUID().toString();
            filename = uuid+filename;
            //构建日期路径：2020/02/03
            String filePath = new DateTime().toString("yyyy/MM/dd");
            String hostName = ConstantPropertiesUtil.HOST;
            //如果上传的是头像，则host里为空，如果上传封面host则有值
            if (StringUtils.isNotEmpty(host)) {
                hostName = host;
            }
            filename = filePath+"/"+hostName+"/"+filename;

            InputStream inputStream = fileResource.getInputStream();
            //3、把上传文件存储到阿里云oss里面
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            PutObjectResult result = ossClient.putObject(yourBucketName, filename, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();
            String path = hostpath+"/"+filename;
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("urlPath",path);
            map.put("result",result);
            return map;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
