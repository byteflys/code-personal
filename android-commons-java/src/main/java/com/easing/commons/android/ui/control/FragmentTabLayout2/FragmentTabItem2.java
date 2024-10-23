package com.easing.commons.android.ui.control.FragmentTabLayout2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.easing.commons.android.R;
import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonFragment;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.drawable.Bitmaps;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

//TabLayout子元素
@SuppressWarnings("all")
public class FragmentTabItem2 extends View {

    String title;
    Drawable selectedIcon;
    Drawable unselectedIcon;
    Bitmap selectedBitmap;
    Bitmap unselectedBitmap;

    Class<? extends CommonFragment> fragmentClazz;
    CommonFragment fragment;

    boolean selected = false;

    String badgeText = null;
    boolean showBadge = true;

    Paint textPaint;
    Paint circlePaint;

    int textSize = Dimens.toPx(8);
    int circleRadius = Dimens.toPx(10);

    public FragmentTabItem2(Context context) {
        this(context, null);
    }

    public FragmentTabItem2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        //解析属性
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.FragmentTabItem2);
        String clazzName = attributes.getString(R.styleable.FragmentTabItem2_context);
        fragmentClazz = Reflection.findClass(clazzName);
        title = attributes.getString(R.styleable.FragmentTabItem2_title);
        selectedIcon = attributes.getDrawable(R.styleable.FragmentTabItem2_selectedIcon);
        unselectedIcon = attributes.getDrawable(R.styleable.FragmentTabItem2_unselectedIcon);
        if (selectedIcon == null) Bitmaps.getDrawable(R.drawable.icon_001007_music);
        if (unselectedIcon == null) unselectedIcon = Bitmaps.getDrawable(R.drawable.icon_001007_music);
        selectedBitmap = Bitmaps.drawableToBitmap(selectedIcon);
        unselectedBitmap = Bitmaps.drawableToBitmap(unselectedIcon);

        //创建画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Colors.WHITE);
        textPaint.setTextSize(textSize);
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Colors.RED_90);
    }

    //初始化Fragment
    protected void initFragment() {
        FragmentTabLayout2 tabLayout = (FragmentTabLayout2) getParent();
        //创建子节点对应的Fragment
        if (fragmentClazz != null && tabLayout.fragmentContainerViewId != 0) {
            fragment = Reflection.newInstance(fragmentClazz);
            CommonActivity activity = (CommonActivity) getContext();
            FragmentManager manager = activity.getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(tabLayout.fragmentContainerViewId, fragment, fragmentClazz.getName());
            transaction.hide(fragment);
            transaction.commit();
        }
        //点击事件
        Views.onClick(this, () -> {
            tabLayout.select(this);
        });
    }

    protected void drawBadge(Canvas canvas, double top, double right) {
        if (showBadge && !Texts.isEmpty(badgeText)) {
            double cx = right;
            double cy = top;
            CanvasPainter.drawCircle(canvas, circlePaint, cx, cy, circleRadius);
            CanvasPainter.drawTextFromCenter(canvas, textPaint, badgeText, cx, cy);
        }
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    public void showBadge(boolean show) {
        this.showBadge = show;
        post(() -> {
            FragmentTabLayout2 tabLayout = (FragmentTabLayout2) getParent();
            tabLayout.invalidate();
        });
    }

    public void setBadgeText(Object badgeText) {
        this.badgeText = Texts.toString(badgeText);
        post(() -> {
            FragmentTabLayout2 tabLayout = (FragmentTabLayout2) getParent();
            tabLayout.invalidate();
        });
    }
}


