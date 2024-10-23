package com.easing.commons.android.ui.control.scroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.easing.commons.android.R;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.ui.dialog.TipBox;

//弹性滚动控件，拉到顶部和底部时，仍可以继续拉出一部分空白区域，然后弹回去
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
    TranslateAnimation animation;
    boolean isAnimationFinished = true;

    //最大宽高
    int maxWidth = Integer.MAX_VALUE;
    int maxHeight = Integer.MAX_VALUE;

    //让控件宽高，保持是某个值的整数倍
    int widthRadix = 0;
    int heightRadix = 0;

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
        widthRadix = (int) attrs.getDimension(R.styleable.BounceScrollView_widthDivision, 0);
        heightRadix = (int) attrs.getDimension(R.styleable.BounceScrollView_heightDivision, 0);
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
        if (widthMode == MeasureSpec.EXACTLY)
            widthSize = widthSize < maxWidth ? widthSize : maxWidth;
        if (widthMode == MeasureSpec.UNSPECIFIED)
            widthSize = widthSize < maxWidth ? widthSize : maxWidth;
        if (widthMode == MeasureSpec.AT_MOST)
            widthSize = widthSize < maxWidth ? widthSize : maxWidth;
        if (widthRadix > 0) {
            widthSize = widthSize - widthSize % widthRadix;
            if (widthSize < widthRadix)
                widthSize = widthRadix;
        }
        int maxWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY)
            heightSize = heightSize < maxHeight ? heightSize : maxHeight;
        if (heightMode == MeasureSpec.UNSPECIFIED)
            heightSize = heightSize < maxHeight ? heightSize : maxHeight;
        if (heightMode == MeasureSpec.AT_MOST)
            heightSize = heightSize < maxHeight ? heightSize : maxHeight;
        if (heightRadix > 0) {
            heightSize = heightSize - heightSize % heightRadix;
            if (heightSize < heightRadix)
                heightSize = heightRadix;
        }
        int maxHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);

        super.onMeasure(maxWidthMeasureSpec, maxHeightMeasureSpec);
    }

    //始终可以竖直滚动，因为有弹簧效果
    @Override
    public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN)
            y = e.getY();
        return super.dispatchTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        //手指松开时，执行状态还原动画
        if (action == MotionEvent.ACTION_UP)
            startRecoverAnimation();
        //手指移动时，滑动ScrollView或重新定位ContentView
        if (action == MotionEvent.ACTION_MOVE) {
            float preY = y;
            float nowY = e.getY();
            int dy = (int) (preY - nowY);
            //保存新的坐标位置
            y = nowY;
            //动画未结束，则不进行重新定位
            if (!isAnimationFinished) {
                dy = 0;
                isAnimationFinished = true;
            }
            //当滚动到顶端或低端时，不再滑动，而是重新定位ContentView
            boolean ifNeedRelayout = ifNeedRelayout();
            if (ifNeedRelayout) {
                //保存正常状态下的布局位置
                if (recentNormalBound.isEmpty())
                    recentNormalBound.set(contentView.getLeft(), contentView.getTop(), contentView.getRight(), contentView.getBottom());
                //重新定位ContentView
                contentView.layout(contentView.getLeft(), contentView.getTop() - dy / 2, contentView.getRight(), contentView.getBottom() - dy / 2);
            }
        }
        return super.onTouchEvent(e);
    }

    //开始状态还原动画
    protected void startRecoverAnimation() {
        if (recentNormalBound.isEmpty())
            return;
        //开启动画，将控件还原至最近的正常状态
        animation = new TranslateAnimation(0, 0, contentView.getTop(), recentNormalBound.top);
        animation.setDuration(200);
        contentView.startAnimation(animation);
        //将控件还原至最近的正常状态
        //layout方法会立刻改变控件位置坐标，但会等到Animation执行完毕才呈现效果
        contentView.layout(recentNormalBound.left, recentNormalBound.top, recentNormalBound.right, recentNormalBound.bottom);
        //重置正常边界
        recentNormalBound.setEmpty();
        //重装动画状态
        isAnimationFinished = false;
    }

    //判断是否需要对ContentView进行重新定位
    //当控件滑倒最顶端或最底端时，对ContentView进行重新定位，从而产生弹簧效果，这时ScrollY是不变的
    protected boolean ifNeedRelayout() {
        int invisibleHeight = contentView.getMeasuredHeight() - getHeight();
        int scrollY = getScrollY();
        if (scrollY <= 0 || scrollY >= invisibleHeight)
            return true;
        return false;
    }
}

