package com.easing.commons.android.ui.control.list.easy_list;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//实现橡皮筋拉伸回弹效果
//支持多指操控，一只手指滑到屏幕边界时，可以通过其它手指继续滑动
@SuppressWarnings("all")
public class ElasticListView extends RecyclerView {

    //正常状态
    private static final int STATE_NORMAL = 0;
    //沿顶部拉伸
    private static final int STATE_STRETCHING_TOP = 1;
    //沿底部拉伸
    private static final int STATE_STRETCHING_BOTTOM = 2;
    //回弹
    private static final int STATE_BOUNCING_BACK = 3;

    //滑动状态
    int state = STATE_NORMAL;

    //当前起作用的手指
    int activePointerId = -1;

    //记录上次手指位置
    float preY;

    //记录手指松开时的拉伸距离
    float maxOffset;

    //记录当前的拉伸距离，用于回弹动画
    float nowOffset;

    //回弹动画
    Animation bounceBackAnimation;
    Interpolator bounceBackInterpolator;

    public ElasticListView(Context context) {
        this(context, null);
    }

    public ElasticListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        //设置竖向布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        setLayoutManager(layoutManager);
        //启用默认的越界滚动效果，即滚动到边界时，继续滚动会产生一个水纹效果
        //手动滚动到边界时，使用橡皮筋拉伸效果，通过惯性自动滚动到边界时，仍然使用默认的水纹效果
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        //创建回弹动画
        bounceBackAnimation = new Animation() {
            @Override
            protected void applyTransformation(float progress, Transformation transformation) {
                nowOffset = maxOffset * progress;
                if (hasEnded()) {
                    nowOffset = 0;
                    state = STATE_NORMAL;
                }
                invalidate();
            }
        };
        //设置动画插值器
        bounceBackInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float timePercent) {
                float progress = (float) Math.cos(Math.PI * timePercent / 2);
                return progress;
            }
        };
        bounceBackAnimation.setInterpolator(bounceBackInterpolator);
        bounceBackAnimation.setDuration(300);
    }

    @Override
    public void draw(Canvas canvas) {
        //普通状态下，正常绘制
        if (state == STATE_NORMAL) {
            super.draw(canvas);
            return;
        }
        //拉伸或回弹状态下，拉伸画布
        int saveCount = canvas.save();
        int height = getHeight();
        float scale = 1 + Math.abs(nowOffset) / height * 0.3F;
        canvas.scale(1, scale, 0, nowOffset >= 0 ? 0 : height);
        super.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        if (onInterceptTouchEventInternal(e))
            return true;
        return super.onInterceptTouchEvent(e);
    }

    protected boolean onInterceptTouchEventInternal(MotionEvent e) {
        int action = MotionEventCompat.getActionMasked(e);
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                preY = e.getY();
                activePointerId = e.getPointerId(0);
                //暂停回弹状态，或恢复到正常状态
                if (state == STATE_BOUNCING_BACK) {
                    //回弹动画已结束，则恢复到正常状态
                    if (nowOffset == 0) {
                        state = STATE_NORMAL;
                        invalidate();
                        break;
                    }
                    //回弹动画尚未结束，切换到拉伸状态
                    //如果此时立刻将手松开，会开始一个新的回弹动画，从上次位置继续回弹
                    clearAnimation();
                    state = nowOffset > 0 ? STATE_STRETCHING_TOP : STATE_STRETCHING_BOTTOM;
                    invalidate();
                }
                break;
            }
        }
        //如果处于拉伸状态，则自己处理该事件，不分发给子节点
        //如果处于普通状态，则调用基类方法去处理
        boolean stretching = isStretching();
        return stretching;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (onTouchEventInternal(e))
            return true;
        return super.onTouchEvent(e);
    }

    protected boolean onTouchEventInternal(MotionEvent e) {
        int action = MotionEventCompat.getActionMasked(e);
        switch (action) {

            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = e.findPointerIndex(activePointerId);
                float nowY = e.getY(pointerIndex);
                float deltaY = nowY - preY;
                preY = nowY;
                //非拉伸状态下，需要处理切换到拉伸状态的case
                if (!isStretching()) {
                    //判断控件能不能滑动，有没有到达边界
                    boolean canScrollUp = false;
                    boolean canScrollDown = false;
                    //computeVerticalScrollRange返回控件全部内容的高度
                    //computeVerticalScrollExtent返回控件在屏幕上可展示区域的高度
                    //computeVerticalScrollOffset返回控件已滑过的距离
                    int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
                    if (range == 0) {
                        //内容较少，已经全部展示，不需要滑动
                        canScrollUp = false;
                        canScrollDown = false;
                    } else {
                        int offset = computeVerticalScrollOffset();
                        canScrollUp = offset > 0;
                        canScrollDown = offset < range;
                    }
                    //未到达边界，上下都可以滑动
                    if (canScrollUp && canScrollDown)
                        break;
                    //达到边界时，再继续滑动，即进入越界滚动状态
                    if (!canScrollUp && deltaY > 0)
                        state = STATE_STRETCHING_TOP;
                    if (!canScrollDown && deltaY < 0)
                        state = STATE_STRETCHING_BOTTOM;
                }
                //拉伸状态下，需要处理重绘控件，和切换到非拉伸状态的case
                if (isStretching()) {
                    nowOffset = nowOffset + deltaY;
                    if ((state == STATE_STRETCHING_TOP && nowOffset < 0) || (state == STATE_STRETCHING_BOTTOM && nowOffset > 0)) {
                        state = STATE_NORMAL;
                        nowOffset = 0;
                    }
                    invalidate();
                }
                break;
            }

            case MotionEventCompat.ACTION_POINTER_DOWN: {
                int index = MotionEventCompat.getActionIndex(e);
                preY = e.getY(index);
                activePointerId = e.getPointerId(index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP: {
                onPointerUp(e);
                int pointerIndex = e.findPointerIndex(activePointerId);
                preY = e.getY(pointerIndex);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (nowOffset != 0) {
                    maxOffset = nowOffset;
                    startAnimation(bounceBackAnimation);
                    state = STATE_BOUNCING_BACK;
                }
            }
        }
        //如果处于拉伸状态，则自己处理该事件，不分发给子节点
        //如果处于普通状态，则调用基类方法去处理
        boolean stretching = isStretching();
        return stretching;
    }

    //多指触控时，有一只手指松开
    protected void onPointerUp(MotionEvent e) {
        int pointerIndex = e.getActionIndex();
        int pointerId = e.getPointerId(pointerIndex);
        if (pointerId == activePointerId) {
            //选取一个新的有效手指
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            activePointerId = e.getPointerId(newPointerIndex);
        }
    }

    //判断控件是否处于拉伸状态
    protected boolean isStretching() {
        return state == STATE_STRETCHING_TOP || state == STATE_STRETCHING_BOTTOM;
    }
}

