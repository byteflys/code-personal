package com.easing.commons.android.ui.control.PaintableCalendar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.value.time.YMD;
import com.easing.commons.android.view.Views;

import java.util.Calendar;
import java.util.Date;

import lombok.Setter;

//可自己绘制的日历控件
@SuppressWarnings("all")
public class PaintableCalendar extends FrameLayout {

    Activity context;
    GridLayout gridLayout;

    static String[] WEEK_TEXTS = {"日", "一", "二", "三", "四", "五", "六"};
    int startWeek = 1;

    @Setter
    Calendar date = Calendar.getInstance();

    PaintableCalendarItem selectedItem;

    @Setter
    Painter painter = new Painter();

    TextView dateText;

    public PaintableCalendar(Context context) {
        this(context, null);
    }

    public PaintableCalendar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        this.context = (Activity) context;
        MainThread.post(this::addRootContent);
    }

    protected void addRootContent() {

        //创建弹窗
        View rootView = LayoutInflater.from(context).inflate(R.layout.md001_paintable_calendar, null);
        addView(rootView, new LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));

        //解析View
        gridLayout = rootView.findViewById(R.id.calendarLayout);
        dateText = rootView.findViewById(R.id.dateText);
        ImageView lastButton = rootView.findViewById(R.id.lastButton);
        ImageView nextButton = rootView.findViewById(R.id.nextButton);

        //显示初始时间
        dateText.setText(painter.toTitle(date));
        dateText.setText(painter.toTitle(date));

        //在CalendarLayout中创建空白Item
        createItems();

        //月份减小事件
        lastButton.setOnClickListener(v -> {
            if (selectedItem != null) {
                selectedItem.isSelected = false;
                selectedItem = null;
            }
            date.add(Calendar.MONTH, -1);
            dateText.setText(painter.toTitle(date));
            painter.onMonth(date);
            redrawItems(gridLayout);
            if(callback != null){
                callback.timeChange(date);
            }
        });

        //月份增加事件
        nextButton.setOnClickListener(v -> {
            if (selectedItem != null) {
                selectedItem.isSelected = false;
                selectedItem = null;
            }
            date.add(Calendar.MONTH, 1);
            dateText.setText(painter.toTitle(date));
            painter.onMonth(date);
            redrawItems(gridLayout);
            if(callback != null){
                callback.timeChange(date);
            }
        });

        //更新数据
        redrawItems(gridLayout);
    }

    //创建Items
    protected void createItems() {
        //添加标题栏
        for (int i = 0; i < 7; i++) {
            int week = startWeek + i;
            if (week >= 7)
                week = week - 7;
            PaintableCalendarItem item = createItem(0, i, true);
            item.setText(WEEK_TEXTS[week]);
        }
        //添加日期Item
        for (int i = 7; i < 49; i++)
            createItem(i / 7, i % 7, false);
    }

    //创建Item
    protected PaintableCalendarItem createItem(int row, int column, boolean isTitle) {
        //重写绘制方法
        //将绘制方法抽离成回调，通过Painter让用户自己定制
        PaintableCalendarItem item = new PaintableCalendarItem(context) {
            @Override
            protected void onDraw(Canvas canvas) {
                painter.drawItem(this, canvas);
            }
        };
        item.isTitle = isTitle;
        //标题禁用事件
        if (isTitle) {
            item.setEnabled(false);
            item.isEnabled = false;
        }
        //设置监听器
        item.setOnTouchListener((v, e) -> {
            if (e.getAction() == MotionEvent.ACTION_DOWN && item.isEnabled) {
                if (selectedItem != null) {
                    selectedItem.isSelected = false;
                    selectedItem.invalidate();
                    selectedItem = null;
                }
                item.isSelected = true;
                selectedItem = item;
                selectedItem.invalidate();
                //执行回调
                painter.onSelect(item);
            }
            return false;
        });
        //设置布局，添加到容器
        gridLayout.addView(item);
        GridLayout.LayoutParams lp = (GridLayout.LayoutParams) item.getLayoutParams();
        GridLayout.Spec rowSpec = GridLayout.spec(row, 1.0F);
        GridLayout.Spec columnSpec = GridLayout.spec(column, 1.0F);
        lp.rowSpec = rowSpec;
        lp.columnSpec = columnSpec;
        item.setLayoutParams(lp);
        return item;
    }

    protected void redrawItems(GridLayout calendarLayout) {
        Calendar tempDate = Times.getCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), 1);
        int currentMonth = date.get(Calendar.MONTH);
        //将tempDate设置到第一天的前一天
        int week = tempDate.get(Calendar.DAY_OF_WEEK) - 1;
        if (week > startWeek)
            tempDate.add(Calendar.DATE, -(week - startWeek + 1));
        else
            tempDate.add(Calendar.DATE, -(week + 7 - startWeek + 1));
        //逐个设置日期和状态
        selectedItem = null;
        for (int i = 7; i < 49; i++) {
            tempDate.add(Calendar.DATE, 1);
            YMD ymd = YMD.create(tempDate.get(Calendar.YEAR), tempDate.get(Calendar.MONTH) + 1, tempDate.get(Calendar.DATE));
            PaintableCalendarItem item = (PaintableCalendarItem) calendarLayout.getChildAt(i);
            item.isEnabled = (tempDate.get(Calendar.MONTH) == currentMonth);
            item.isSelected = false;
            item.ymd = ymd;
            item.setText(ymd.day + "");
        }
    }

    public void updateDate(Calendar calendar) {
        this.date = calendar;
        dateText.setText(painter.toTitle(date));
        painter.onMonth(date);
        redrawItems(gridLayout);
    }

    public void updateDate(String anyDate) {
        Date date = Times.parseDate(anyDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        updateDate(calendar);
    }

    private PaintableCalendarCallback callback;

    public void setPaintableCalendarCallback(PaintableCalendarCallback callback){
        this.callback = callback;
    }

    public interface PaintableCalendarCallback{
        void timeChange(Calendar calendar);
    }

}

