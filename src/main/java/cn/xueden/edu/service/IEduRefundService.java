package cn.xueden.edu.service;

import cn.xueden.edu.service.dto.EduRefundQueryCriteria;
import org.springframework.data.domain.Pageable;

/**功能描述：退款记录业务接口
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduRefundService {
    /**
     * 获取退款记录列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(EduRefundQueryCriteria queryCriteria, Pageable pageable);
}
