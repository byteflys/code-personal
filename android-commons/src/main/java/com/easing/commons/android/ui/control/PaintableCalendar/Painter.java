package com.easing.commons.android.ui.control.PaintableCalendar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.easing.commons.android.R;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.value.time.YMD;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@SuppressWarnings("all")
public class Painter {

    protected Paint paint = new Paint();

    protected int titleColor = Colors.getColor(R.color.color_black_90);
    protected int dateColor = Colors.getColor(R.color.color_black_70);
    protected int disabledColor = Colors.getColor(R.color.color_black_40);
    protected int selectedColor = Colors.getColor(R.color.color_white);
    protected int selectedBackgroundColor = R.color.color_light_blue;
    protected int todayColor = Colors.getColor(R.color.color_red);

    protected int titleFontSize = Dimens.toPx(12);
    protected int dateFontSize = Dimens.toPx(10);
    protected int todayFontSize = Dimens.toPx(12);

    public Painter() {
        paint.setAntiAlias(true);
    }

    //获取初始时间
    protected Calendar getInitialDate() {
        return Times.getCalendar();
    }

    //时间转标题
    protected String toTitle(Calendar date) {
        return new SimpleDateFormat("yyyy年MM月").format(date.getTime());
    }

    //Item被选中
    protected void onSelect(PaintableCalendarItem item) {

    }

    //Item被选中
    protected void onMonth(Calendar item) {

    }

    //绘制Item
    protected void drawItem(PaintableCalendarItem item, Canvas canvas) {

        //标题
        if (item.isTitle) {
            paint.setColor(titleColor);
            paint.setTextSize(titleFontSize);
            paint.setTypeface(Typeface.DEFAULT);
            CanvasPainter.drawTextFromCenter(canvas, paint, item.getText().toString(), item.w / 2, item.h / 2);
            return;
        }

        //禁用
        if (!item.isEnabled) {
            paint.setColor(disabledColor);
            paint.setTextSize(dateFontSize);
            paint.setTypeface(Typeface.DEFAULT);
            CanvasPainter.drawTextFromCenter(canvas, paint, item.getText().toString(), item.w / 2, item.h / 2);
            return;
        }

        //选中
        if (item.isSelected) {
            paint.setColor(selectedColor);
            paint.setTextSize(dateFontSize);
            paint.setTypeface(Typeface.DEFAULT);
            CanvasPainter.drawColorRect(canvas, 0, 0, item.w, item.h, selectedBackgroundColor);
            CanvasPainter.drawTextFromCenter(canvas, paint, item.getText().toString(), item.w / 2, item.h / 2);
            return;
        }

        //今天时间格
        YMD nowYmd = Times.getYmd();
        if (YMD.equal(item.ymd, nowYmd)) {
            paint.setColor(todayColor);
            paint.setTextSize(todayFontSize);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            CanvasPainter.drawTextFromCenter(canvas, paint, item.getText().toString(), item.w / 2, item.h / 2);
            return;
        }

        //普通时间格
        paint.setColor(dateColor);
        paint.setTextSize(dateFontSize);
        paint.setTypeface(Typeface.DEFAULT);
        CanvasPainter.drawTextFromCenter(canvas, paint, item.getText().toString(), item.w / 2, item.h / 2);
    }
}

