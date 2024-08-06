package com.android.ItemTouchHelper.helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

import com.android.ItemTouchHelper.Adapter;


public class HelperCallback extends ItemTouchHelper.Callback {


    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlag = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipeFlag = ItemTouchHelper.LEFT;

        return makeMovementFlags(dragFlag, swipeFlag);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Adapter adapter = (Adapter) recyclerView.getAdapter();
        adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY != 0 && dX == 0) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        //REMARK => 实现侧滑菜单效果
        //ItemView的根布局是一个FrameLayout，Menu和Content实际上是FrameLayout下的两个子View
        //Content显示在Menu的上方，当Content通过滑动动画向左侧平移，Menu就逐渐露出来，形成侧滑菜单效果
        Adapter.ItemBaseViewHolder holder = (Adapter.ItemBaseViewHolder) viewHolder;
        if (viewHolder instanceof Adapter.ActionWidthVH) {
            if (dX < -holder.mActionContainer.getWidth()) {
                dX =- holder.mActionContainer.getWidth();
            }
            holder.mViewContent.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof Adapter.ItemBaseViewHolder) {
            holder.mViewContent.setTranslationX(dX);
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }


}
