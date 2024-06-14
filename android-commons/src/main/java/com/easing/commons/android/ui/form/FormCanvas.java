package com.easing.commons.android.ui.form;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.easing.commons.android.manager.Dimens;

//工具类，用于绘制FormItem
public class FormCanvas {

    private static final Paint paint = new Paint();

    private static final int lineColor = 0x22111111;
    private static final float lineWidth = Dimens.toPx(1);

    static {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
    }

    public static void drawBorder(View view, Canvas canvas, int[] itemLocation, int[] parentLocation) {

        //获取边框位置
        Rect rect = new Rect();
        view.getDrawingRect(rect);

        //每个格子只画左边框和上边框
        //只有最右边的格子画右边框，最下边的格子画下边框
        canvas.drawLine(rect.left + lineWidth / 2, rect.top + lineWidth / 2, rect.left + lineWidth / 2, rect.bottom, paint);
        canvas.drawLine(rect.left + lineWidth / 2, rect.top + lineWidth / 2, rect.right - lineWidth / 2, rect.top + lineWidth / 2, paint);
        if (itemLocation[1] == parentLocation[1])
            canvas.drawLine(rect.right - lineWidth / 2, rect.top + lineWidth / 2, rect.right - lineWidth / 2, rect.bottom, paint);
        if (itemLocation[3] == parentLocation[3])
            canvas.drawLine(rect.left + lineWidth / 2, rect.bottom - lineWidth / 2, rect.right - lineWidth / 2, rect.bottom - lineWidth / 2, paint);
    }
}

