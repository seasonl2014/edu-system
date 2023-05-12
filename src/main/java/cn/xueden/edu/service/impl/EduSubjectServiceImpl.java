package cn.xueden.edu.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.xueden.edu.alivod.AliVodCategoryService;
import cn.xueden.edu.alivod.dto.CategoryDto;
import cn.xueden.edu.converter.EduSubjectConverter;
import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.repository.EduSubjectRepository;
import cn.xueden.edu.service.IEduSubjectService;
import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.edu.utils.EduSubjectTreeBuilder;
import cn.xueden.edu.utils.ListPageUtils;
import cn.xueden.edu.vo.EduSubjectModel;
import cn.xueden.edu.vo.EduSubjectTreeNodeModel;
import cn.xueden.exception.BadRequestException;
import cn.xueden.utils.PageUtil;
import cn.xueden.utils.PageVo;

import org.springframework.beans.BeanUtils;
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

    private final AliVodCategoryService vidCategoryClient;

    public EduSubjectServiceImpl(EduSubjectRepository eduSubjectRepository, AliVodCategoryService vidCategoryClient) {
        this.eduSubjectRepository = eduSubjectRepository;
        this.vidCategoryClient = vidCategoryClient;
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

    @Override
    public EduSubject getById(Long id) {
        return eduSubjectRepository.findById(id).orElseGet(EduSubject::new);
    }

    /**
     * 更新课程分类信息
     * @param eduSubject
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editSubject(EduSubject eduSubject) {
        // 根据ID获取分类信息
        EduSubject dbEduSubject= eduSubjectRepository.getReferenceById(eduSubject.getId());
        if(dbEduSubject!=null&& !dbEduSubject.getId().equals(eduSubject.getId())){
            throw new BadRequestException("更新失败，分类名称已经存在！");
        }else{
            BeanUtil.copyProperties(eduSubject,dbEduSubject,
                    CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            eduSubjectRepository.save(dbEduSubject);
        }
    }

    /**
     * 新增或修改分类获取课程分类树形结构
     * @return
     */
    @Override
    public List<EduSubjectTreeNodeModel> getParentEduSubjectTree() {
        List<EduSubjectModel> productCategoryVOList = findAll();
        List<EduSubjectTreeNodeModel> nodeVOS=EduSubjectConverter.converterToTreeNodeVO(productCategoryVOList);
        return  EduSubjectTreeBuilder.buildParent(nodeVOS);
    }

    /**
     * 添加课程类别
     * @param eduSubjectModel
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(EduSubjectModel eduSubjectModel) {
        EduSubject eduSubject = new EduSubject();
        BeanUtils.copyProperties(eduSubjectModel,eduSubject);
        // 获取阿里云点播对应课程分类ID
        // 如果父节点不为空
        if(eduSubjectModel.getParentId()!=null&&eduSubjectModel.getParentId()!=0){
            // 获取父节点
            EduSubject dbEduSubject = eduSubjectRepository.getReferenceById(eduSubjectModel.getParentId());
            if(dbEduSubject!=null){
                eduSubjectModel.setCateId(dbEduSubject.getCateId());
            }
        }
        // 添加到阿里云视频点播分类
        CategoryDto categoryDto = vidCategoryClient.addAliVodCategory(eduSubjectModel);
        if(categoryDto!=null){
            eduSubject.setCateId(categoryDto.getCateId());
            eduSubjectRepository.save(eduSubject);
        }else {
            throw new BadRequestException("添加失败，请检查阿里云视频点播配置是否正确！");
        }

    }
}
