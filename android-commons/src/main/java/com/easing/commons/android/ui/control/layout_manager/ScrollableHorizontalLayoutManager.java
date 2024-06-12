package com.easing.commons.android.ui.control.layout_manager;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScrollableHorizontalLayoutManager extends LinearLayoutManager implements IScrollable {

    boolean scrollable = true;

    public ScrollableHorizontalLayoutManager(Context context) {
        super(context);
        setOrientation(RecyclerView.HORIZONTAL);
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
        return scrollable;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
