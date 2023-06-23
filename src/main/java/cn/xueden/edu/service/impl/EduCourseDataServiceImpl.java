package cn.xueden.edu.service.impl;

import cn.xueden.edu.domain.EduCourseData;
import cn.xueden.edu.repository.EduCourseDataRepository;
import cn.xueden.edu.service.IEduCourseDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：课程资料业务接口实现
 * @author:梁志杰
 * @date:2023/5/19
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduCourseDataServiceImpl implements IEduCourseDataService {

    private final EduCourseDataRepository eduCourseDataRepository;

    public EduCourseDataServiceImpl(EduCourseDataRepository eduCourseDataRepository) {
        this.eduCourseDataRepository = eduCourseDataRepository;
    }

    /**
     * 根据课程ID获取相应的课程资料数据
     * @param courseId
     * @return
     */
    @Override
    public List<EduCourseData> getCourseDataByCourseId(Long courseId) {
        return eduCourseDataRepository.getCourseDataByCourseId(courseId);
    }

    /**
     * 根据ID获取课程资料
     * @param courseDataId
     * @return
     */
    @Override
    public EduCourseData getById(Long courseDataId) {
        return eduCourseDataRepository.findById(courseDataId).orElseGet(EduCourseData::new);
    }

    /**
     * 保存或更新课程配套资料
     * @param eduCourseDataList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateCourseData(List<EduCourseData> eduCourseDataList) {
        if(eduCourseDataList!=null&&eduCourseDataList.size()>0){
            eduCourseDataRepository.saveAll(eduCourseDataList);
        }
    }

    /**
     * 根据文件路径获取数据
     * @param fileName
     * @return
     */
    @Override
    public EduCourseData getByDownloadAddress(String fileName) {
        return eduCourseDataRepository.findByDownloadAddress(fileName);
    }

    /**
     * 根据fileKey查询数据
     * @param fileKey
     * @return
     */
    @Override
    public EduCourseData findByFileKey(String fileKey) {
        return eduCourseDataRepository.findFirstByFileKey(fileKey);
    }

    /**
     * 保存记录
     * @param tempEduCourseData
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EduCourseData tempEduCourseData) {
        eduCourseDataRepository.save(tempEduCourseData);
    }
}
