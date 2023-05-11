package cn.xueden.edu.service.impl;


import cn.xueden.edu.domain.EduStudentId;
import cn.xueden.edu.repository.EduStudentIdRepository;
import cn.xueden.edu.service.IEduStudentIdService;
import cn.xueden.edu.service.dto.EduStudentIdQueryCriteria;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**功能描述：学生学号业务接口实现类
 * @author:梁志杰
 * @date:2023/5/11
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
public class EduStudentIdServiceImpl implements IEduStudentIdService {

    private final EduStudentIdRepository eduStudentIdRepository;

    public EduStudentIdServiceImpl(EduStudentIdRepository eduStudentIdRepository) {
        this.eduStudentIdRepository = eduStudentIdRepository;
    }

    /**
     * 根据条件分页获取学生学号列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(EduStudentIdQueryCriteria queryCriteria, Pageable pageable) {
        Page<EduStudentId> page = eduStudentIdRepository.findAll((root, query, criteriaBuilder)->
                QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }
}
