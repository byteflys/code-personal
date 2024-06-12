package com.easing.commons.android.ui.control.list.easy_list;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;

import androidx.core.view.MotionEventCompat;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.ui.dialog.TipBox;

import libSwipRecyclerView.SwipeRecyclerView;

//TODO => 阅读BounceRecyclerView和SwipeRecyclerView源码
//带有弹簧效果的RecyclerView
@SuppressWarnings("all")
public class BounceRecyclerView extends SwipeRecyclerView {

    static final int STATE_NORMAL = 0;
    static final int STATE_DRAG_TOP_OR_LEFT = 1;
    static final int STATE_DRAG_BOTTOM_OR_RIGHT = 2;
    static final int STATE_Bounce_BACK = 3;
    static final int STATE_FLING = 4;
    int mState = STATE_NORMAL;

    int mReleaseBackAnimDuration = 300;
    int mFlingBackAnimDuration = 300;

    static final int INVALID_POINTER = -1;

    final int mTouchSlop;
    int mOrientation; // horizontal or vertical
    float mLastMotionPos; // x-coordinate or y-coordinate of last event, base on mOrientation
    float mFrom;
    float mOffset;
    int mActivePointerId = INVALID_POINTER;

    boolean mEnableBounceEffectWhenDrag = false;
    boolean mEnableBounceEffectWhenFling = false;

    Animation BounceAnimation;
    Interpolator releaseBackAnimInterpolator;
    Interpolator flingBackAnimInterpolator;

    public BounceRecyclerView(Context context) {
        this(context, null);
    }

    public BounceRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        initAnimation();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mEnableBounceEffectWhenDrag && onInterceptTouchEventInternal(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    boolean onInterceptTouchEventInternal(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionPos = mOrientation == VERTICAL ? ev.getY() : ev.getX();
                mActivePointerId = ev.getPointerId(0);
                // If STATE_Bounce_BACK, we intercept the event and stop the animation.
                // If STATE_FLING, we do not intercept and allow the animation to finish.
                if (mState == STATE_Bounce_BACK) {
                    if (mOffset != 0) {
                        clearAnimation();
                        setState(mOffset > 0 ? STATE_DRAG_TOP_OR_LEFT : STATE_DRAG_BOTTOM_OR_RIGHT);
                    } else {
                        setState(STATE_NORMAL);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    break;
                }
                final float pos = mOrientation == VERTICAL ? ev.getY(pointerIndex) : ev.getX(pointerIndex);
                final float posDiff = pos - mLastMotionPos;
                mLastMotionPos = pos;
                if (!isDragged()) {
                    boolean canScrollUpOrLeft, canScrollDownOrRight;
                    final int offset = mOrientation == VERTICAL ?
                            super.computeVerticalScrollOffset() :
                            super.computeHorizontalScrollOffset();
                    final int range = mOrientation == VERTICAL ?
                            super.computeVerticalScrollRange() - super.computeVerticalScrollExtent() :
                            super.computeHorizontalScrollRange() - super.computeHorizontalScrollExtent();
                    if (range == 0) {
                        canScrollDownOrRight = canScrollUpOrLeft = false;
                    } else {
                        canScrollUpOrLeft = offset > 0;
                        canScrollDownOrRight = offset < (range - 1);
                    }
                    if (canScrollUpOrLeft && canScrollDownOrRight) {
                        break;
                    }
                    if ((Math.abs(posDiff) > mTouchSlop)) {
                        boolean isOverScroll = false;
                        if (!canScrollUpOrLeft && posDiff > 0) {
                            setState(STATE_DRAG_TOP_OR_LEFT);
                            isOverScroll = true;
                        } else if (!canScrollDownOrRight && posDiff < 0) {
                            setState(STATE_DRAG_BOTTOM_OR_RIGHT);
                            isOverScroll = true;
                        }
                        if (isOverScroll) {
                            // Prevent touch effect on item
                            MotionEvent fakeCancelEvent = MotionEvent.obtain(ev);
                            fakeCancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                            super.onTouchEvent(fakeCancelEvent);
                            fakeCancelEvent.recycle();
                            super.awakenScrollBars();

                            final ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                break;
        }
        boolean dragged = isDragged();
        return isDragged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mEnableBounceEffectWhenDrag && onTouchEventInternal(ev)) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    boolean onTouchEventInternal(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionPos = mOrientation == VERTICAL ? ev.getY() : ev.getX();
                mActivePointerId = ev.getPointerId(0);
                break;
            case MotionEvent.ACTION_MOVE: {
                if (mActivePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex < 0) {
                    break;
                }
                final float pos = mOrientation == VERTICAL ? ev.getY(pointerIndex) : ev.getX(pointerIndex);
                final float posDiff = pos - mLastMotionPos;
                mLastMotionPos = pos;
                if (!isDragged()) {
                    boolean canScrollUpOrLeft, canScrollDownOrRight;
                    final int offset = mOrientation == VERTICAL ?
                            super.computeVerticalScrollOffset() :
                            super.computeHorizontalScrollOffset();
                    final int range = mOrientation == VERTICAL ?
                            super.computeVerticalScrollRange() - super.computeVerticalScrollExtent() :
                            super.computeHorizontalScrollRange() - super.computeHorizontalScrollExtent();
                    if (range == 0) {
                        canScrollDownOrRight = canScrollUpOrLeft = false;
                    } else {
                        canScrollUpOrLeft = offset > 0;
                        canScrollDownOrRight = offset < (range - 1);
                    }
                    if (canScrollUpOrLeft && canScrollDownOrRight) {
                        break;
                    }

                    if ((Math.abs(posDiff) >= mTouchSlop)) {
                        boolean isOverScroll = false;
                        if (!canScrollUpOrLeft && posDiff > 0) {
                            setState(STATE_DRAG_TOP_OR_LEFT);
                            isOverScroll = true;
                        } else if (!canScrollDownOrRight && posDiff < 0) {
                            setState(STATE_DRAG_BOTTOM_OR_RIGHT);
                            isOverScroll = true;
                        }
                        if (isOverScroll) {
                            // Prevent touch effect on item
                            MotionEvent fakeCancelEvent = MotionEvent.obtain(ev);
                            fakeCancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                            super.onTouchEvent(fakeCancelEvent);
                            fakeCancelEvent.recycle();
                            super.awakenScrollBars();

                            final ViewParent parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                }
                if (isDragged()) {
                    mOffset += posDiff;
                    // correct mOffset
                    if ((isDraggedTopOrLeft() && mOffset <= 0) || (isDraggedBottomOrRight() && mOffset >= 0)) {
                        setState(STATE_NORMAL);
                        mOffset = 0;
                        // return to touch item
                        MotionEvent fakeDownEvent = MotionEvent.obtain(ev);
                        fakeDownEvent.setAction(MotionEvent.ACTION_DOWN);
                        super.onTouchEvent(fakeDownEvent);
                        fakeDownEvent.recycle();
                    }
                    invalidate();
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mLastMotionPos = mOrientation == VERTICAL ? ev.getY(index) : ev.getX(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP: {
                onSecondaryPointerUp(ev);
                final int index = ev.findPointerIndex(mActivePointerId);
                if (index != -1) {
                    mLastMotionPos = mOrientation == VERTICAL ? ev.getY(index) : ev.getX(index);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mOffset != 0) {
                    // Bounce back
                    mFrom = mOffset;
                    startReleaseAnimation();
                    setState(STATE_Bounce_BACK);
                }
                mActivePointerId = INVALID_POINTER;
            }
        }
        return isDragged();
    }

    @Override
    public void draw(Canvas canvas) {
        if (!mEnableBounceEffectWhenDrag && !mEnableBounceEffectWhenFling) {
            super.draw(canvas);
            return;
        }
        if (mState == STATE_NORMAL) {
            super.draw(canvas);
            return;
        }
        final int sc = canvas.save();

        // scale the canvas
        if (mOrientation == VERTICAL) {
            final int viewHeight = getHeight();
            final float scaleY = 1 + Math.abs(mOffset) / viewHeight * 0.3f;
            canvas.scale(1, scaleY, 0, mOffset >= 0 ? 0 : (viewHeight + getScrollY()));
        } else {
            final int viewWidth = getWidth();
            final float scaleX = 1 + Math.abs(mOffset) / viewWidth * 0.3f;
            canvas.scale(scaleX, 1, mOffset >= 0 ? 0 : (viewWidth + getScrollX()), 0);
        }

        super.draw(canvas);
        canvas.restoreToCount(sc);
    }

    protected void absorbGlows(int velocityX, int velocityY) {
        if (mEnableBounceEffectWhenFling && mState != STATE_FLING) {
            final int v = mOrientation == VERTICAL ? velocityY : velocityX;
            mFrom = -v * (1f / 60);
            startFlingAnimation();
            setState(STATE_FLING);
        }
    }

    void initAnimation() {
        BounceAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mOffset = mFrom * interpolatedTime;
                if (hasEnded()) {
                    mOffset = 0;
                    setState(STATE_NORMAL);
                }
                invalidate();
            }
        };

        releaseBackAnimInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return (float) Math.cos(Math.PI * v / 2);
            }
        };

        flingBackAnimInterpolator = new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return (float) Math.sin(Math.PI * v);
            }
        };
    }

    void startReleaseAnimation() {
        BounceAnimation.setDuration(mReleaseBackAnimDuration);
        BounceAnimation.setInterpolator(releaseBackAnimInterpolator);
        startAnimation(BounceAnimation);
    }

    void startFlingAnimation() {
        BounceAnimation.setDuration(mFlingBackAnimDuration);
        BounceAnimation.setInterpolator(flingBackAnimInterpolator);
        startAnimation(BounceAnimation);
    }

    void setState(int newState) {
        if (mState != newState) {
            mState = newState;
        }
    }

    boolean isDragged() {
        //TODO => 有时会异常
        return mState == STATE_DRAG_TOP_OR_LEFT || mState == STATE_DRAG_BOTTOM_OR_RIGHT;
    }

    boolean isDraggedTopOrLeft() {
        return mState == STATE_DRAG_TOP_OR_LEFT;
    }

    boolean isDraggedBottomOrRight() {
        return mState == STATE_DRAG_BOTTOM_OR_RIGHT;
    }

    void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = ev.getActionIndex();
        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (layout != null) {
            mOrientation = layout.canScrollHorizontally() ? HORIZONTAL : VERTICAL;
        }
    }

    public void setEnableBounceEffectWhenDrag(boolean enable) {
        mEnableBounceEffectWhenDrag = enable;
    }

    public void setEnableBounceEffectWhenFling(boolean enable) {
        mEnableBounceEffectWhenFling = enable;
    }

    public void setReleaseBackAnimDuration(int duration) {
        mReleaseBackAnimDuration = duration;
    }

    public void setFlingBackAnimDuration(int duration) {
        mFlingBackAnimDuration = duration;
    }

    //开启弹簧效果
    public BounceRecyclerView enableBounceEffect(boolean enable) {
        mEnableBounceEffectWhenDrag = enable;
        mEnableBounceEffectWhenFling = enable;
        return this;
    }
}
