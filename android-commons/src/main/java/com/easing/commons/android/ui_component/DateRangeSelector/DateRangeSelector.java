package com.easing.commons.android.ui_component.DateRangeSelector;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.control.picker.DatePicker;
import com.easing.commons.android.ui.dialog.BottomSlideDialog;
import com.easing.commons.android.view.Views;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间日期选择
 */
@SuppressWarnings("all")
public class DateRangeSelector {

    CommonActivity activity;

    BottomSlideDialog dialog;

    TextView startDateText;
    TextView endDateText;
    ViewGroup startDateLayout;
    ViewGroup endDateLayout;

    String requestCode;

    String startDate, startTime = null;
    String endDate, endTime = null;

    View selectedView = null;

    public DateRangeSelector(CommonActivity activity) {

        this.activity = activity;
        this.dialog = BottomSlideDialog.create(activity, R.layout.layout_date_range_selector);
    }

    protected void init() {

        //已初始化
        if (startDateText != null)
            return;

        //解析控件
        startDateText = dialog.findView(R.id.startDateText);
        endDateText = dialog.findView(R.id.endDateText);
        startDateLayout = dialog.findView(R.id.startDateLayout);
        endDateLayout = dialog.findView(R.id.endDateLayout);

        //不限
        View noLimitButton = dialog.findView(R.id.noLimitButton);
        Views.onClick(noLimitButton, () -> {
            select(noLimitButton);
            startDate = null;
            startTime = null;
            endDate = null;
            endTime = null;
            showDate();
        });
        noLimitButton.performClick();

        //今天
        View thisDayButton = dialog.findView(R.id.thisDayButton);
        Views.onClick(thisDayButton, () -> {
            select(thisDayButton);
            Date date = Times.date();
            startDate = Times.formatDate(date, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            endDate = Times.formatDate(date, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //昨天
        View lastDayButton = dialog.findView(R.id.lastDayButton);
        Views.onClick(lastDayButton, () -> {
            select(lastDayButton);
            Date date = Times.date();
            date = Times.subDay(date);
            startDate = Times.formatDate(date, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            endDate = Times.formatDate(date, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //本周
        View thisWeekButton = dialog.findView(R.id.thisWeekButton);
        Views.onClick(thisWeekButton, () -> {
            select(thisWeekButton);
            Calendar calendar = Times.getCalendar();
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.DATE, 6);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //上周
        View lastWeekButton = dialog.findView(R.id.lastWeekButton);
        Views.onClick(lastWeekButton, () -> {
            select(lastWeekButton);
            Calendar calendar = Times.getCalendar();
            calendar.add(Calendar.DATE, -7);
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                calendar.add(Calendar.DATE, -1);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.DATE, 6);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //本月
        View thisMonthButton = dialog.findView(R.id.thisMonthButton);
        Views.onClick(thisMonthButton, () -> {
            select(thisMonthButton);
            Calendar calendar = Times.getCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //上月
        View lastMonthButton = dialog.findView(R.id.lastMonthButton);
        Views.onClick(lastMonthButton, () -> {
            select(lastMonthButton);
            Calendar calendar = Times.getCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.add(Calendar.MONTH, -1);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.MONTH, 1);
            calendar.add(Calendar.DATE, -1);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //本季度
        View thisQuarterButton = dialog.findView(R.id.thisQuarterButton);
        Views.onClick(thisQuarterButton, () -> {
            select(thisQuarterButton);
            Calendar calendar = Times.getCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int quarter = calendar.get(Calendar.MONTH) / 3;
            calendar.set(Calendar.MONTH, quarter * 3);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.MONTH, 3);
            calendar.add(Calendar.DATE, -1);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //上季度
        View lastQuarterButton = dialog.findView(R.id.lastQuarterButton);
        Views.onClick(lastQuarterButton, () -> {
            select(lastQuarterButton);
            Calendar calendar = Times.getCalendar();
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            int quarter = calendar.get(Calendar.MONTH) / 3;
            calendar.set(Calendar.MONTH, quarter * 3);
            calendar.add(Calendar.MONTH, -3);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.MONTH, 3);
            calendar.add(Calendar.DATE, -1);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //今年
        View thisYearButton = dialog.findView(R.id.thisYearButton);
        Views.onClick(thisYearButton, () -> {
            select(thisYearButton);
            Calendar calendar = Times.getCalendar();
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.DATE, -1);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //去年
        View lastYearButton = dialog.findView(R.id.lastYearButton);
        Views.onClick(lastYearButton, () -> {
            select(lastYearButton);
            Calendar calendar = Times.getCalendar();
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            calendar.add(Calendar.YEAR, -1);
            startDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            startTime = startDate + " 00:00:00";
            calendar.add(Calendar.YEAR, 1);
            calendar.add(Calendar.DATE, -1);
            endDate = Times.formatCalendar(calendar, Times.FORMAT_06);
            endTime = endDate + " 23:59:59";
            showDate();
        });

        //自定义
        View customButton = dialog.findView(R.id.customButton);
        Views.onClick(customButton, () -> {
            select(customButton);
        });

        //选择开始时间
        Views.onClick(startDateLayout, () -> {
            DatePicker.pick(activity, date -> {
                startDate = Times.formatDate(date, Times.FORMAT_06);
                startTime = Times.formatDate(date, Times.FORMAT_01);
                startTime = startDate + " 00:00:00";
                showDate();
                select(customButton);
            }, R.style.EASING_DIALOG_PINK_GREEN);
        });

        //选择结束时间
        Views.onClick(endDateLayout, () -> {
            DatePicker.pick(activity, date -> {
                endDate = Times.formatDate(date, Times.FORMAT_06);
                endTime = endDate + " 23:59:59";
                showDate();
                select(customButton);
            }, R.style.EASING_DIALOG_PINK_GREEN);
        });

        //重置
        View resetButton = dialog.findView(R.id.resetButton);
        Views.onClick(resetButton, () -> {
            noLimitButton.performClick();
        });

        //确定
        View okButton = dialog.findView(R.id.okButton);
        Views.onClick(okButton, () -> {
            EventBus.core.emit("onDateRangeSelected", requestCode, startDate, endDate);
            EventBus.core.emit("onDateRangeSelectedTime", requestCode, startTime, endTime); //带时分秒的
            dialog.close();
        });
    }

    protected void select(View button) {
        if (selectedView != null)
            selectedView.setSelected(false);
        button.setSelected(true);
        selectedView = button;
    }

    protected void showDate() {
        startDateText.setText(startDate);
        endDateText.setText(endDate);
    }

    public void select(String requestCode) {
        init();
        this.requestCode = requestCode;
        dialog.show();
    }
}
