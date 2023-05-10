package cn.xueden.system.service.impl;


import cn.xueden.system.domain.SysLog;
import cn.xueden.system.repository.SysLogRepository;
import cn.xueden.system.service.ILogService;
import cn.xueden.system.service.dto.SysLogQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：日志管理接口实现类
 * @author:梁志杰
 * @date:2023/3/21
 * @description:cn.xueden.system.service.impl
 * @version:1.0
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysLogServiceImpl implements ILogService {

    private final SysLogRepository logRepository;

    public SysLogServiceImpl(SysLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    /**
     * 获取日志列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(SysLogQueryCriteria queryCriteria, Pageable pageable) {
        Page<SysLog> page = logRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }
}
