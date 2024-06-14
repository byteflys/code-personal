package com.easing.commons.android.ui.control.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.easing.commons.android.value.measure.Pos;
import com.easing.commons.android.view.Views;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//水平滚动容器，可模拟ViewPager，Gallery等效果
//这是一个不带Item复用的版本，如果需要考虑性能问题，可以放置一个FrameLayout作为Item，滑入时向Layout添加内容，滑出时移除Layout里面的内容即可
//由于没有Item复用功能，所有Item都是长活的，非常适合控件需要后台继续工作的情景
@SuppressWarnings("all")
public class HorizontalScroller<K, V extends View> extends HorizontalScrollView {

    final Context ctx;
    final LinearLayout container;

    final LinkedList<K> datas = new LinkedList();
    final LinkedList<V> views = new LinkedList();

    final Map<K, V> dataViewMap = new LinkedHashMap();
    final Map<V, K> viewDataMap = new LinkedHashMap();

    final Map<V, Boolean> visibilityMap = new LinkedHashMap();

    View leftPadder;
    View rightPadder;

    ViewMapper<K, V> viewMapper;
    OnSlideIn<K, V> onSlideIn;
    OnSlideOut<K, V> onSlideOut;
    OnItemSelect<K, V> onItemSelect;
    OnItemClick<K, V> onItemClick;
    OnParentClick<K, V> onParentClick;

    boolean autoAdjustToCenter = false;

    int lastScrollX;
    float lastClickX;
    long lastClickTime;

    V lastSelectView;

    int firstSelectionIndex = 0;

    public HorizontalScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        this.leftPadder = new View(context);
        this.rightPadder = new View(context);
        this.container = new LinearLayout(context);
        Views.size(container, Views.WRAP_CONTENT, Views.MATCH_PARENT);
        this.container.setHorizontalGravity(Gravity.LEFT);
        this.container.setVerticalGravity(Gravity.CENTER);
        this.container.addView(leftPadder);
        this.container.addView(rightPadder);
        super.addView(container);
        super.setHorizontalScrollBarEnabled(false);
        super.setVerticalScrollBarEnabled(false);
        super.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    protected void onDetachedFromWindow() {
        if (onSlideOut != null && lastSelectView != null) {
            K data = viewDataMap.get(lastSelectView);
            onSlideOut.onSlideOut(data, lastSelectView);
        }
        super.onDetachedFromWindow();
    }

    //添加的新的数据项/视图项
    public HorizontalScroller<K, V> add(K data) {
        post(() -> {
            //如果数据已存在，先删除已有视图
            if (datas.contains(data)) {
                View view = this.dataViewMap.get(data);
                datas.remove(data);
                views.remove(view);
                dataViewMap.remove(data);
                viewDataMap.remove(view);
                super.removeView(view);
            }

            //根据数据创建视图
            V view = viewMapper.buildView(data);
            container.addView(view, views.size());
            Views.size(view, getMeasuredWidth(), getMeasuredHeight());

            //记录新的数据项/视图项
            datas.add(data);
            views.add(view);
            dataViewMap.put(data, view);
            viewDataMap.put(view, data);
            visibilityMap.put(view, false);

            //选中默认项
            if (datas.size() - 1 == firstSelectionIndex) {
                visibilityMap.put(view, true);
                if (onSlideIn != null)
                    onSlideIn.onSlideIn(data, view);
                if (onItemSelect != null) {
                    onItemSelect.onItemSelect(data, view);
                    lastSelectView = view;
                }
                scrollToView(firstSelectionIndex);
            }
        });
        return this;
    }

    //添加的新的数据项/视图项
    public HorizontalScroller<K, V> add(K data, Integer width, Integer height) {
        post(() -> {
            //如果数据已存在，先删除已有视图
            if (datas.contains(data)) {
                View view = this.dataViewMap.get(data);
                datas.remove(data);
                views.remove(view);
                dataViewMap.remove(data);
                viewDataMap.remove(view);
                super.removeView(view);
            }

            //根据数据创建视图
            V view = viewMapper.buildView(data);
            container.addView(view, views.size());
            Views.size(view, width, height);

            //记录新的数据项/视图项
            datas.add(data);
            views.add(view);
            dataViewMap.put(data, view);
            viewDataMap.put(view, data);
            visibilityMap.put(view, false);

            //添加首个时，选中该数据
            if (datas.size() == 1) {
                visibilityMap.put(view, true);
                if (onSlideIn != null)
                    onSlideIn.onSlideIn(data, view);
                if (onItemSelect != null) {
                    onItemSelect.onItemSelect(data, view);
                    lastSelectView = view;
                }
            }
        });

        return this;
    }

    //批量添加数据
    public HorizontalScroller<K, V> addAll(K[] datas) {
        for (K data : datas)
            add(data);
        return this;
    }

    //批量添加数据
    public HorizontalScroller<K, V> addAll(List<K> datas) {
        for (K data : datas)
            add(data);
        return this;
    }

    //批量添加数据
    public HorizontalScroller<K, V> addAll(K[] datas, Integer width, Integer height) {
        for (K data : datas)
            add(data, width, height);
        return this;
    }

    //批量添加数据
    public HorizontalScroller<K, V> addAll(List<K> datas, Integer width, Integer height) {
        for (K data : datas)
            add(data, width, height);
        return this;
    }

    //重置数据
    public HorizontalScroller<K, V> removeAll() {
        datas.clear();
        views.clear();
        for (V view : visibilityMap.keySet())
            if (visibilityMap.get(view))
                if (onSlideOut != null)
                    onSlideOut.onSlideOut(viewDataMap.get(view), view);
        dataViewMap.clear();
        viewDataMap.clear();
        visibilityMap.clear();
        container.removeAllViews();
        return this;
    }

    //根据数据查找视图
    public V dataToView(K data) {
        return dataViewMap.get(data);
    }

    //根据视图查找数据
    public K viewToData(V view) {
        return viewDataMap.get(view);
    }

    //滚动至指定数据
    public HorizontalScroller<K, V> scrollToData(K data) {
        post(() -> {
            post(() -> {
                HorizontalScroller<K, V> scroller = HorizontalScroller.this;
                View view = dataToView(data);
                float dx = view.getLeft();
                super.scrollTo((int) dx, 0);
                adjustToCenter();
            });
        });
        return this;
    }

    //滚动至指定视图
    public HorizontalScroller<K, V> scrollToView(V view) {
        post(() -> {
            post(() -> {
                float dx = view.getLeft();
                super.scrollTo((int) dx, 0);
            });
        });
        return this;
    }

    //滚动至指定位置
    public HorizontalScroller<K, V> scrollToView(int index) {
        post(() -> {
            post(() -> {
                V view = views.get(index);
                float dx = view.getLeft();
                super.scrollTo((int) dx, 0);
            });
        });
        return this;
    }

    //监听Item点击
    public HorizontalScroller<K, V> onItemClick(OnItemClick<K, V> onItemClick) {
        this.onItemClick = onItemClick;
        return this;
    }

    //监听外部点击
    public HorizontalScroller<K, V> onParentClick(OnParentClick<K, V> onParentClick) {
        this.onParentClick = onParentClick;
        return this;
    }

    //鼠标弹起时，自动调整控件至居中位置
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ev.getX();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastScrollX = getScrollX();
            lastClickX = ev.getRawX();
            lastClickTime = System.currentTimeMillis();
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (autoAdjustToCenter)
                if (getScrollX() != lastScrollX)
                    postDelayed(() -> {
                        adjustToCenter();
                    }, 200);
            if (onParentClick != null)
                if (views.size() == 0)
                    onParentClick.onParentClick();
            if (onItemClick != null)
                if (Math.abs(ev.getRawX() - lastClickX) < 20)
                    if (System.currentTimeMillis() - lastClickTime < 1000) {
                        for (V view : views) {
                            Pos pos = Views.positionInScreen(view);
                            int width = view.getMeasuredWidth();
                            int x1 = pos.x;
                            int x2 = x1 + width;
                            if (x1 <= lastClickX && x2 >= lastClickX)
                                onItemClick.onItemClick(viewDataMap.get(view), view);
                        }
                    }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    //控制滑动速度
    @Override
    public void fling(int velocityX) {
        super.fling((int) (velocityX * 0.5));
    }

    //滑动过程中，监听视图滑入滑出
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        int parentWidth = super.getMeasuredWidth();
        for (V view : views) {
            Pos pos = Views.positionInParent(view);
            int width = view.getMeasuredWidth();
            int x1 = pos.x - super.getScrollX();
            int x2 = x1 + width;
            boolean invisible = x2 <= 0 || x1 >= parentWidth;
            boolean visible = !invisible;
            Boolean visibility = visibilityMap.get(view);
            K data = viewDataMap.get(view);
            if (visible != visibility) {
                visibilityMap.put(view, visible);
                //滑入
                if (visible)
                    if (onSlideIn != null)
                        onSlideIn.onSlideIn(data, view);
                //滑出
                if (!visible)
                    if (onSlideOut != null)
                        onSlideOut.onSlideOut(data, view);
            }
        }
    }

    //是否自动调整控件至居中位置
    public HorizontalScroller<K, V> autoAdjustToCenter(boolean autoAdjustToCenter) {
        this.autoAdjustToCenter = autoAdjustToCenter;
        return this;
    }

    //TODO
    //滑动结束时，将中间的控件调整到屏幕居中位置
    private void adjustToCenter() {
        int parentWidth = super.getMeasuredWidth();
        int sum = 0;
        for (V view : views) {
            Pos pos = Views.positionInParent(view);
            int width = view.getMeasuredWidth();
            int x1 = pos.x - super.getScrollX();
            int x2 = x1 + width;
            if (x1 <= parentWidth / 2 && x2 >= parentWidth / 2) {
                int target = sum + width / 2 - parentWidth / 2;
                post(() -> {
                    HorizontalScroller scroller = HorizontalScroller.this;
                    smoothScrollTo(target, 0);
                    //选中中间控件
                    if (onItemSelect != null) {
                        K data = viewDataMap.get(view);
                        onItemSelect.onItemSelect(data, view);
                        lastSelectView = view;
                    }
                });
                return;
            }
            sum += width;
        }
    }

    //重置所有Item大小
    public HorizontalScroller<K, V> resizeAll() {
        post(() -> {
            HorizontalScroller<K, V> scroller = HorizontalScroller.this;
            for (V view : views)
                Views.size(view, getMeasuredWidth(), getMeasuredHeight());
        });
        return this;
    }

    //重置所有Item大小
    public HorizontalScroller<K, V> resizeAll(Integer width, Integer height) {
        post(() -> {
            for (V view : views)
                Views.size(view, width, height);
        });
        return this;
    }

    //视图映射器，根据数据创建视图
    public HorizontalScroller<K, V> viewMapper(ViewMapper<K, V> mapper) {
        this.viewMapper = mapper;
        return this;
    }

    //监听视图滑入
    public HorizontalScroller<K, V> onSlideIn(OnSlideIn<K, V> onSlideIn) {
        this.onSlideIn = onSlideIn;
        return this;
    }

    //监听视图滑出
    public HorizontalScroller<K, V> onSlideOut(OnSlideOut<K, V> onSlideOut) {
        this.onSlideOut = onSlideOut;
        return this;
    }

    //监听视图选中
    public HorizontalScroller<K, V> onItemSelect(OnItemSelect<K, V> onItemSelect) {
        this.onItemSelect = onItemSelect;
        return this;
    }

    //默认选中项，这个方法需要再控件添加Item前调用
    public HorizontalScroller<K, V> firstSelection(int firstSelectionIndex) {
        this.firstSelectionIndex = firstSelectionIndex;
        return this;
    }

    //视图映射器，根据数据创建视图
    public interface ViewMapper<K, V extends View> {

        V buildView(K data);
    }

    //监听视图滑入
    public interface OnSlideIn<K, V extends View> {

        void onSlideIn(K data, V view);
    }

    //监听视图滑出
    public interface OnSlideOut<K, V extends View> {

        void onSlideOut(K data, V view);
    }

    //监听视图选中
    public interface OnItemSelect<K, V extends View> {

        void onItemSelect(K data, V view);
    }

    //监听Item点击
    public interface OnItemClick<K, V extends View> {

        void onItemClick(K data, V view);
    }

    //监听外部点击
    public interface OnParentClick<K, V extends View> {

        void onParentClick();
    }
}
