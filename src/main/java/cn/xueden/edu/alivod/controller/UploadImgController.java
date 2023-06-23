package cn.xueden.edu.alivod.controller;

import cn.hutool.core.date.DateTime;
import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.alivod.AliOssMultipartUploadFileService;
import cn.xueden.edu.alivod.AliOssUploadImageLocalFileService;
import cn.xueden.edu.alivod.service.IChunkService;
import cn.xueden.edu.alivod.utils.ConstantPropertiesUtil;
import cn.xueden.edu.domain.EduCourseData;
import cn.xueden.edu.domain.EduStudentBuyCourse;
import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.service.*;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.JWTUtil;
import com.aliyuncs.exceptions.ClientException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.io.IOUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**功能描述：上传图片到阿里视频点播平台自带的OSS
 * @author:梁志杰
 * @date:2023/5/16
 * @description:cn.xueden.edu.alivod.controller
 * @version:1.0
 */
@RestController
@RequestMapping("aliVod/upload")
public class UploadImgController {
    Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${file.path}")
    private String filePath;

    private final IEduCourseService eduCourseService;

    private final AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService;

    private final IEduStudentBuyCourseService eduStudentBuyCourseService;

    private final IEduStudentBuyVipService eduStudentBuyVipService;

    private final IEduCourseDataService eduCourseDataService;

    private final IEduBannerService eduBannerService;

    private final IChunkService chunkService;

    private final AliOssMultipartUploadFileService aliOssMultipartUploadFileService;

    public UploadImgController(IEduCourseService eduCourseService, AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService, IEduStudentBuyCourseService eduStudentBuyCourseService, IEduStudentBuyVipService eduStudentBuyVipService, IEduCourseDataService eduCourseDataService, IEduBannerService eduBannerService, IChunkService chunkService, AliOssMultipartUploadFileService aliOssMultipartUploadFileService) {
        this.eduCourseService = eduCourseService;
        this.aliVodeUploadImageLocalFileService = aliVodeUploadImageLocalFileService;
        this.eduStudentBuyCourseService = eduStudentBuyCourseService;
        this.eduStudentBuyVipService = eduStudentBuyVipService;
        this.eduCourseDataService = eduCourseDataService;
        this.eduBannerService = eduBannerService;
        this.chunkService = chunkService;
        this.aliOssMultipartUploadFileService = aliOssMultipartUploadFileService;
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

    @EnableSysLog("【后台】上传课程配套资料")
    @PostMapping("uploadFile")
    public BaseResult uploadFile(@RequestParam("fileResource") MultipartFile fileResource,
                                 @RequestParam(value = "courseDataId",required = false) Long courseDataId,
                                 @RequestParam(value = "fileKey",required = true) String fileKey,
                                 HttpServletRequest request){
        if(courseDataId==null||fileKey==null){
            return BaseResult.fail("上传失败");
        }else {
            Map<String,Object> map = new HashMap<>();
            // 根据fileKey查询数据
            EduCourseData dbEduCourseData = eduCourseDataService.findByFileKey(fileKey);
            if(dbEduCourseData!=null){
                map.put("courseDataId",courseDataId);
                map.put("urlPath",dbEduCourseData.getDownloadAddress());
                return BaseResult.success("极速秒传成功",map);
            }
            map = aliVodeUploadImageLocalFileService.uploadCourseResource(fileResource,courseDataId,request,fileKey);
            map.put("courseDataId",courseDataId);
            return BaseResult.success(map);
        }
    }

    /**
     * 获取实时长传进度
     * @param fileKey
     * @return
     */
    @GetMapping("getUploadPercent/{fileKey}")
    public BaseResult getUploadPercent(@PathVariable String fileKey,HttpServletRequest request){
        System.out.println("从request获取===="+fileKey+"=====getUploadPercent方法====："+request.getServletContext().getAttribute("upload_percent"+fileKey));
        int percent = request.getServletContext().getAttribute("upload_percent"+fileKey) == null ? -1: (int) request.getServletContext().getAttribute("upload_percent"+fileKey);
        Map<String,Object> map = new HashMap<>();
        map.put("percent",percent);
        return BaseResult.success(map);
    }

    /**
     * 功能描述：
     * @param request
     * @return
     */
    @GetMapping("/downFileFromOss")
    public void downFileFromOss(HttpServletRequest request, HttpServletResponse response){
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");
        String yourBucketName = ConstantPropertiesUtil.BUCKET_COURSE_NAME;
        String endpoint = ConstantPropertiesUtil.END_POINT;
        String accessKeyId = ConstantPropertiesUtil.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtil.ACCESS_KEY_SECRET;
        String fileName = request.getParameter("fileName");
        response.addHeader("Content-Disposition", "attachment;filename="+fileName);
        try {
            String token = request.getParameter("studentToken");
            EduCourseData dbEduCourseData = eduCourseDataService.getByDownloadAddress(fileName);
            boolean isCanViewVideo = canViewVideo(token,dbEduCourseData.getCourseId());
            if(isCanViewVideo){
                // 创建OSSClient实例。
                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                OSSObject oSSObject = ossClient.getObject(yourBucketName, fileName);
                byte[] Object = IOUtils.toByteArray(oSSObject.getObjectContent());
                response.getOutputStream().write(Object);
            }else {
                throw new BadRequestException("下载失败，请先购买课程或加入齐天大会员");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断是否有下载该课程资料的权限
     * @param token
     * @param courseId
     * @return
     */
    private boolean canViewVideo(String token,Long courseId){
        if(token!= null && !token.equals("null")&& !token.equals("")){
            // 获取登录学员ID
            DecodedJWT decodedJWT = JWTUtil.verify(token);
            Long studentId= Long.parseLong(decodedJWT.getClaim("studentId").asString());
            if(studentId==null){
                return false;
            }
            // 是否是VIP会员
            EduStudentBuyVip dbEduStudentBuyVip = eduStudentBuyVipService.findByStudentId(studentId);
            if(dbEduStudentBuyVip!=null&&dbEduStudentBuyVip.getIsPayment()==1){
                return true;
                // 是否已经购买课程
            }else{
                EduStudentBuyCourse dbEduStudentBuyCourse = eduStudentBuyCourseService.findByCourseIdAndStudentId(courseId,studentId);
                if(dbEduStudentBuyCourse!=null && dbEduStudentBuyCourse.getIsPayment()==1){
                    return true;
                }
            }

        }
        return false;
    }


    @EnableSysLog("【后台】上传轮播图封面")
    @PostMapping("uploadBanner")
    public BaseResult uploadBanner(@RequestParam("fileResource") MultipartFile fileResource,
                                  @RequestParam(value = "bannerId",required = false) Long bannerId){
        System.out.println("获取轮播图ID："+bannerId);
        Map<String,Object> map = aliVodeUploadImageLocalFileService.uploadImageLocalFile(fileResource,"course");
        eduBannerService.uploadBanner(bannerId,map.get("urlPath").toString());
        return BaseResult.success(map);
    }

    @EnableSysLog("【后台】根据文件唯一标志获取课程资料")
    @GetMapping("/check/{fileKey}")
    public BaseResult getCourseDataByKey(@PathVariable String fileKey){
        Map<String,Object> map = new HashMap<>();
        // 根据fileKey查询数据
        EduCourseData dbEduCourseData = eduCourseDataService.findByFileKey(fileKey);
        if(dbEduCourseData!=null){
            map.put("isUploaded",true);
            return BaseResult.success("极速秒传成功",map);
        }else {
            //如果没有，就查找分片信息，并返回给前端
            List<Integer> chunkList = chunkService.selectChunkListByMd5(fileKey);
            map.put("chunkList",chunkList);
            map.put("isUploaded",false);
            return BaseResult.success(map);
        }
    }

    @EnableSysLog("分片上传课程资料")
    @PostMapping("chunk")
    public BaseResult chunk(@RequestParam("chunk") MultipartFile chunk,
                            @RequestParam("md5") String md5,
                            @RequestParam("index") Integer index,
                            @RequestParam("chunkTotal")Integer chunkTotal,
                            @RequestParam("fileSize")Long fileSize,
                            @RequestParam("fileName")String fileName,
                            @RequestParam("chunkSize")Long chunkSize,
                            @RequestParam("courseId")Long courseId) throws IOException, ClientException {

        String[] splits = fileName.split("\\.");
        String type = splits[splits.length-1];
        String resultFileName = filePath+md5+"."+type;
        chunkService.saveChunk(chunk,md5,index,chunkSize,resultFileName,courseId);

        // 上传到阿里云oss文件名称
        // 1、获取文件后缀名
        String substring = "."+type;
        //构建日期名称：2020-02-03
        String fileDate = new DateTime().toString("yyyy-MM-dd");
        String filename = md5+"-"+fileDate+"-"+courseId+substring;
        String hostName = ConstantPropertiesUtil.HOST_COURSE;
        String objectName = hostName+"/"+filename;
        logger.info("上传分片："+index +" ,"+chunkTotal+","+fileName+","+resultFileName);
        if(Objects.equals(index, chunkTotal)){
            EduCourseData tempEduCourseData = new EduCourseData();
            tempEduCourseData.setCourseId(courseId);
            tempEduCourseData.setName(fileName);
            tempEduCourseData.setDownloadAddress(objectName);
            tempEduCourseData.setFileKey(md5);
            tempEduCourseData.setFileSize(fileSize);
            // 上传到服务器完成，开始上传到阿里云oss
            aliOssMultipartUploadFileService.uploadChunkFile(resultFileName,objectName,courseId);
            chunkService.deleteChunkByMd5(md5);
            eduCourseDataService.save(tempEduCourseData);
            // 删除本地临时文件
            Path tempPath = Paths.get(resultFileName);
            Files.delete(tempPath);
            return BaseResult.success("文件上传成功");
        }else{
            return BaseResult.success("分片上传成功");
        }
        
    }

}
