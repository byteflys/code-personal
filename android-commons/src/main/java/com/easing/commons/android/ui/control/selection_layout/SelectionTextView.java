package com.easing.commons.android.ui.control.selection_layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.easing.commons.android.format.Texts;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Paints;
import com.easing.commons.android.view.Views;

@SuppressWarnings("all")
public class SelectionTextView extends AppCompatTextView {

    boolean showBadge = false;

    String badgeText = null;

    Paint textPaint;
    Paint circlePaint;

    int padding = Dimens.toPx(5);
    int textSize = Dimens.toPx(10);
    int circleRadius = Dimens.toPx(10);

    public SelectionTextView(Context context) {
        this(context, null);
    }

    public SelectionTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Colors.WHITE);
        textPaint.setTextSize(textSize);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Colors.RED_70);

        Views.onClick(this, () -> {
            ISelectionLayout selectionLayout = (ISelectionLayout) getParent();
            selectionLayout.selectView(this);
        });
    }

    public void showBadge(boolean show) {
        this.showBadge = show;
        invalidate();
    }

    public void setBadgeText(Object badgeText) {
        this.badgeText = Texts.toString(badgeText);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //不显示角标
        if (!showBadge || Texts.isEmpty(badgeText))
            return;

        //绘制角标圆圈
        float cx = getMeasuredWidth() - padding - circleRadius;
        float cy = padding + circleRadius;
        canvas.drawCircle(cx, cy, circleRadius, circlePaint);

        //绘制角标文字
        float fontHeight = Paints.fontHeight(textPaint);
        float distanceFromBaselineToMiddle = Paints.distanceFromBaselineToMiddle(textPaint);
        float textWidth = textPaint.measureText(badgeText);
        float x = cx - textWidth / 2;
        float y = cy + distanceFromBaselineToMiddle;
        canvas.drawText(badgeText, x, y, textPaint);
    }
}

