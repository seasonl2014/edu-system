package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduVipType;
import cn.xueden.edu.service.dto.EduVipTypeQueryCriteria;
import org.springframework.data.domain.Pageable;

/**功能描述：VIP类别业务接口
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduVipTypeService {
    /**
     * 根据条件分页获取VIP类别列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduVipTypeQueryCriteria queryCriteria, Pageable pageable);

    /**
     * 添加类别
     * @param eduVipType
     */
    void addEduVipType(EduVipType eduVipType);
}
