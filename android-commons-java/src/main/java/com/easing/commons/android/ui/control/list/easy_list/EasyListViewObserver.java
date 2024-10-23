package com.easing.commons.android.ui.control.list.easy_list;

import androidx.recyclerview.widget.RecyclerView;

public class EasyListViewObserver extends RecyclerView.AdapterDataObserver {

    public void onChanged() {
        updateHeaderView();
    }

    public void onItemRangeChanged(int positionStart, int itemCount) {
        updateHeaderView();
    }

    public void onItemRangeInserted(int positionStart, int itemCount) {
        updateHeaderView();
    }

    public void onItemRangeRemoved(int positionStart, int itemCount) {
        updateHeaderView();
    }

    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        updateHeaderView();
    }

    public void updateHeaderView() {

    }
}
