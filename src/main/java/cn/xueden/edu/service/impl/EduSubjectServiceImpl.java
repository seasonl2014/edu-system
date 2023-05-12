package cn.xueden.edu.service.impl;

import cn.xueden.edu.converter.EduSubjectConverter;
import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.repository.EduSubjectRepository;
import cn.xueden.edu.service.IEduSubjectService;
import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.edu.utils.EduSubjectTreeBuilder;
import cn.xueden.edu.utils.ListPageUtils;
import cn.xueden.edu.vo.EduSubjectModel;
import cn.xueden.edu.vo.EduSubjectTreeNodeModel;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.PageVo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**功能描述：课程分类业务接口实现
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.service.impl
 * @version:1.0
 */
@Service
@Transactional(readOnly = true)
public class EduSubjectServiceImpl implements IEduSubjectService {

    private final EduSubjectRepository eduSubjectRepository;

    public EduSubjectServiceImpl(EduSubjectRepository eduSubjectRepository) {
        this.eduSubjectRepository = eduSubjectRepository;
    }

    /**
     * 获取课程分类树形结构
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    @Override
    public Object categoryTree(EduSubjectQueryCriteria queryCriteria, PageVo pageVo) {
        List<EduSubjectModel> productCategoryVOList = findAll();
        List<EduSubjectTreeNodeModel> nodeVOS=EduSubjectConverter.converterToTreeNodeVO(productCategoryVOList);
        List<EduSubjectTreeNodeModel> tree = EduSubjectTreeBuilder.build(nodeVOS);
        List<EduSubjectTreeNodeModel> page;
        page= ListPageUtils.page(tree, pageVo.getPageSize(),pageVo.getPageIndex());
        return  PageUtil.toPage(page,tree.size());
    }

    /**
     * 所有课程类别
     * @return
     */
    public List<EduSubjectModel> findAll(){
        List<EduSubject> productCategories = eduSubjectRepository.findAll();
        return EduSubjectConverter.converterToVOList(productCategories);
    }

}
