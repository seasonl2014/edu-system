package cn.xueden.edu.utils;

import cn.xueden.edu.vo.EduSubjectTreeNodeModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author:梁志杰
 * @date:2023/5/12
 * @description:cn.xueden.edu.utils
 * @version:1.0
 */
public class EduSubjectTreeBuilder {
    private static int count=1;

    /**
     * 构建多级
     * @param nodes
     * @return
     */
    public static List<EduSubjectTreeNodeModel> build(List<EduSubjectTreeNodeModel> nodes){
        //根节点
        List<EduSubjectTreeNodeModel> rootMenu = new ArrayList<>();
        for (EduSubjectTreeNodeModel nav : nodes) {
            if(nav.getParentId()==0){
                nav.setLev(1);
                rootMenu.add(nav);
            }
        }
        /* 根据Menu类的order排序 */
        Collections.sort(rootMenu,EduSubjectTreeNodeModel.order());
        /*为根菜单设置子菜单，getChild是递归调用的*/
        for (EduSubjectTreeNodeModel nav : rootMenu) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<EduSubjectTreeNodeModel> childList = getChild(nav, nodes);
            nav.setChildren(childList);//给根节点设置子节点
        }
        return rootMenu;
    }

    /**
     * 获取子菜单
     * @param pNode
     * @param nodes
     * @return
     */
    private static List<EduSubjectTreeNodeModel> getChild(EduSubjectTreeNodeModel pNode, List<EduSubjectTreeNodeModel> nodes) {
        //子菜单
        List<EduSubjectTreeNodeModel> childList = new ArrayList<>();
        for (EduSubjectTreeNodeModel nav : nodes) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getParentId().equals(pNode.getId())){
                nav.setLev(pNode.getLev()+1);
                childList.add(nav);
            }
        }
        //递归
        for (EduSubjectTreeNodeModel nav : childList) {
            nav.setChildren(getChild(nav, nodes));
        }
        //排序
        Collections.sort(childList,EduSubjectTreeNodeModel.order());
        //如果节点下没有子节点，返回一个空List（递归退出）
        if(childList.size() == 0){
            return null;
        }
        return childList;
    }

    /**
     * 新增或修改分类获取课程分类树形结构
     * @param nodes
     * @return
     */
    public static List<EduSubjectTreeNodeModel> buildParent(List<EduSubjectTreeNodeModel> nodes) {
        //根节点
        List<EduSubjectTreeNodeModel> rootMenu = new ArrayList<>();
        for (EduSubjectTreeNodeModel nav : nodes) {
            if(nav.getParentId()==0){
                nav.setLev(1);
                rootMenu.add(nav);
            }
        }
        /* 根据Menu类的order排序 */
        Collections.sort(rootMenu,EduSubjectTreeNodeModel.order());
        /*为根菜单设置子菜单，getChild是递归调用的*/
        for (EduSubjectTreeNodeModel nav : rootMenu) {
            /* 获取根节点下的所有子节点 使用getChild方法*/
            List<EduSubjectTreeNodeModel> childList = getParentChild(nav, nodes);
            nav.setChildren(childList);//给根节点设置子节点
        }
        return rootMenu;
    }

    private static List<EduSubjectTreeNodeModel> getParentChild(EduSubjectTreeNodeModel pNode, List<EduSubjectTreeNodeModel> nodes) {
        //子菜单
        List<EduSubjectTreeNodeModel> childList = new ArrayList<>();
        for (EduSubjectTreeNodeModel nav : nodes) {
            // 遍历所有节点，将所有菜单的父id与传过来的根节点的id比较
            //相等说明：为该根节点的子节点。
            if(nav.getParentId().equals(pNode.getId())){
                nav.setLev(2);
                childList.add(nav);
            }
        }
        return childList;
    }
}
