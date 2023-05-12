package cn.xueden.edu.converter;

import cn.xueden.edu.domain.EduSubject;
import cn.xueden.edu.vo.EduSubjectModel;
import cn.xueden.edu.vo.EduSubjectTreeNodeModel;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**功能描述：课程分类转换类
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.converter
 * @version:1.0
 */
public class EduSubjectConverter {

    /**
     * 转Model
     * @param eduSubject
     * @return
     */
    public static EduSubjectModel converterToEduSubjectModel(EduSubject eduSubject) {
        EduSubjectModel eduSubjectModel = new EduSubjectModel();
        BeanUtils.copyProperties(eduSubject,eduSubjectModel);
        return eduSubjectModel;
    }

    /**
     * 转voList
     * @param eduSubjects
     * @return
     */
    public static List<EduSubjectModel> converterToVOList(List<EduSubject> eduSubjects) {
        List<EduSubjectModel> EduSubjectVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(eduSubjects)){
            for (EduSubject eduSubject : eduSubjects) {
                EduSubjectModel eduSubjectModel = new EduSubjectModel();
                BeanUtils.copyProperties(eduSubject,eduSubjectModel);
                EduSubjectVOS.add(eduSubjectModel);
            }
        }
        return EduSubjectVOS;
    }

    /**
     * 转树节点
     * @param eduSubjectVOList
     * @return
     */
    public static List<EduSubjectTreeNodeModel> converterToTreeNodeVO(List<EduSubjectModel> eduSubjectVOList) {
        List<EduSubjectTreeNodeModel> nodes=new ArrayList<>();
        if(!CollectionUtils.isEmpty(eduSubjectVOList)){
            for (EduSubjectModel eduSubjectModel : eduSubjectVOList) {
                EduSubjectTreeNodeModel eduSubjectTreeNodeModel = new EduSubjectTreeNodeModel();
                BeanUtils.copyProperties(eduSubjectModel,eduSubjectTreeNodeModel);
                nodes.add(eduSubjectTreeNodeModel);
            }
        }
        return nodes;
    }

}
