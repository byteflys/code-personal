package com.easing.commons.android.ui.control.layout_manager;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollableGridLayoutManager extends GridLayoutManager implements IScrollable {

    boolean scrollable = true;

    public ScrollableGridLayoutManager(Context context, int column) {
        super(context, column);
    }

    @Override
    public void scrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }

    @Override
    public boolean scrollable() {
        return scrollable;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return scrollable;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //这里捕获之前的数组越界问题...
            super.onLayoutChildren( recycler, state );
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }
}
