package cn.xueden.edu.service;

import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.service.dto.EduSubjectQueryCriteria;
import cn.xueden.edu.vo.EduSubjectModel;
import cn.xueden.edu.vo.EduSubjectTreeNodeModel;
import cn.xueden.utils.PageVo;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**功能描述：课程分类业务接口
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.service
 * @version:1.0
 */
public interface IEduSubjectService {

    /**
     * 获取课程分类树形结构
     * @param queryCriteria
     * @param pageVo
     * @return
     */
    Object categoryTree(EduSubjectQueryCriteria queryCriteria, PageVo pageVo);

    /**
     * 根据房间ID获取课程分类信息
     * @param id
     * @return
     */
    EduSubject getById(Long id);

    /**
     * 更新课程分类信息
     * @param eduSubject
     */
    void editSubject(EduSubject eduSubject);

    /**
     * 新增或修改分类获取课程分类树形结构
     * @return
     */
    List<EduSubjectTreeNodeModel> getParentEduSubjectTree();

    /**
     * 添加课程类别
     * @param eduSubjectModel
     */
    void add(EduSubjectModel eduSubjectModel);

    /**
     * 获取栏目和课程
     * @param queryCriteria
     * @param pageable
     * @return
     */
    List<EduSubjectModel> getIndexColumnCourses(EduSubjectQueryCriteria queryCriteria, Pageable pageable);
}
