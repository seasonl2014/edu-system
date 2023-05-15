package cn.xueden.edu.controller;

import cn.xueden.annotation.EnableSysLog;
import cn.xueden.base.BaseResult;
import cn.xueden.edu.alivod.utils.AliyunVODSDKUtils;
import cn.xueden.edu.alivod.utils.ConstantPropertiesUtil;
import cn.xueden.edu.domain.EduCourse;
import cn.xueden.edu.service.IEduCourseChapterService;
import cn.xueden.edu.service.IEduCourseService;
import cn.xueden.edu.service.IEduEnvironmenParamService;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**功能描述：前台详情页前端控制器
 * @author:梁志杰
 * @date:2023/5/14
 * @description:cn.xueden.edu.controller
 * @version:1.0
 */
@RestController
@RequestMapping("edu/front/detail")
public class EduDetailController {

    private final IEduCourseService eduCourseService;

    private final IEduEnvironmenParamService eduEnvironmenParamService;

    private final IEduCourseChapterService eduCourseChapterService;

    public EduDetailController(IEduCourseService eduCourseService, IEduEnvironmenParamService eduEnvironmenParamService, IEduCourseChapterService eduCourseChapterService) {
        this.eduCourseService = eduCourseService;
        this.eduEnvironmenParamService = eduEnvironmenParamService;
        this.eduCourseChapterService = eduCourseChapterService;
    }

    @EnableSysLog("【前台】根据课程ID获取课程详情详细")
    @GetMapping("/{id}")
    public BaseResult detail(@PathVariable Long id){
        EduCourse dbEduCourse = eduCourseService.getById(id);
        dbEduCourse.setViewCount(dbEduCourse.getViewCount()+1);
        eduCourseService.editCourse(dbEduCourse);
        return BaseResult.success(dbEduCourse);
    }

    @EnableSysLog("【前台】根据课程ID获取相应的开发环境参数数据")
    @GetMapping("/getParamListByCourseId/{courseId}")
    public BaseResult getEduEnvironmenParamListByCourseId(@PathVariable Long courseId){
        if(null==courseId){
            return BaseResult.fail("获取数据失败！");
        }else {
            return BaseResult.success(eduEnvironmenParamService.getEduEnvironmenParamListByCourseId(courseId));
        }
    }

    @EnableSysLog("【前台】根据课程ID获取相应的课程大纲数据")
    @GetMapping("/getChapterListByCourseId/{courseId}")
    public BaseResult getEduCourseChapterListByCourseId(@PathVariable Long courseId){
        if(null==courseId){
            return BaseResult.fail("获取数据失败！");
        }else {
            return BaseResult.success(eduCourseChapterService.getEduCourseChapterListByCourseId(courseId));
        }
    }

    @EnableSysLog("【前台】根据视频id获取阿里云视频点播播放凭证")
    @GetMapping("/getPlayAuth/{vid}")
    public BaseResult getPlayAutoId(@PathVariable String vid){
        return getPlayAutoId2(vid);
    }

    @EnableSysLog("【前台】根据视频id获取播放凭证")
    @PostMapping("getPlayAuth2/{vid}")
    public BaseResult getPlayAutoId2(@PathVariable String vid){
        try {
            //初始化客户端、请求对象和相应对象
            DefaultAcsClient client = AliyunVODSDKUtils.initVodClient(ConstantPropertiesUtil.ACCESS_KEY_ID, ConstantPropertiesUtil.ACCESS_KEY_SECRET);

            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();

            //设置请求参数
            request.setVideoId(vid);
            //获取请求响应
            response = client.getAcsResponse(request);

            //输出请求结果
            //播放凭证
            String playAuth = response.getPlayAuth();
            Map<String,Object> map = new HashMap<>();
            map.put("playAuth",playAuth);
            return BaseResult.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResult.fail("获取播放凭证失败");
        }

    }
}
