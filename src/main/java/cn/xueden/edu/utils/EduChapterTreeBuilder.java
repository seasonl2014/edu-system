package cn.xueden.edu.utils;

import cn.xueden.edu.vo.EduChapterModel;
import cn.xueden.edu.vo.EduChapterTreeNodeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author:梁志杰
 * @date:2023/5/29
 * @description:cn.xueden.edu.utils
 * @version:1.0
 */
public class EduChapterTreeBuilder {
    /**
     * 构建多级
     * @param nodes
     * @return
     */
    public static List<EduChapterModel> build(List<EduChapterModel> nodes){
        //根节点
        List<EduChapterModel> rootMenu = new ArrayList<>();
        for (EduChapterModel nav : nodes) {
            nav.setLev(1);
            rootMenu.add(nav);

        }
        /* 根据Menu类的order排序 */
        Collections.sort(rootMenu,EduChapterModel.order());
        return rootMenu;
    }
}
