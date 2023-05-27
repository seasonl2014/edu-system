package cn.xueden.edu.alivod.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.alivod.AliOssUploadImageLocalFileService;
import cn.xueden.edu.alivod.utils.ConstantPropertiesUtil;
import cn.xueden.edu.domain.EduCourseData;
import cn.xueden.edu.domain.EduStudentBuyCourse;
import cn.xueden.edu.domain.EduStudentBuyVip;
import cn.xueden.edu.service.IEduCourseDataService;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.IEduStudentBuyCourseService;
import cn.xueden.edu.service.IEduStudentBuyVipService;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.JWTUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.io.IOUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    private final IEduStudentBuyCourseService eduStudentBuyCourseService;

    private final IEduStudentBuyVipService eduStudentBuyVipService;

    private final IEduCourseDataService eduCourseDataService;

    public UploadImgController(IEduCourseService eduCourseService, AliOssUploadImageLocalFileService aliVodeUploadImageLocalFileService, IEduStudentBuyCourseService eduStudentBuyCourseService, IEduStudentBuyVipService eduStudentBuyVipService, IEduCourseDataService eduCourseDataService) {
        this.eduCourseService = eduCourseService;
        this.aliVodeUploadImageLocalFileService = aliVodeUploadImageLocalFileService;
        this.eduStudentBuyCourseService = eduStudentBuyCourseService;
        this.eduStudentBuyVipService = eduStudentBuyVipService;
        this.eduCourseDataService = eduCourseDataService;
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
                                 @RequestParam(value = "courseDataId",required = false) Long courseDataId){
        if(courseDataId==null){
            return BaseResult.fail("上传失败");
        }else {
            Map<String,Object> map = aliVodeUploadImageLocalFileService.uploadCourseResource(fileResource,courseDataId);
            map.put("courseDataId",courseDataId);
            return BaseResult.success(map);
        }
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

}
