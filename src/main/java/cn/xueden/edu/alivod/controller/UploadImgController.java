package cn.xueden.edu.alivod.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.alivod.AliOssUploadImageLocalFileService;
import cn.xueden.edu.service.IEduCourseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**功能描述：上传图片到阿里视频点播平台自带的OSS
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.alivod.controller
 * @version:1.0
 */
@RestController
@RequestMapping("aliVod/upload")
public class UploadImgController {

    private final IEduCourseService eduCourseService;

    private final AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService;

    public UploadImgController(IEduCourseService eduCourseService, AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService) {
        this.eduCourseService = eduCourseService;
        this.aliVodeUploadImageLocalFileService = aliVodeUploadImageLocalFileService;
    }

    @EnableSysLog("【后台】上传课程封面")
    @PostMapping("uploadCover")
    public BaseResult uploadCover(@RequestParam("fileResource") MultipartFile fileResource,
                                  @RequestParam(value = "courseId",required = false) Long courseId){
        System.out.println("获取课程ID："+courseId);
        Map<String,Object> map = aliVodeUploadImageLocalFileService.uploadImageLocalFile(fileResource,"course");
        eduCourseService.uploadCover(courseId,map.get("urlPath").toString());
        return BaseResult.success(map);
    }

    @EnableSysLog("【后台】编辑器上传图片")
    @PostMapping("uploadEditor")
    public BaseResult uploadEditor(@RequestParam("fileResource") MultipartFile fileResource){
        Map<String,Object> map = aliVodeUploadImageLocalFileService.uploadImageLocalFile(fileResource,"course");
        return BaseResult.success(map);
    }
}
