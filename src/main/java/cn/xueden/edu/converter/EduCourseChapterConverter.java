package cn.xueden.edu.converter;

import cn.xueden.edu.domain.EduCourseChapter;
import cn.xueden.edu.vo.EduChapterModel;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**功能描述：课程大纲转换类
 * @author:梁志杰
 * @date:2023/5/15
 * @description:cn.xueden.edu.converter
 * @version:1.0
 */
public class EduCourseChapterConverter {

    /**
     * 转voList
     * @param eduCourseChapters
     * @return
     */
    public static List<EduChapterModel> converterToVOList(List<EduCourseChapter> eduCourseChapters) {
        List<EduChapterModel> eduChapterVOS=new ArrayList<>();
        if(!CollectionUtils.isEmpty(eduCourseChapters)){
            for (EduCourseChapter eduChapter : eduCourseChapters) {
                EduChapterModel eduChapterVO = new EduChapterModel();
                BeanUtils.copyProperties(eduChapter,eduChapterVO);
                eduChapterVOS.add(eduChapterVO);
            }
        }
        return eduChapterVOS;
    }
}
