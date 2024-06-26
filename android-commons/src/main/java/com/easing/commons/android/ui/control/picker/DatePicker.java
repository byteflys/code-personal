package com.easing.commons.android.ui.control.picker;

import android.app.DatePickerDialog;
import android.content.Context;

import androidx.annotation.StyleRes;

import com.easing.commons.android.time.Times;
import com.easing.commons.android.value.time.YMD;

import java.util.Date;

public class DatePicker {

    public static void pick(Context ctx, Callback callback, @StyleRes int theme) {
        YMD ymd = Times.getYmd();
        new DatePickerDialog(ctx, theme, (p, y, m, d) -> {
            Date date = Times.getDate(y, m + 1, d);
            if (callback != null)
                callback.onDateSelect(date);
        }, ymd.year, ymd.month - 1, ymd.day).show();
    }

    //使用系统默认样式
    public static void pick(Context ctx, Callback callback) {
        YMD ymd = Times.getYmd();
        new DatePickerDialog(ctx, (p, y, m, d) -> {
            Date date = Times.getDate(y, m + 1, d);
            if (callback != null)
                callback.onDateSelect(date);
        }, ymd.year, ymd.month - 1, ymd.day).show();
    }

    public interface Callback {
        void onDateSelect(Date date);
    }
}
