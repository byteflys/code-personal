package com.easing.commons.android.ui.control.CountdownButton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.easing.commons.android.R;
import com.easing.commons.android.clazz.Types;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;

//倒计时控件
@SuppressWarnings("all")
public class CountdownButton extends View {

    int endTime = 3;

    Action onTimeEnd;

    long createTime;

    boolean isTimeEnd = false;

    int textColor = Colors.BLACK_50;

    int w;
    int h;
    int radius;

    Paint textPaint = new Paint();
    Paint circlePaint = new Paint();

    public CountdownButton(Context context) {
        this(context, null);
    }

    public CountdownButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    public void init(Context context, AttributeSet attributeSet) {
        TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.CountdownButton);
        endTime = attrs.getInteger(R.styleable.CountdownButton_endTime, 1);
        textColor = attrs.getColor(R.styleable.CountdownButton_textColor, Colors.BLACK_50);
        createTime = Times.millisOfNow();
        circlePaint.setColor(Colors.WHITE_50);
        circlePaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textPaint.setTextSize(Dimens.toPx(14));
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = MeasureSpec.getSize(widthMeasureSpec);
        h = MeasureSpec.getSize(heightMeasureSpec);
        radius = Math.min(w, h);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //已结束，不绘制
        if (isTimeEnd) return;
        //计算时差
        long now = Times.millisOfNow();
        int left = Types.intValue((createTime + endTime * 1000 + 1000 - now) / 1000L);
        //计时未结束，绘制时间
        if (left >= 1) {
            //画圆
            canvas.drawCircle(w / 2, h / 2, w / 2, circlePaint);
            //画文字
            CanvasPainter.drawTextFromCenter(canvas, textPaint, Texts.toString(left), w / 2, h / 2);
            //绘制下一帧
            postInvalidateDelayed(100);
            return;
        }
        //计时结束，不再绘制
        isTimeEnd = true;
        setVisibility(View.GONE);
        if (onTimeEnd != null)
            onTimeEnd.runAndPostException();
    }

    public CountdownButton setEndTime(int second) {
        endTime = second;
        return this;
    }

    public void onTimeEnd(Action callback) {
        this.onTimeEnd = callback;
    }
}
