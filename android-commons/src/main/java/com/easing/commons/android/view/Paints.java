package com.easing.commons.android.view;

import android.graphics.Paint;

public class Paints {

    public static float fontHeight(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float height = metrics.descent - metrics.ascent;
        return height;
    }

    public static float distanceFromBaselineToMiddle(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        float distance = (metrics.descent - metrics.ascent) / 2 - metrics.descent;
        return distance;
    }
}

