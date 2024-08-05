package com.android.ItemTouchHelper.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.android.ItemTouchHelper.util.Console;
import com.android.ItemTouchHelper.util.MotionEvents;

public class MRecyclerView extends RecyclerView {

    public static final String TAG = "ItemTouchHelperDebug";

    public MRecyclerView(Context context) {
        super(context);
    }

    public MRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        Console.debug("==========", MotionEvents.name(e), "==========");
        Console.debug("RecyclerView.dispatchTouchEvent");
        boolean ret = super.dispatchTouchEvent(e);
        Console.debug("=>", "RecyclerView.dispatchTouchEvent", ret);
        return ret;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        Console.debug("RecyclerView.onInterceptTouchEvent");
        boolean ret = super.onInterceptTouchEvent(e);
        Console.debug("=>", "RecyclerView.onInterceptTouchEvent", ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        Console.debug("RecyclerView.onTouchEvent");
        boolean ret = super.onTouchEvent(e);
        Console.debug("=>", "RecyclerView.onTouchEvent", ret);
        return ret;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        Console.debug("RecyclerView.requestDisallowInterceptTouchEvent");
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        Console.debug("=>", "RecyclerView.requestDisallowInterceptTouchEvent", "void");
    }
}

