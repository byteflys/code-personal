package com.easing.commons.android.ui.control.tree2;

import com.easing.commons.android.format.Texts;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

@SuppressWarnings("all")
public class TreeNode<T extends TreeNode> {

    public String id = Texts.random();
    public String parentId;
    public Integer level = 0;

    //添加transient关键字，是告诉JSON等序列化功能，在序列化时忽略此字段
    //父节点包含子节点，字节点也包含父节点，对象之间相互循环引用，会导致序列化时产生死循环
    //可以通过transient来忽略parentNode字段，通过parentId来查找
    public transient T parentNode;
    public List<T> children = new LinkedList();

    //选中状态，0表示全部取消，1表示全部选中，2表示部分选中
    public int selection = 0;
    public boolean expand = true;
    @Getter
    public boolean visible = true;

    //虚拟节点，为形成一个完整的树而添加的特殊节点
    public boolean virtual = false;

    //以文本方式显示Node
    @Override
    public String toString() {
        return id;
    }

    //判断是不是父级下最后一个子结点
    public boolean isLastChild() {
        if (parentNode == null)
            return true;
        boolean last = true;
        int index = parentNode.children.indexOf(this);
        for (index++; index < parentNode.children.size(); index++) {
            T item = (T) parentNode.children.get(index);
            if (item.visible)
                return false;
        }
        return true;
    }

    //判断是不是叶节点（无可见的下级结点）
    public boolean isLeaf() {
        for (TreeNode node : children)
            if (node.visible)
                return false;
        return true;
    }

    //树结构转List
    public List<T> toList() {
        List<T> list = new LinkedList();
        T node = (T) this;
        return TreeManager.depthFirstSearch(list, node, 0);
    }

    //树结构转List，只显示已展开的结点
    public List<T> toExpandList() {
        List<T> list = new LinkedList();
        T node = (T) this;
        return TreeManager.visibleNodes(list, node, 0);
    }

    //选中所有节点
    public void selectAll() {
        selection = 1;
        TreeManager.syncChildSelection(null, (T) this);
    }

    //取消选中所有节点
    public void deselectAll() {
        selection = 0;
        TreeManager.syncChildSelection(null, (T) this);
    }

    //构建层级关系
    public void buildHierarchyRelation() {
        TreeManager.depthFirstSearch(null, (T) this, 0);
    }
}

