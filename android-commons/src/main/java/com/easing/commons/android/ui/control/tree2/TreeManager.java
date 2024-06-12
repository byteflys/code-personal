package com.easing.commons.android.ui.control.tree2;

import java.util.LinkedList;
import java.util.List;

//本工具类很多方法需要传入一个List
//List的功能一般是用来存储发生改变的元素，用于树控件局部更新
@SuppressWarnings("all")
public class TreeManager {

    //按深度优先遍历全部结点
    public static <T extends TreeNode<T>> List<T> depthFirstSearch(List<T> list, T node, int depth) {
        if (list == null)
            list = new LinkedList();
        node.level = depth;
        list.add(node);
        for (T item : node.children) {
            item.parentId = node.id;
            item.parentNode = node;
            depthFirstSearch(list, (T) item, depth + 1);
        }
        return list;
    }

    //按深度优先遍历全部展开的结点
    public static <T extends TreeNode> List<T> visibleNodes(List<T> list, T node, int depth) {
        if (list == null)
            list = new LinkedList();
        node.level = depth;
        list.add(node);
        if (node.expand)
            for (Object item : node.children)
                visibleNodes(list, (T) item, depth + 1);
        return list;
    }

    //切换选中状态
    public static <T extends TreeNode> List<T> toggleSelection(List<T> list, T node) {
        if (list == null)
            list = new LinkedList();
        //全部取消或部分选，均切换为全选
        if (node.selection == 0) {
            node.selection = 1;
            list.add(node);
            //同步子结点选中状态
            syncChildSelection(list, node);
        }
        //全选，切换为全部取消
        else if (node.selection == 1) {
            node.selection = 0;
            list.add(node);
            //同步子结点选中状态
            syncChildSelection(list, node);
        }
        return list;
    }

    //子结点选中状态与父节点同步
    public static <T extends TreeNode<T>> void syncChildSelection(List<T> list, T node) {
        if (list == null)
            list = new LinkedList();
        for (T child : node.children) {
            if (child.selection != node.selection)
                list.add(child);
            child.selection = node.selection;
            syncChildSelection(list, child);
        }
    }

    //同步父结点选中状态
    public static <T extends TreeNode<T>> void syncParentSelection(List<T> list, T node) {
        if (list == null)
            list = new LinkedList();
        T parentNode = node.parentNode;
        while (parentNode != null) {
            int selectCount = 0;
            int unselectCount = 0;
            for (T child : parentNode.children) {
                if (child.selection == 0)
                    unselectCount++;
                if (child.selection == 1)
                    selectCount++;
            }
            int newSelection = 0;
            if (unselectCount == parentNode.children.size())
                newSelection = 0;
            else if (selectCount == parentNode.children.size())
                newSelection = 1;
            else
                newSelection = 2;
            if (parentNode.selection != newSelection)
                list.add(parentNode);
            parentNode.selection = newSelection;
            parentNode = parentNode.parentNode;
        }
    }

    //统计一个节点展开时，有多少子孙节点要展示，主要用于树控件动态刷新
    public static <T extends TreeNode> List<TreeNode<T>> getExpandItems(List<TreeNode<T>> list, TreeNode<T> node) {
        if (list == null)
            list = new LinkedList();
        for (TreeNode<T> child : node.children) {
            list.add(child);
            if (child.expand)
                for (TreeNode<T> childChild : child.children) {
                    list.add(childChild);
                    if (childChild.expand)
                        getExpandItems(list, childChild);
                }
        }
        return list;
    }

    //统计一个节点折叠时，有多少子孙节点要隐藏，主要用于树控件动态刷新
    public static <T extends TreeNode> List<TreeNode<T>> getCollapseItems(List<TreeNode<T>> list, TreeNode<T> node) {
        if (list == null)
            list = new LinkedList();
        for (TreeNode<T> child : node.children) {
            list.add(child);
            if (child.expand) {
                for (TreeNode<T> childChild : child.children) {
                    list.add(childChild);
                    if (childChild.expand)
                        getCollapseItems(list, childChild);
                }
            }
        }
        return list;
    }
}

