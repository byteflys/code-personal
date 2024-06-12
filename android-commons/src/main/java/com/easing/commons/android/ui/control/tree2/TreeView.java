package com.easing.commons.android.ui.control.tree2;

import android.content.Context;
import android.util.AttributeSet;

import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.ui.control.list.easy_list.EasyListView;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewAdapter;

import java.util.List;

@SuppressWarnings("all")
public class TreeView<T extends TreeNode> extends EasyListView {

    TreeAdapter<T> treeAdapter;
    NodePainter<T> nodePainter;

    T selectedNode;

    boolean enableTextSelection = false;

    public TreeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        treeAdapter = new TreeAdapter(this);
        adapter(treeAdapter);
    }

    //设置适配器
    @Override
    public EasyListView adapter(EasyListViewAdapter adapter) {
        this.treeAdapter = (TreeAdapter) adapter;
        return super.adapter(adapter);
    }

    //自定义绘制结点
    public TreeView<T> nodePainter(NodePainter<T> nodePainter) {
        this.nodePainter = nodePainter;
        return this;
    }

    //开启文本选中样式
    public TreeView<T> enableTextSelection(boolean enableTextSelection) {
        this.enableTextSelection = enableTextSelection;
        return this;
    }

    //重置数据
    public TreeView<T> reset(List<T> nodeList) {
        treeAdapter.reset(nodeList);
        return this;
    }

    //重置数据
    public TreeView<T> reset(T node) {
        List<T> nodeList = node.toExpandList();
        nodeList = Collections.filter(nodeList, TreeNode::isVisible);
        treeAdapter.reset(nodeList);
        return this;
    }

    public interface NodePainter<T extends TreeNode> {

        void draw(TreeAdapter.TreeViewHolder holder, T node);
    }
}
