package cn.xueden.system.service;

import cn.xueden.system.service.dto.SysLogQueryCriteria;
import org.springframework.data.domain.Pageable;

/**功能描述：日志管理接口
 * @author:梁志杰
 * @date:2023/3/21
 * @description:cn.xueden.system.service
 * @version:1.0
 */
public interface ILogService {
    /**
     * 获取日志列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    Object getList(SysLogQueryCriteria queryCriteria, Pageable pageable);
}
