package cn.xueden.edu.alivod.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseEntity;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.alivod.service.IVodService;
import cn.xueden.edu.domain.EduCourseVideo;
import cn.xueden.edu.service.IEduCourseVideoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**功能描述：上传视频前端控制器
 * @author:梁志杰
 * @date:2023/5/30
 * @description:cn.xueden.edu.alivod.controller
 * @version:1.0
 */
@RestController
@RequestMapping("aliVod/upload/video")
public class UploadVideoController {

    private final IEduCourseVideoService eduCourseVideoService;

    private final IVodService vodService;

    public UploadVideoController(IEduCourseVideoService eduCourseVideoService, IVodService vodService) {
        this.eduCourseVideoService = eduCourseVideoService;
        this.vodService = vodService;
    }

    @EnableSysLog("【后台】批量上传视频")
    @PostMapping("batch")
    public BaseResult batch(@RequestParam("file") MultipartFile file, @RequestParam("id")Long id,
                            @RequestParam("fileKey")String fileKey, HttpServletRequest request){
        System.out.println("文件："+file);
        System.out.println("id："+id);
        System.out.println("fileKey："+fileKey);
        Map<String,Object> map = new HashMap<>();
        // 判断视频是否已经上传过了
       EduCourseVideo dbEduCourseVideo = eduCourseVideoService.findByFileKey(fileKey);
       if(dbEduCourseVideo!=null){
           map.put("videoSourceId",dbEduCourseVideo.getVideoSourceId());
           return BaseResult.success("极速秒传完成");
       }else {
           String videoSourceId = vodService.batchUploadAliyunVideoById(file,id,request,fileKey);
           map.put("videoSourceId",videoSourceId);
       }
        map.put("fileKey",fileKey);
        return BaseResult.success();

    }

    /**
     *
     * @param file 上传文件
     * @param id 课程小节的ID
     * @param fileKey 文件唯一标志
     * @param request
     * @return
     */
    @EnableSysLog("【后台重传】上传单条视频")
    @PostMapping("single")
    public BaseResult single(@RequestParam("file") MultipartFile file, @RequestParam("id")Long id,
                            @RequestParam("fileKey")String fileKey, HttpServletRequest request){
        System.out.println("文件："+file);
        System.out.println("id："+id);
        System.out.println("fileKey："+fileKey);
        Map<String,Object> map = new HashMap<>();
        // 判断视频是否已经上传过了
        EduCourseVideo dbEduCourseVideo = eduCourseVideoService.findByFileKey(fileKey);
        if(dbEduCourseVideo!=null){
            // 如果不相等，就说明不是同一条记录，需要更新视频源ID和时长
            if(!id.equals(dbEduCourseVideo.getId())){
                eduCourseVideoService.updateByCourseVideo(id,dbEduCourseVideo);
            }
            map.put("videoSourceId",dbEduCourseVideo.getVideoSourceId());
            return BaseResult.success("极速秒传完成");
        }else {
            String videoSourceId = vodService.singleUploadAliyunVideoById(file,id,request,fileKey);
            map.put("videoSourceId",videoSourceId);
        }
        map.put("fileKey",fileKey);
        return BaseResult.success();

    }


}
