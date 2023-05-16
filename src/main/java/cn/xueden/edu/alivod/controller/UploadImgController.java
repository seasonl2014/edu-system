package cn.xueden.edu.alivod.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.alivod.AliOssUploadImageLocalFileService;
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

    private final AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService;

    public UploadImgController(AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService) {
        this.aliVodeUploadImageLocalFileService = aliVodeUploadImageLocalFileService;
    }

    @EnableSysLog("【后台】上传课程封面")
    @PostMapping("uploadCover")
    public BaseResult uploadCover(@RequestParam("fileResource") MultipartFile fileResource,
                                  @RequestParam(value = "host",required = false) String host,
                                  @RequestParam(value = "id",required = false) String id){
        System.out.println("获取课程ID："+id);
        Map<String,Object> map = aliVodeUploadImageLocalFileService.uploadImageLocalFile(fileResource,host);
        return BaseResult.success(map);
    }
}
