package com.easing.commons.android.MediaPlayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.easing.commons.android.R;
import com.easing.commons.android.clazz.Types;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;

import java.util.LinkedList;
import java.util.List;

//视频回放面板
@SuppressWarnings("all")
public class PlaybackView extends View {

    //视频时间段列表
    public static class TimeSpan {

        public int start;
        public int end;

        @Override
        public String toString() {
            String start = Times.formatDate(this.start * 1000L);
            String end = Times.formatDate(this.end * 1000L);
            return start + "  " + end;
        }

        public TimeSpan clone() {
            TimeSpan span = new TimeSpan();
            span.start = start;
            span.end = end;
            return span;
        }
    }

    //进度改变回调，只有手动滑动才能触发这个回调
    public interface OnProgressChange {
        void onProgressChange(int timeInSecond);
    }

    //有视频的时间段
    List<TimeSpan> spanList = new LinkedList();

    //进度改变回调
    OnProgressChange onProgressChange;

    //回放面板总大小
    int width;
    int height;

    //面板顶部位置
    int paneTop;
    int paneBottom;

    //开始时间，结束时间
    int startTime;
    int endTime;

    //每个格子的时长和宽度，多少格子显示一次时间
    int strideTime;
    int strideWidth;
    int strideCount;

    //预设的格子大小
    Integer presetedStrideTime;
    Integer presetedStrideWidth;
    Integer presetedStrideCount;

    //预设时间格式
    String presetedTimeFormat = "HH:mm:ss";

    //左端时间，中间时间，右端时间
    double t1;
    double t2;
    double t3;

    //背景画笔，空数据块画笔，视频数据块画笔，细时间轴画笔，粗时间轴画笔，当前时间轴画笔
    Paint backgroundPaint = new Paint();
    Paint emptyDataPaint = new Paint();
    Paint videoDataPaint = new Paint();
    Paint thinAxisPaint = new Paint();
    Paint boldAxisPaint = new Paint();
    Paint timeAxisPaint = new Paint();
    Paint strideTimePaint = new Paint();
    Paint mainTimePaint = new Paint();

    //元素高度
    int thinAxisHeight = Dimens.toPx(5);
    int boldAxisHeight = Dimens.toPx(10);
    int dataSliderHeight = Dimens.toPx(10);
    int timeAxisExtraHeight = Dimens.toPx(5);

    //元素宽度
    int thinAxisWidth = Dimens.toPx(1.5f);
    int boldAxisWidth = Dimens.toPx(2);
    int timeAxisWidth = Dimens.toPx(5);

    //元素字体大小
    int strideTimeTextSize = Dimens.toPx(10);
    int mainTimeTextSize = Dimens.toPx(18);

    //元素颜色
    int backgroundColor = R.color.color_white;
    int emptyDataSliderColor = R.color.color_05_grey;
    int videoDataSliderColor = R.color.color_apple_green;
    int thinAxisColor = R.color.color_60_grey;
    int boldAxisColor = R.color.color_60_grey;
    int timeAxisColor = R.color.color_light_blue;
    int strideTimeColor = R.color.color_60_grey;
    int mainTimeColor = R.color.color_60_grey;

    //正在画的时间轴
    int axisTime;

    //正在拖拽
    boolean isDragging = false;
    //惯性滑动
    boolean isInertiaSliding = false;
    //自动向有视频的位置滑动
    boolean isTargetSliding = false;
    //锁定控件，不允许用户操作
    boolean isLocking = false;

    //上次滑动位置
    double lastX;
    double lastY;
    long lastT;
    double targetT;
    double v;

    public PlaybackView(Context context) {
        this(context, null);
    }

    public PlaybackView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //控件初始化
    public void init(Context context, AttributeSet attributeSet) {
        setTimeSpanList(null);
        //初始化背景画笔
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(Colors.getColor(backgroundColor));
        //初始化空数据块画笔
        emptyDataPaint.setAntiAlias(true);
        emptyDataPaint.setColor(Colors.getColor(emptyDataSliderColor));
        //初始化视频数据块画笔
        videoDataPaint.setAntiAlias(true);
        videoDataPaint.setColor(Colors.getColor(videoDataSliderColor));
        //初始化细时间轴画笔
        thinAxisPaint.setAntiAlias(true);
        thinAxisPaint.setColor(Colors.getColor(thinAxisColor));
        thinAxisPaint.setStrokeWidth(thinAxisWidth);
        //初始化粗时间轴画笔
        boldAxisPaint.setAntiAlias(true);
        boldAxisPaint.setColor(Colors.getColor(boldAxisColor));
        boldAxisPaint.setStrokeWidth(boldAxisWidth);
        //初始化当前时间轴画笔
        timeAxisPaint.setAntiAlias(true);
        timeAxisPaint.setColor(Colors.getColor(timeAxisColor));
        timeAxisPaint.setStrokeWidth(timeAxisWidth);
        //初始化格子时间文字画笔
        strideTimePaint.setAntiAlias(true);
        strideTimePaint.setColor(Colors.getColor(strideTimeColor));
        strideTimePaint.setTextSize(strideTimeTextSize);
        //初始化当前时间文字画笔
        mainTimePaint.setAntiAlias(true);
        mainTimePaint.setColor(Colors.getColor(mainTimeColor));
        mainTimePaint.setTextSize(mainTimeTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //画背景
        CanvasPainter.drawRect(canvas, backgroundPaint, 0, paneTop, width, paneBottom);

        //画空数据块
        CanvasPainter.drawRect(canvas, emptyDataPaint, 0, paneTop, width, paneBottom);

        //画视频数据块
        for (TimeSpan span : spanList) {
            double x1 = width / 2D + strideWidth * (span.start - t2) / strideTime;
            double x2 = width / 2D + strideWidth * (span.end + 1 - t2) / strideTime;
            CanvasPainter.drawRect(canvas, backgroundPaint, x1, paneTop, x2, paneBottom);
            CanvasPainter.drawRect(canvas, videoDataPaint, x1, paneTop, x2, paneBottom);
        }

        //画顶部横线
        canvas.drawLine(0, paneTop, width, paneTop, boldAxisPaint);

        //画底部横线
        canvas.drawLine(0, paneBottom, width, paneBottom, boldAxisPaint);

        //画时间轴
        axisTime = Maths.roundByBase(t1, strideTime, false);
        while (axisTime >= t1 && axisTime <= t3) {
            double x = strideWidth * (axisTime - t1) / strideTime;
            String time = Times.formatDate(axisTime * 1000L, "HH:mm:ss");
            boolean isBigStride = axisTime / strideTime % strideCount == 0;
            //画顶部时间轴
            if (isBigStride)
                CanvasPainter.drawLine(canvas, boldAxisPaint, x, paneTop, x, paneTop + boldAxisHeight);
            else
                CanvasPainter.drawLine(canvas, thinAxisPaint, x, paneTop, x, paneTop + thinAxisHeight);
            //画底部时间轴
            if (isBigStride) {
                CanvasPainter.drawLine(canvas, boldAxisPaint, x, paneBottom - boldAxisHeight, x, paneBottom);
                CanvasPainter.drawTextFromBottomCenter(canvas, strideTimePaint, time, x, paneBottom - boldAxisHeight);
            } else
                CanvasPainter.drawLine(canvas, thinAxisPaint, x, paneBottom - thinAxisHeight, x, paneBottom);
            //下个时间轴
            axisTime = axisTime + strideTime;
        }

        //画当前时间轴
        canvas.drawLine(width / 2, paneTop - timeAxisExtraHeight, width / 2, paneBottom + timeAxisExtraHeight, timeAxisPaint);

        //画当前时间
        String mainTime = Times.formatDate(t2 * 1000L, presetedTimeFormat);
        CanvasPainter.drawTextFromBottomCenter(canvas, mainTimePaint, mainTime, width / 2, paneTop - timeAxisExtraHeight);

        //画备注
        int padding = Dimens.toPx(10);
        int size = (int) CanvasPainter.getTextHeight(strideTimePaint);
        double y1 = paneBottom + timeAxisExtraHeight * 2;
        double y2 = y1 + size;
        double x1 = padding;
        double x2 = x1 + size + padding / 2D + (int) CanvasPainter.getTextWidth(strideTimePaint, "有录像") + padding;
        CanvasPainter.drawColorRect(canvas, x1, y1, x1 + size, y2, videoDataSliderColor);
        CanvasPainter.drawTextFromLeftTop(canvas, strideTimePaint, "有录像", x1 + size + padding / 2, y1);
        CanvasPainter.drawColorRect(canvas, x2, y1, x2 + size, y2, R.color.color_20_grey);
        CanvasPainter.drawTextFromLeftTop(canvas, strideTimePaint, "无录像", x2 + size + padding / 2, y1);

        //惯性滑动
        if (isInertiaSliding) {
            //速度太慢则停止惯性滑动
            if (Maths.abs(v) < 0.1) {
                isInertiaSliding = false;
                v = 0;
                notifyProgressChange();
            }
            //否则逐渐减速进行滑动
            else {
                long now = Times.millisOfNow();
                double dx = (now - lastT) * v;
                double tTemp = t2 - strideTime * dx / strideWidth;
                if (tTemp < startTime) {
                    isInertiaSliding = false;
                    v = 0;
                    updateProgress(startTime, false);
                    notifyProgressChange();
                } else if (tTemp > endTime) {
                    isInertiaSliding = false;
                    v = 0;
                    updateProgress(endTime, false);
                    notifyProgressChange();
                } else {
                    v = v * 0.1D;
                    lastT = now;
                    updateProgress(tTemp, false);
                }
            }
        }

        //自动向有视频的位置滑动
        if (isTargetSliding) {
            long now = Times.millisOfNow();
            double dx = (now - lastT) * v;
            double tTemp = t2 + strideTime * dx / strideWidth;
            if (tTemp >= targetT) {
                isTargetSliding = false;
                updateProgress(targetT, false);
            } else {
                updateProgress(tTemp, false);
                v = v * 1.3D;
                lastT = now;
            }
        }

        //一直刷新
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        invalidateTimeSpan();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //控件被锁定，不处理手势
        if (isLocking) return true;
        //自动滑动中，不处理触摸事件
        if (isInertiaSliding || isTargetSliding) return true;
        //获取事件信息
        int action = event.getAction();
        double x = event.getX();
        double y = event.getY();
        //按下开始拖拽
        if (action == MotionEvent.ACTION_DOWN) {
            isDragging = true;
            lastX = x;
            lastY = y;
            lastT = Times.millisOfNow();
        }
        //不在拖拽过程中，不处理触摸事件
        if (!isDragging)
            return true;
        //触摸到外部区域，结束拖拽
        boolean out = x < 0 || x > width || y < paneTop || y > paneBottom;
        if (out) {
            isDragging = false;
            autoScroll();
            return true;
        }
        //释放或移出，结束拖拽
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            isDragging = false;
            autoScroll();
            return true;
        }
        //移动时拖拽面板
        if (action == MotionEvent.ACTION_MOVE) {
            //计算当前时间
            double t2Temp = t2 - strideTime * 1.5 * (x - lastX) / strideWidth;
            //计算滑动速度
            long now = Times.millisOfNow();
            v = (x - lastX) / (now - lastT);
            //记录最新参数
            lastX = x;
            lastT = now;
            //超出播放范围
            if (t2Temp < startTime) {
                updateProgress(startTime, false);
                TipBox.tipInCenter("已到达视频最顶端");
                return true;
            }
            if (t2Temp > endTime) {
                updateProgress(endTime, false);
                TipBox.tipInCenter("已到达视频最尾端");
                return true;
            }
            //更新进度
            updateProgress(t2Temp, false);
            return true;
        }
        return true;
    }

    //快速滑动松手时，自动滑动一段距离
    protected void autoScroll() {
        //无视频，不处理
        if (spanList.isEmpty())
            return;
        //滑动速度过快，自动惯性滑动
        if (Maths.abs(v) > 10) {
            isInertiaSliding = true;
            return;
        } else
            notifyProgressChange();
    }

    //通知进度改变
    protected void notifyProgressChange() {
        if (onProgressChange != null)
            onProgressChange.onProgressChange(Types.intValue(t2));
    }

    //判断当前时间是否有视频
    protected boolean isTimeAvailable(Number t) {
        for (TimeSpan span : spanList)
            if (t.doubleValue() >= span.start && t.doubleValue() <= span.end)
                return true;
        return false;
    }

    //获取下个最近的有效时间
    protected int getNextAvailableTime(Number t) {
        for (TimeSpan span : spanList)
            if (span.start >= t.doubleValue())
                return span.start;
        return t.intValue();
    }

    //重新计算显示的时间范围
    protected void invalidateTimeSpan() {
        if (width == 0 || height == 0) return;
        //计算初始显示的时间范围
        if (spanList.isEmpty()) {
            int now = (int) (Times.millisOfNow() / 1000);
            startTime = now;
            endTime = now;
            inferStrideTime();
            t2 = now;
            t1 = t2 - strideTime * width / 2D / strideWidth;
            t3 = t2 + strideTime * width / 2D / strideWidth;
        } else {
            startTime = Collections.getFirst(spanList).start;
            endTime = Collections.getLast(spanList).end + 1;
            inferStrideTime();
            t2 = startTime;
            t1 = t2 - strideTime * width / 2D / strideWidth;
            t3 = t2 + strideTime * width / 2D / strideWidth;
        }
        //计算回放面板绘制范围
        paneTop = (int) CanvasPainter.getTextHeight(mainTimePaint) + timeAxisExtraHeight;
        paneBottom = height - timeAxisExtraHeight * 2 - (int) CanvasPainter.getTextHeight(strideTimePaint);
        invalidate();
    }

    //计算每个格子的时长，格子数量，格子宽度
    protected void inferStrideTime() {
        //使用预设的
        if (presetedStrideTime != null) {
            strideTime = presetedStrideTime;
            strideWidth = presetedStrideWidth;
            strideCount = presetedStrideCount;
            return;
        }
        //自动推算
        double interval = endTime - startTime;
        if (interval > 10 * 60 * 60) {
            strideTime = 1 * 60 * 60;
            strideCount = 3;
            strideWidth = Dimens.toPx(40);
        } else if (interval > 2 * 60 * 60) {
            strideTime = 30 * 60;
            strideCount = 2;
            strideWidth = Dimens.toPx(60);
        } else if (interval > 1 * 60 * 60) {
            strideTime = 10 * 60;
            strideCount = 6;
            strideWidth = Dimens.toPx(20);
        } else if (interval > 10 * 60) {
            strideTime = 1 * 60;
            strideCount = 6;
            strideWidth = Dimens.toPx(20);
        } else if (interval > 1 * 60) {
            strideTime = 10;
            strideCount = 6;
            strideWidth = Dimens.toPx(20);
        } else {
            strideTime = 1;
            strideCount = 5;
            strideWidth = Dimens.toPx(24);
        }
    }

    // ********************************************** ControlAPI **********************************************

    //设置时间段
    //需要自己保证TimeSpan非空，并且按顺序排列
    public PlaybackView setTimeSpanList(List<TimeSpan> spans) {
        spanList.clear();
        if (spans != null)
            spanList.addAll(spans);
        invalidateTimeSpan();
        return this;
    }

    //获取时间段列表
    public List<TimeSpan> getTimeSpanList() {
        return spanList;
    }

    //查找指定时间所在时间段
    public TimeSpan findTimeSpan(int t) {
        for (TimeSpan span : spanList)
            if (t >= span.start)
                if (t <= span.end)
                    return span;
        return null;
    }

    //正在滑动中
    public boolean isSliding() {
        return isDragging || isInertiaSliding || isTargetSliding;
    }

    //更新时间
    public void updateProgress(Number time, boolean byMediaPlayer) {
        //滑动过程中，不允许播放器控制进度
        if (byMediaPlayer)
            if (isSliding())
                return;
        //更新进度
        t2 = time.doubleValue();
        t1 = t2 - strideTime * width / 2D / strideWidth;
        t3 = t2 + strideTime * width / 2D / strideWidth;
    }

    //滑动定位到新时间
    public void scrollTo(Number time, boolean byMediaPlayer) {
        //无视频，不处理
        if (spanList.isEmpty())
            return;
        //滑动过程中，不允许再滚动
        if (isSliding())
            return;
        //开始自动滑动
        isTargetSliding = true;
        targetT = getNextAvailableTime(t2);
        v = 1;
        lastT = Times.millisOfNow();
    }

    //预设格子尺寸，单位为dp、second
    public void presetStrideValues(Integer width, Integer time, Integer count) {
        presetedStrideWidth = Dimens.toPx(width);
        presetedStrideTime = time;
        presetedStrideCount = count;
    }

    //预设主时间格式
    public void setMainTimeFormat(String format) {
        presetedTimeFormat = format;
    }

    //锁定控件，不允许操作
    public void lockUI(boolean isLocking) {
        this.isLocking = isLocking;
    }

    //锁定控件，不允许操作
    public void lockUI(long ms) {
        isLocking = true;
        MainThread.postLater(() -> {
            isLocking = false;
        }, ms);
    }

    // ********************************************** Callback **********************************************

    //设置进度回调
    public void onProgressChange(OnProgressChange onProgressChange) {
        this.onProgressChange = onProgressChange;
    }
}
