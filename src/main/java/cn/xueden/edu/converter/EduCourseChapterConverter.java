package cn.xueden.edu.converter;

import cn.xueden.edu.domain.EduCourseChapter;
import cn.xueden.edu.domain.EduCourseVideo;
import cn.xueden.edu.repository.EduCourseVideoRepository;
import cn.xueden.edu.vo.EduChapterModel;
import cn.xueden.edu.vo.EduChapterTreeNodeModel;
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

    /**
     * 转树节点
     * @param eduChapterVOList
     * @return
     */
    public static List<EduChapterModel> converterToTreeNodeVO(List<EduChapterModel> eduChapterVOList,
                                                                      EduCourseVideoRepository eduVideoMapper) {
        List<EduChapterModel> nodes=new ArrayList<>();
        if(!CollectionUtils.isEmpty(eduChapterVOList)){
            for (EduChapterModel eduChapterVO : eduChapterVOList) {
                List<EduCourseVideo> eduVideoList = eduVideoMapper.findByChapterIdOrderBySortAsc(eduChapterVO.getId());

                // 统计章节总时长
                float totalChapterDuration=0f;
                for (EduCourseVideo video:eduVideoList){
                    totalChapterDuration+=video.getDuration();
                }
                eduChapterVO.setDuration(totalChapterDuration);
                eduChapterVO.setChildren(eduVideoList);
                nodes.add(eduChapterVO);
            }
        }
        return nodes;
    }

}
