package cn.xueden.edu.service.impl;


import cn.xueden.edu.domain.EduVipType;
import cn.xueden.edu.repository.EduVipTypeRepository;
import cn.xueden.edu.service.IEduVipTypeService;
import cn.xueden.edu.service.dto.EduVipTypeQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**功能描述：VIP类别业务接口实现类
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduVipTypeServiceImpl implements IEduVipTypeService {

    private final EduVipTypeRepository eduVipTypeRepository;

    public EduVipTypeServiceImpl(EduVipTypeRepository eduVipTypeRepository) {
        this.eduVipTypeRepository = eduVipTypeRepository;
    }

    /**
     * 根据条件获取类别列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduVipTypeQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduVipType> page = eduVipTypeRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addEduVipType(EduVipType eduVipType) {
        eduVipType.setNums(0);
        eduVipTypeRepository.save(eduVipType);
    }
}
