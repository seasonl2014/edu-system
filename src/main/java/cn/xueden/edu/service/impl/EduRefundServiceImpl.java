package cn.xueden.edu.service.impl;


import cn.xueden.edu.domain.EduRefund;
import cn.xueden.edu.repository.EduRefundRepository;
import cn.xueden.edu.service.IEduRefundService;
import cn.xueden.edu.service.dto.EduRefundQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：退款记录业务接口实现类
 * @author:梁志杰
 * @date:2023/9/6
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduRefundServiceImpl implements IEduRefundService {

    private final EduRefundRepository eduRefundRepository;

    public EduRefundServiceImpl(EduRefundRepository eduRefundRepository) {
        this.eduRefundRepository = eduRefundRepository;
    }

    /**
     * 获取退款记录列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduRefundQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduRefund> page = eduRefundRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }
}
