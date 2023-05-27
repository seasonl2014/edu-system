package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduEnvironmenParam;

import java.util.List;

/**功能描述：开发环境参数业务接口
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduEnvironmenParamService {
    /**
     * 根据课程ID获取相应的开发环境参数数据
     * @param courseId
     * @return
     */
    Object getEduEnvironmenParamListByCourseId(Long courseId);


    /**
     * 保存或更新课程环境
     * @param eduEnvironmenParamList
     */
    void saveOrUpdateEnvironmen(List<EduEnvironmenParam> eduEnvironmenParamList);
}
