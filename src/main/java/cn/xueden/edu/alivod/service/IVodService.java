package cn.xueden.edu.alivod.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

/**功能描述：阿里云视频点播业务接口
 * @author:梁志杰
 * @date:2023/5/30
 * @description:cn.xueden.edu.alivod.service
 * @version:1.0
 */
public interface IVodService {
    /**
     * 根据章节id实现上传视频到阿里云服务器的方法(批量上传)
     * @param file
     * @param id 课程大章的ID
     * @param request
     * @param fileKey
     * @return
     */
    String batchUploadAliyunVideoById(MultipartFile file, Long id, HttpServletRequest request, String fileKey);

    /**
     * 上传单条视频到阿里云服务器的方法
     * @param file 待上传的文件
     * @param id 课程小节ID
     * @param request
     * @param fileKey
     * @return
     */
    String singleUploadAliyunVideoById(MultipartFile file, Long id, HttpServletRequest request, String fileKey);
}
