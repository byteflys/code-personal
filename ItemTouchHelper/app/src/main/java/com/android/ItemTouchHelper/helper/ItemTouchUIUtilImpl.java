package com.android.ItemTouchHelper.helper;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.view.View;

public class ItemTouchUIUtilImpl implements ItemTouchUIUtil {

    // TODO : 13
    @Override
    public void clearView(View view) {
        view.setTranslationX(0f);
        view.setTranslationY(0f);
    }

    @Override
    public void onSelected(View view) {

    }

    // TODO : 14
    @Override
    public void onDraw(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        view.setTranslationX(dX);
        view.setTranslationY(dY);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {

    }
}
