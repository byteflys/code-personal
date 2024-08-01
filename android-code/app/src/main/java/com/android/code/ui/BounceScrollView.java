package com.android.code.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.android.code.R;

// scroll view that supports drag out of bound limit
// and auto bounce back on hand up
@SuppressWarnings("all")
public class BounceScrollView extends ScrollView {

    View contentView;

    Rect recentNormalBound = new Rect();

    float previousY;

    ObjectAnimator topPropertyAnimator;
    boolean isAnimationFinished = true;

    int maxWidth = Integer.MAX_VALUE;
    int maxHeight = Integer.MAX_VALUE;

    float maxScreenRatioX = 0F;
    float maxScreenRatioY = 0F;

    public BounceScrollView(Context context) {
        this(context, null);
    }

    public BounceScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

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

    @Override
    protected void onFinishInflate() {
        if (getChildCount() > 0)
            contentView = getChildAt(0);
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // new width spec
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        widthSize = Math.min(widthSize, maxWidth);
        int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        // new height spec
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        heightSize = Math.min(heightSize, maxHeight);
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
        // measure by new spec
        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // stop previous animation
        if (!isAnimationFinished) {
            previousY = e.getY();
            topPropertyAnimator.cancel();
            isAnimationFinished = true;
            return super.onTouchEvent(e);
        }
        int action = e.getAction();
        if (action == MotionEvent.ACTION_DOWN)
            previousY = e.getY();
        // record latest normal bound
        // or relayout to break bound limits
        if (action == MotionEvent.ACTION_MOVE) {
            float preY = previousY;
            float nowY = e.getY();
            int dy = (int) (nowY - preY);
            previousY = nowY;
            if (needRelayout()) {
                if (recentNormalBound.isEmpty())
                    recentNormalBound.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
                contentView.layout(contentView.getLeft(), contentView.getTop() + dy / 2, contentView.getRight(), contentView.getBottom() + dy / 2);
            }
        }
        // start recover animation if needed
        if (action == MotionEvent.ACTION_UP)
            startRecoverAnimation();
        return super.onTouchEvent(e);
    }

    // recover to normal bound
    protected void startRecoverAnimation() {
        if (recentNormalBound.isEmpty())
            return;
        topPropertyAnimator = ObjectAnimator.ofInt(contentView, "top", contentView.getTop(), recentNormalBound.top);
        topPropertyAnimator.setDuration(1000);
        topPropertyAnimator.addUpdateListener(animation -> {
            if (animation.getAnimatedFraction() == 1f)
                isAnimationFinished = true;
        });
        topPropertyAnimator.start();
        isAnimationFinished = false;
    }

    // when content reach top or bottom
    // can not scroll any more
    // we can only update layout attribute to break bound limits
    protected boolean needRelayout() {
        if (!recentNormalBound.isEmpty())
            return true;
        int invisibleHeight = contentView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY <= 0 || scrollY >= invisibleHeight)
            return true;
        return false;
    }
}