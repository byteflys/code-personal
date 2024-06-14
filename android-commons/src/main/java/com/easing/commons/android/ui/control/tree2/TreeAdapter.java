package com.easing.commons.android.ui.control.tree2;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewAdapter;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewHolder;
import com.easing.commons.android.view.Views;

import java.util.List;

import butterknife.BindView;

@SuppressWarnings("all")
public class TreeAdapter<T extends TreeNode> extends EasyListViewAdapter<T, TreeAdapter.TreeViewHolder> {

    TreeView<T> treeView;

    public TreeAdapter(TreeView<T> treeView) {
        this.itemLayout = R.layout.item_tree_node;
        this.treeView = treeView;
    }

    @Override
    public void onViewBind(TreeViewHolder holder, T node, int position) {
        holder.root.setVisibility(node.visible ? View.VISIBLE : View.GONE);
        Views.size(holder.root, Views.MATCH_PARENT, Views.WRAP_CONTENT);
        holder.root.setVisibility(node.virtual ? View.GONE : View.VISIBLE);
        holder.verticalLineLayout.removeAllViews();
        Views.padding(holder.verticalLineLayout, 0, 0, node.level == 0 ? 0 : Dimens.toPx(10), 0);
        if (node.level > 1) {
            T parent = (T) node.parentNode;
            for (int i = 0; i < node.level - 1; i++) {
                int layoutId = parent.isLastChild() ? R.layout.item_tree_vertical_line_2 : R.layout.item_tree_vertical_line_1;
                View item = Views.inflateAndAttach(ctx, layoutId, holder.verticalLineLayout, false);
                holder.verticalLineLayout.addView(item, 0);
                parent = (T) parent.parentNode;
            }
        }
        holder.verticalLineIcon.setVisibility(node.level == 0 ? View.GONE : View.VISIBLE);
        holder.horizontalLineIcon.setVisibility(node.level == 0 ? View.GONE : View.VISIBLE);
        Views.size(holder.verticalLineIcon, null, node.isLastChild() ? Dimens.toPx(16.5F) : Views.MATCH_PARENT);
        holder.expandIcon.setImageResource(node.expand ? R.drawable.icon_treeview_collapse : R.drawable.icon_treeview_expand);
        if (node.isLeaf())
            holder.expandIcon.setImageResource(R.drawable.icon_treeview_collapse_disable);
        if (node.selection == 0)
            holder.selectionIcon.setImageResource(R.drawable.icon_checkbox_none);
        if (node.selection == 1)
            holder.selectionIcon.setImageResource(R.drawable.icon_checkbox_all);
        holder.treeIcon.setVisibility(node.virtual ? View.GONE : View.VISIBLE);
        Views.text(holder.nameText, node.virtual ? "" : node);
        if (treeView.enableTextSelection)
            holder.nameText.setSelected(node == treeView.selectedNode);

        //折叠或展开
        holder.expandIcon.setOnClickListener(v -> {
            if (node.isLeaf())
                return;
            node.expand = !node.expand;
            int index = datas.indexOf(node);
            if (node.expand) {
                List expandItems = TreeManager.getExpandItems(null, node);
                datas.addAll(index + 1, expandItems);
                notifyItemChanged(index);
                notifyItemRangeInserted(index + 1, expandItems.size());
            } else {
                List collapseItems = TreeManager.getCollapseItems(null, node);
                datas.removeAll(collapseItems);
                notifyItemChanged(index);
                notifyItemRangeRemoved(index + 1, collapseItems.size());
            }
        });

        //勾选或取消
        holder.selectionIcon.setOnClickListener(v -> {
            List<T> list = TreeManager.toggleSelection(null, node);
            for (T item : list) {
                int index = datas.indexOf(item);
                if (index >= 0)
                    notifyItemChanged(index);
            }
        });

        //选定某项
        holder.nameText.setOnClickListener(v -> {
            if (!treeView.enableTextSelection)
                return;
            treeView.selectedNode = node;
            int index = datas.indexOf(node);
            notifyItemChanged(index);
        });

        //用户自定义绘制
        if (treeView.nodePainter != null)
            treeView.nodePainter.draw(holder, node);
    }

    public static class TreeViewHolder extends EasyListViewHolder {
        @BindView(R2.id.verticalLineLayout)
        public LinearLayout verticalLineLayout;
        @BindView(R2.id.image_line_vertical)
        public ImageView verticalLineIcon;
        @BindView(R2.id.image_line_horizontal)
        public ImageView horizontalLineIcon;
        @BindView(R2.id.image_expand)
        public ImageView expandIcon;
        @BindView(R2.id.image_selection)
        public ImageView selectionIcon;
        @BindView(R2.id.image_tree)
        public ImageView treeIcon;
        @BindView(R2.id.text_name)
        public TextView nameText;
    }
}
