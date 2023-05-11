package cn.xueden.edu.service.impl;


import cn.xueden.edu.domain.EduStudentId;
import cn.xueden.edu.repository.EduStudentIdRepository;
import cn.xueden.edu.service.IEduStudentIdService;
import cn.xueden.edu.service.dto.EduStudentIdQueryCriteria;
import cn.xueden.edu.vo.StudentIdModel;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.QueryHelp;
import cn.xueden.utils.XuedenUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 生成学生学号
     * @param studentIdModel
     */
    @Override
    public void addStudentId(StudentIdModel studentIdModel) {
        List<String> list = XuedenUtil.startFormat(studentIdModel.getStartNo(),studentIdModel.getNumber());
        List<EduStudentId> eduStudentIdList = list.stream().map(item-> {
            EduStudentId eduStudentId = new EduStudentId();
            eduStudentId.setStudentId(Long.parseLong(item));
            eduStudentId.setStatus(0);
            return eduStudentId;
        }).collect(Collectors.toList());
        try {
            eduStudentIdRepository.saveAll(eduStudentIdList);
        }catch (Exception e){
            throw new BadRequestException("生成失败，有重复的学号");
        }

    }
}
