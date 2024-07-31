package com.android.code.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import androidx.annotation.NonNull;

import com.android.code.R;

//带回弹效果的ScrollView，拉到顶部和底部时，仍可以继续拉出一部分空白区域，松手后回弹至正常状态
//这个控件没有对padding进行处理，请不要为ScrollView设置padding
@SuppressWarnings("all")
public class BounceScrollView extends ScrollView {

    //ScrollView包裹的子View
    View contentView;

    //记录最近一次正常状态下的控件边界，即没有重定位时的状态
    //当控件处于弹性拉伸状态时，最近一次的正常状态，就是刚滑到顶端或底端时的状态
    Rect recentNormalBound = new Rect();

    //上次点击时的Y坐标
    float y;

    //动画是否在进行中
    boolean isAnimationFinished = true;

    //最大宽高
    int maxWidth = Integer.MAX_VALUE;
    int maxHeight = Integer.MAX_VALUE;

    //宽高占屏幕的最大比例
    float maxScreenRatioX = 0F;
    float maxScreenRatioY = 0F;

    public BounceScrollView(Context context) {
        this(context, null);
    }

    public BounceScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化，读取属性
    protected void init(Context context, AttributeSet attributeSet) {
        int screenWidth = Device.screenWidth();
        int screenHeight = Device.screenHeight();
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.BounceScrollView);
        maxWidth = (int) attrs.getDimension(R.styleable.BounceScrollView_maxWidth, Integer.MAX_VALUE);
        maxHeight = (int) attrs.getDimension(R.styleable.BounceScrollView_maxHeight, Integer.MAX_VALUE);
        maxScreenRatioX = attrs.getFloat(R.styleable.BounceScrollView_maxScreenRatioX, 0);
        maxScreenRatioY = attrs.getFloat(R.styleable.BounceScrollView_maxScreenRatioY, 0);
        if (maxScreenRatioX > 0)
            if (maxScreenRatioX * screenWidth < maxWidth)
                maxWidth = (int) (maxScreenRatioX * screenWidth);
        if (maxScreenRatioY > 0)
            if (maxScreenRatioY * screenHeight < maxHeight)
                maxHeight = (int) (maxScreenRatioY * screenHeight);
    }

    //布局解析完毕
    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0)
            contentView = getChildAt(0);
        super.onFinishInflate();
    }

    //重写测量方法，以支持最大宽高、整倍宽高功能
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthSize = Math.min(widthSize, maxWidth);
        int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightSize = Math.min(heightSize, maxHeight);
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);
    }

    //始终可以竖直滚动，因为有弹簧效果
    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN)
            y = e.getY();
        //手指松开时，执行状态还原动画
        if (action == MotionEvent.ACTION_UP)
            startRecoverAnimation();
        //手指移动时，滑动ScrollView或重新定位ContentView
        if (action == MotionEvent.ACTION_MOVE) {
            float preY = y;
            float nowY = e.getY();
            int dy = (int) (nowY - preY);
            //保存新的坐标位置
            y = nowY;
            //动画未结束，则不进行重新定位
            if (!isAnimationFinished) {
                dy = 0;
                isAnimationFinished = true;
            }
            //当滚动到顶端或低端时，不再滑动，而是重新定位ContentView
            if (needRelayout()) {
                //保存正常状态下的布局位置
                if (recentNormalBound.isEmpty())
                    recentNormalBound.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
                //重新定位ContentView
                contentView.layout(contentView.getLeft(), contentView.getTop() + dy / 2, contentView.getRight(), contentView.getBottom() + dy / 2);
            }
        }
        return super.onTouchEvent(e);
    }

    //开始状态还原动画
    protected void startRecoverAnimation() {
        if (recentNormalBound.isEmpty())
            return;
        //开启动画，将控件还原至最近的正常状态
        contentView.animate()
                .yBy(recentNormalBound.top - contentView.getTop())
                .setDuration(200)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                        if (animation.getAnimatedFraction() == 1f) {
//                    isAnimationFinished = true;
                        }
                    }
                }).start();
        //重置正常边界
        recentNormalBound.setEmpty();
        //重装动画状态
        isAnimationFinished = false;
    }

    //判断是否需要对ContentView进行重新定位
    //当控件滑倒最顶端或最底端时，对ContentView进行重新定位，从而产生弹簧效果，这时ScrollY是不变的
    protected boolean needRelayout() {
        int invisibleHeight = contentView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY <= 0 || scrollY >= invisibleHeight)
            return true;
        return false;
    }
}



