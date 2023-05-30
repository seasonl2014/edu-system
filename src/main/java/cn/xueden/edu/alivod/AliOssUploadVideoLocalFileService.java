package cn.xueden.edu.alivod;

import cn.xueden.edu.alivod.utils.ConstantPropertiesUtil;
import cn.xueden.edu.domain.EduCourseVideo;
import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import static cn.xueden.edu.alivod.utils.AliyunVODSDKUtils.initVodClient;

/**阿里云上传本地视频
 * @author:梁志杰
 * @date:2023/5/30
 * @description:cn.xueden.edu.alivod
 * @version:1.0
 */
@Component
public class AliOssUploadVideoLocalFileService {

    public EduCourseVideo uploadVideoLocalFile(MultipartFile file, Long cateId, HttpServletRequest servletRequest,Long id,String fileKey){
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        try {
            EduCourseVideo eduVideo = new EduCourseVideo();
            String fileName = file.getOriginalFilename();
            String title = fileName.substring(0,fileName.lastIndexOf("."));
            InputStream inputStream = file.getInputStream();
            int fileSize = inputStream.available();
            UploadStreamRequest request = new UploadStreamRequest(accessKeyId, accessKeySecret, title, fileName, inputStream);
            request.setCateId(cateId);
            request.setPrintProgress(true);
            /* 设置自定义上传进度回调 (必须继承 VoDProgressListener) */
            request.setProgressListener(new PutVodProgressListener(servletRequest,fileSize,id,fileKey));
            UploadVideoImpl uploader = new UploadVideoImpl();
            UploadStreamResponse response = uploader.uploadStream(request);
            //请求视频点播服务的请求ID
            System.out.print("RequestId=" + response.getRequestId() + "\n");
            if (response.isSuccess()) {
                System.out.print("videoSourceId=" + response.getVideoId() + "\n");
            } else {
                /* 如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因 */
                System.out.print("videoSourceId=" + response.getVideoId() + "\n");
                System.out.print("ErrorCode=" + response.getCode() + "\n");
                System.out.print("ErrorMessage=" + response.getMessage() + "\n");
            }
            //获取阿里云视频信息
            Float duration = getVideoInfo(response.getVideoId());
            eduVideo.setVideoSourceId(response.getVideoId());
            eduVideo.setDuration(duration);
            eduVideo.setSize((long)fileSize);
            eduVideo.setFileKey(fileKey);
            eduVideo.setTitle(title);
            return eduVideo;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取阿里云视频信息(暂时先获取视频时长)
     * @param videoId
     * @return
     */
    public Float getVideoInfo(String videoId){
        try {
            TimeUnit.SECONDS.sleep(5);//秒
            DefaultAcsClient client = initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);
            GetVideoInfoResponse response = new GetVideoInfoResponse();
            GetVideoInfoRequest request = new GetVideoInfoRequest();
            request.setVideoId(videoId);
            response =client.getAcsResponse(request);
            Float duration = response.getVideo().getDuration();
            return duration;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
