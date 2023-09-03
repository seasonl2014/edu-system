package cn.xueden.sms;


import cn.xueden.edu.domain.EduAliOss;
import cn.xueden.edu.domain.EduSms;
import cn.xueden.edu.repository.EduAliOssRepository;
import cn.xueden.edu.repository.EduSmsRepository;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.springframework.stereotype.Component;

/**功能描述：阿里云短信发送
 * @author:梁志杰
 * @date:2023/5/25
 * @description:cn.xueden.sms
 * @version:1.0
 */
@Component
public class SendSmsService {
    private  final EduSmsRepository eduSmsRepository;

    private final EduAliOssRepository eduAliOssRepository;

    public SendSmsService(EduSmsRepository eduSmsRepository, EduAliOssRepository eduAliOssRepository) {
        this.eduSmsRepository = eduSmsRepository;
        this.eduAliOssRepository = eduAliOssRepository;
    }

    /**
     * 给手机发送验证码
     * @param code
     * @param phone
     * @return
     */
    public  String sendCodeByPhone(String code,String phone) {
        // 获取阿里云对象存储信息
        EduSms dbEduSms = eduSmsRepository.findFirstByOrderByIdDesc();
        // 获取阿里云对象存储信息
        EduAliOss dbEduAliOss = eduAliOssRepository.findFirstByOrderByIdDesc();
        DefaultProfile profile = DefaultProfile.getProfile(dbEduSms.getRegionId(), dbEduAliOss.getAccessKeyID(), dbEduAliOss.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", dbEduSms.getRegionId());
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", dbEduSms.getSignName());
        request.putQueryParameter("TemplateCode", dbEduSms.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println("返回值response："+response);
            System.out.println("返回值response.getData()："+response.getData());
            return response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "", "");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", "13558395109");
        request.putQueryParameter("SignName", "墨鱼课堂");
        request.putQueryParameter("TemplateCode", "SMS_209832260");
        request.putQueryParameter("TemplateParam", "{\"code\":\"256049\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

}
