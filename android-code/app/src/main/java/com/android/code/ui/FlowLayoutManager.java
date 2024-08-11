package com.android.code.ui;

import android.graphics.Rect;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

//自定义流式布局管理器
//这个Demo主要是为了演示自定义LayoutManager
//很多代码都做了简化处理，在性能上并不够好
public class FlowLayoutManager extends RecyclerView.LayoutManager {

    //保存所有item偏移量信息，所有数据高度和
    int totalHeight = 0;
    SparseArray<Rect> itemBounds = new SparseArray<>();

    //滑动偏移量
    int verticalScrollOffset = 0;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        //摆放
        if (getItemCount() <= 0)
            return;
        //布局动画
        if (state.isPreLayout())
            return;
        //将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler);

        int offsetY = 0;
        int offsetX = 0;
        int itemHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            int w = getDecoratedMeasuredWidth(view);
            int h = getDecoratedMeasuredHeight(view);
            itemHeight = h;
            Rect bound = itemBounds.get(i);
            if (bound == null)
                bound = new Rect();
            //需要换行
            if (offsetX + w > getWidth()) {
                //换行的View的值
                offsetY += h;
                offsetX = w;
                bound.set(0, offsetY, w, offsetY + h);
            } else {
                //不需要换行
                bound.set(offsetX, offsetY, offsetX + w, offsetY + h);
                offsetX += w;
            }
            //计算前View的布局区域，然后放到allItemframs数组
            itemBounds.put(i, bound);
        }
        totalHeight = offsetY + itemHeight;

        //回收不可见的元素
        recyclerViewFillView(recycler, state);
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        //实际滑动距离
        int trval = dy;
        if (verticalScrollOffset + dy < 0) {
            trval = -verticalScrollOffset;
        } else if (verticalScrollOffset + dy > totalHeight - getHeight()) {
            trval = totalHeight - getHeight() - verticalScrollOffset;
        }

        //边界值判断
        verticalScrollOffset += trval;

        //平移容器内的item
        offsetChildrenVertical(trval);
        recyclerViewFillView(recycler, state);
        return trval;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    protected void recyclerViewFillView(RecyclerView.Recycler recycler, RecyclerView.State state) {
        //清空RecyclerView的子View
        detachAndScrapAttachedViews(recycler);
        Rect phoneFrame = new Rect(0, verticalScrollOffset, getWidth(), verticalScrollOffset + getHeight());
        //将滑出屏幕的view进行回收
        Rect childRect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            Rect child = itemBounds.get(i);
            if (!Rect.intersects(phoneFrame, child))
                removeAndRecycleView(childView, recycler);
        }

        //可见区域出现在屏幕上的子view
        for (int j = 0; j < getItemCount(); j++)
            if (Rect.intersects(phoneFrame, itemBounds.get(j))) {
                //scrap回收池里面拿的
                View scrap = recycler.getViewForPosition(j);
                measureChildWithMargins(scrap, 0, 0);
                addView(scrap);
                Rect frame = itemBounds.get(j);
                layoutDecorated(scrap, frame.left, frame.top - verticalScrollOffset, frame.right, frame.bottom - verticalScrollOffset);
            }
    }
}

