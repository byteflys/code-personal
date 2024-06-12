package com.easing.commons.android.ui.control.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui.adapter.ViewAdapter;
import com.easing.commons.android.value.measure.Rect;

import java.util.LinkedList;

//瀑布流布局（元素从左到右，从上到下，见缝就插，没有行的概念）
@SuppressWarnings("all")
public class FallsLayout extends ViewGroup {

    protected CommonActivity context;

    protected ViewAdapter adapter;

    protected int margin = Dimens.toPx(0);

    //记录所有的边界点
    protected final Object lock = new Object();
    protected final LinkedList<Integer> xList = new LinkedList();
    protected final LinkedList<Integer> yList = new LinkedList();

    //已经使用的区域
    protected final LinkedList<Rect> usedRects = new LinkedList();

    //父级宽度
    protected int parentWidth;

    public FallsLayout(Context context) {
        super(context, null);
    }

    public FallsLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        this.context = (CommonActivity) context;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(margin, margin, margin, margin);
        return lp;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        MarginLayoutParams lp = new MarginLayoutParams(getContext(), attrs);
        return lp;
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        MarginLayoutParams lp = new MarginLayoutParams(p);
        return lp;
    }

    @Override
    protected void onMeasure(int wSpec, int hSpec) {

        //解析测量参数
        int mode_w = MeasureSpec.getMode(wSpec);
        int mode_h = MeasureSpec.getMode(hSpec);
        int size_w = MeasureSpec.getSize(wSpec);
        int size_h = MeasureSpec.getSize(hSpec);

        //瀑布布局必须明确指定宽高
        if (mode_w != MeasureSpec.EXACTLY)
            throw new RuntimeException("FallsLayout must exactly specify width");
        parentWidth = size_w;

        //添加起始位置
        xList.clear();
        yList.clear();
        usedRects.clear();
        xList.add(getPaddingLeft());
        yList.add(getPaddingTop());

        //循环遍历子View，测量总尺寸
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            measureChild(child, wSpec, hSpec);
            int w = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int h = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            Rect rect = findRect(w, h);
            usedRects.add(rect);
            insertValue(rect.left, xList);
            insertValue(rect.right, xList);
            insertValue(rect.top, yList);
            insertValue(rect.bottom, yList);
        }

        //确定最终ViewGroup高度
        int contentHeight = yList.getLast() + getPaddingBottom();
        if (mode_h == MeasureSpec.AT_MOST && size_h < contentHeight)
            contentHeight = size_h;
        super.setMeasuredDimension(size_w, contentHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //循环遍历子View，测量总尺寸
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            Rect rect = usedRects.get(i);
            child.layout(rect.left + lp.leftMargin, rect.top + lp.topMargin, rect.right - lp.rightMargin, rect.bottom - lp.bottomMargin);
        }
    }

    //通过Adapter来AddView
    public void setViewAdapter(ViewAdapter adapter) {
        this.adapter = adapter;
        removeAllViews();
        for (int i = 0; i < adapter.getDatas().size(); i++) {
            View view = adapter.createView(i);
            final int pos = i;
            view.setOnClickListener(v -> adapter.onItemClick(pos));
            view.setOnLongClickListener(v -> adapter.onItemLongClick(pos));
            addView(view);
        }
        requestLayout();
    }

    //查找空白区域
    //尝试以每个边界点作为起点
    //如果和其它控件都不相交，也不超出parent宽度，则使用此边界点作为起点
    //否则切换到最底行最左侧显示
    protected Rect findRect(int w, int h) {
        synchronized (lock) {
            for (int x1 : xList)
                for (int i = 0; i < yList.size() - 1; i++) {
                    int y1 = yList.get(i);
                    int x2 = x1 + w;
                    int y2 = y1 + h;
                    //判断是否相交
                    Rect rect = new Rect(x1, y1, x2, y2);
                    boolean cross = false;
                    breakPoint:
                    for (Rect usedRect : usedRects)
                        if (Rect.isCrossed(rect, usedRect)) {
                            cross = true;
                            break breakPoint;
                        }
                    //不相交，不超出parent宽度，则可以放置在此位置
                    if (!cross)
                        if (x2 <= parentWidth - getPaddingRight() + Dimens.toPx(1)) //防止float和int转换时精度丢失，条件放宽松点
                            return rect;
                }
            //其它点都不合适，切换到最底部显示
            return new Rect(getPaddingLeft(), yList.getLast(), getPaddingLeft() + w, yList.getLast() + h);
        }
    }

    //记录临界点
    protected void insertValue(int value, LinkedList<Integer> intList) {
        int index = -1;
        for (int i = intList.size(); i > 0; i--) {
            int item = intList.get(i - 1);
            if (item == value) break;
            if (item < value) {
                index = i;
                break;
            }
        }
        if (index >= 0)
            intList.add(index, value);
    }
}



