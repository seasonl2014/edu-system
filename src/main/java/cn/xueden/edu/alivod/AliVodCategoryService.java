package cn.xueden.edu.alivod;

import cn.xueden.edu.alivod.dto.CategoryDto;

import cn.xueden.edu.domain.EduAliOss;
import cn.xueden.edu.repository.EduAliOssRepository;
import cn.xueden.edu.vo.EduSubjectModel;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.AddCategoryRequest;
import com.aliyuncs.vod.model.v20170321.AddCategoryResponse;
import org.springframework.stereotype.Component;
import static cn.xueden.edu.alivod.utils.AliyunVODSDKUtils.initVodClient;
/**功能描述：阿里云视频点播分类业务实现
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.alivod
 * @version:1.0
 */
@Component
public class AliVodCategoryService {

    private final EduAliOssRepository eduAliOssRepository;

    public AliVodCategoryService(EduAliOssRepository eduAliOssRepository) {
        this.eduAliOssRepository = eduAliOssRepository;
    }

    /**
     * 添加阿里云视频点播分类
     */
    public CategoryDto addAliVodCategory(EduSubjectModel eduSubjectModel){
        // 获取阿里云对象存储信息
        EduAliOss dbEduAliOss = eduAliOssRepository.findFirstByOrderByIdDesc();
        CategoryDto categoryDto = new CategoryDto();
        try {
            DefaultAcsClient client = initVodClient(dbEduAliOss.getAccessKeyID(), dbEduAliOss.getAccessKeySecret());
            AddCategoryResponse response = new AddCategoryResponse();
            AddCategoryRequest request = new AddCategoryRequest();
            // 分类名称
            request.setCateName(eduSubjectModel.getName());
            if(eduSubjectModel.getCateId()!=null){
                request.setParentId(eduSubjectModel.getCateId());
            }

            response = client.getAcsResponse(request);
            // 创建成功的分类信息
            categoryDto.setParentId(response.getCategory().getParentId());
            categoryDto.setCateId(response.getCategory().getCateId());
            categoryDto.setCateName(response.getCategory().getCateName());
            categoryDto.setLevel(response.getCategory().getLevel());
            System.out.print("ParentId = " + response.getCategory().getParentId() + "\n");
            System.out.print("CateId = " + response.getCategory().getCateId() + "\n");
            System.out.print("CateName = " + response.getCategory().getCateName() + "\n");
            System.out.print("Level = " + response.getCategory().getLevel() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        return categoryDto;
    }

}
