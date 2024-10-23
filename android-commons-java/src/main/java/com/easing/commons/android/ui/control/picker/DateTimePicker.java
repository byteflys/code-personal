package com.easing.commons.android.ui.control.picker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.easing.commons.android.time.Times;

import java.util.Calendar;
import java.util.Date;

/**
 * 时间选择弹框
 */
public class DateTimePicker implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Context ctx;
    Callback callback;
    TextView anchor;

    Calendar calendar;

    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    //选择日期时间，并显示到指定控件上
    public static DateTimePicker show(Context ctx, TextView anchor) {
        DateTimePicker picker = new DateTimePicker();
        picker.ctx = ctx;
        picker.anchor = anchor;
        picker.calendar = Times.getCalendar();
        picker.datePicker = new DatePickerDialog(picker.ctx, picker, picker.calendar.get(Calendar.YEAR), picker.calendar.get(Calendar.MONTH), picker.calendar.get(Calendar.DAY_OF_MONTH));
        picker.datePicker.show();
        return picker;
    }

    //选择日期时间，并执行回调
    public static void pick(Context ctx, Callback callback) {
        DateTimePicker picker = new DateTimePicker();
        picker.ctx = ctx;
        picker.callback = callback;
        picker.calendar = Times.getCalendar();
        picker.datePicker = new DatePickerDialog(picker.ctx, picker, picker.calendar.get(Calendar.YEAR), picker.calendar.get(Calendar.MONTH), picker.calendar.get(Calendar.DAY_OF_MONTH));
        picker.datePicker.show();
    }

    public static void pickTime(Context ctx, Callback callback) {
        DateTimePicker picker = new DateTimePicker();
        picker.ctx = ctx;
        picker.callback = callback;
        picker.calendar = Times.getCalendar();
        picker.showTimePicker();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year, month, day);
        showTimePicker();
    }

    public void showTimePicker() {
        timePicker = new TimePickerDialog(ctx, this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePicker.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Date date = Times.toDate(calendar);
        String format = Times.formatCalendar(calendar, Times.FORMAT_01);
        if (callback != null)
            callback.onDateSelect(date);
        if (anchor != null)
            anchor.setText(format);
    }

    public interface Callback {
        void onDateSelect(Date date);
    }
}
