package com.easing.commons.android.ui.control.FragmentTabLayout2;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.easing.commons.android.R;
import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonFragment;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.optimize.CanvasPainter;
import com.easing.commons.android.value.color.Colors;

import java.util.LinkedList;
import java.util.List;

//Fragment切换布局
//这个选中时带有弧形隆起效果，和图标缩放效果
@SuppressWarnings("all")
public class FragmentTabLayout2 extends LinearLayout {

    //文字间隔
    double padding1;
    //图标间隔
    double padding2;

    //颜色背景
    int selectedTextColor;
    int unselectedTextColor;
    int borderColor;
    int backgroundColor;

    //是否画边框线
    double borderWidth;
    boolean drawBorder;

    //子节点
    List<FragmentTabItem2> childNodes = new LinkedList();

    //Fragment容器id
    int fragmentContainerViewId;

    //尺寸
    double width;
    double height;
    double itemWidth;
    double itemHeight;
    double textHeight;
    double iconSize;
    double textSize;

    //动画持续时间
    double animationDuration = 200;

    //画笔
    Paint textPaint;
    Paint imagePaint;
    Paint borderPaint;
    Paint roundPaint;
    Paint backgroundPaint;

    //选中
    FragmentTabItem2 selectedItem = null;
    long selectedTime = 0L;
    OnSelected listener;

    //布局加载完成回调
    Action onLayoutComplete;

    public FragmentTabLayout2(Context context) {
        this(context, null);
    }

    public FragmentTabLayout2(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    //初始化
    protected void init(Context context, AttributeSet attributeSet) {
        //解析属性
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.FragmentTabLayout2);
        textSize = attributes.getDimension(R.styleable.FragmentTabLayout2_textSize, Dimens.toPx(12));
        padding1 = attributes.getDimension(R.styleable.FragmentTabLayout2_padding1, Dimens.toPx(2));
        padding2 = attributes.getDimension(R.styleable.FragmentTabLayout2_padding2, Dimens.toPx(4));
        selectedTextColor = attributes.getColor(R.styleable.FragmentTabLayout2_selectedTextColor, Colors.LIGHT_GREY);
        unselectedTextColor = attributes.getColor(R.styleable.FragmentTabLayout2_unselectedTextColor, Colors.BLACK_90);
        borderColor = attributes.getColor(R.styleable.FragmentTabLayout2_borderColor, Colors.LIGHT_GREY);
        backgroundColor = attributes.getColor(R.styleable.FragmentTabLayout2_background, Colors.TRANSPARENT);
        borderWidth = attributes.getDimension(R.styleable.FragmentTabLayout2_borderWidth, Dimens.toPx(1F));
        drawBorder = attributes.getBoolean(R.styleable.FragmentTabLayout2_drawBorder, false);
        fragmentContainerViewId = attributes.getResourceId(R.styleable.FragmentTabLayout2_fragment, 0);
        //创建画笔
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setTextSize((float) textSize);
        imagePaint = new Paint();
        imagePaint.setAntiAlias(true);
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth((float) borderWidth);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
        backgroundPaint = new Paint();
        backgroundPaint.setAntiAlias(true);
        backgroundPaint.setColor(backgroundColor);
        roundPaint = new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setColor(Colors.RED);
        //设置布局
        setOrientation(LinearLayout.HORIZONTAL);
        //允许绘制
        setWillNotDraw(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //添加子节点
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            FragmentTabItem2 item = (FragmentTabItem2) getChildAt(i);
            item.initFragment();
            childNodes.add(item);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //通知布局加载完成
        if (onLayoutComplete != null)
            onLayoutComplete.runAndPostException();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        //不处理
        int childCount = getChildCount();
        if (childCount == 0) return;

        //计算尺寸
        width = right - left;
        height = bottom - top;
        itemWidth = width / childCount;
        itemHeight = height - padding2;
        textHeight = CanvasPainter.getTextHeight(textPaint);
        iconSize = itemHeight - padding1 * 2 - padding2 - textHeight;

        //刷新UI
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //判断是否在动画中
        long now = Times.millisOfNow();
        boolean inAnimation = now - selectedTime <= animationDuration;
        double enlarge = !inAnimation ? 1 : (now - selectedTime) / animationDuration;
        double animatedSize = iconSize + (1.5 * padding2 - borderPaint.getStrokeWidth()) * enlarge;

        //绘制背景
        CanvasPainter.drawRect(canvas, backgroundPaint, 0, padding2, width, height);

        //绘制横线
        if (drawBorder)
            CanvasPainter.drawLine(canvas, borderPaint, 0, padding2, width, padding2);

        //逐个Item绘制
        for (int i = 0; i < childNodes.size(); i++) {

            //计算每个格子的位置
            FragmentTabItem2 child = childNodes.get(i);
            double x1 = i * itemWidth;
            double x2 = (i + 1) * itemWidth;
            double xmid = (x1 + x2) / 2;
            double y1 = padding2;
            double y2 = height;

            //绘制圆
            if (child.selected) {
                double cx = xmid;
                double cy = y2 - padding1 * 2 - textHeight - animatedSize / 2;
                double radius = animatedSize / 2 + padding2 / 2;
                if (cy - radius < padding2)
                    CanvasPainter.drawCircle(canvas, backgroundPaint, cx, cy, radius);
            }

            //绘制弧线
            if (drawBorder && child.selected) {
                double cx = xmid;
                double cy = y2 - padding1 * 2 - textHeight - animatedSize / 2;
                double radius = animatedSize / 2 + padding2 / 2;
                if (cy - radius < padding2) {
                    double dy = cy - padding2;
                    double dx = Math.sqrt(radius * radius - dy * dy);
                    CanvasPainter.drawLine(canvas, borderPaint, x1, y1, cx - dx, y1);
                    CanvasPainter.drawLine(canvas, borderPaint, cx + dx, y1, x2, y1);
                    double degree = 180 * Math.asin(dx / radius) / Math.PI;
                    RectF ovalRect = CanvasPainter.rectF(cx - radius, cy - radius, cx + radius, cy + radius);
                    canvas.drawArc(ovalRect, -90 - (float) degree, (float) degree * 2, false, borderPaint);
                } else
                    CanvasPainter.drawLine(canvas, borderPaint, x1, y1, x2, y1);
            }

            //绘制文字
            textPaint.setColor(child.selected ? selectedTextColor : unselectedTextColor);
            CanvasPainter.drawTextFromBottomCenter(canvas, textPaint, child.title, xmid, y2 - padding1);

            //绘制动画图标
            if (child.selected) {
                Bitmap bitmap = child.selectedBitmap;
                Rect srcRect = CanvasPainter.rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect dstRect = CanvasPainter.rect(xmid - animatedSize / 2, y2 - padding1 * 2 - textHeight - animatedSize, xmid + animatedSize / 2, y2 - padding1 * 2 - textHeight);
                canvas.drawBitmap(bitmap, srcRect, dstRect, imagePaint);
            }

            //绘制普通图标
            if (!child.selected) {
                Bitmap bitmap = child.unselectedBitmap;
                Rect srcRect = CanvasPainter.rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect dstRect = CanvasPainter.rect(xmid - iconSize / 2, y1 + padding2, xmid + iconSize / 2, y2 - padding1 * 2 - textHeight);
                canvas.drawBitmap(bitmap, srcRect, dstRect, imagePaint);
            }

            //绘制角标
            child.drawBadge(canvas, y1 + padding2, xmid + iconSize / 2);
        }

        //如果再动画中，则继续绘制，形成动画效果
        if (inAnimation)
            invalidate();
    }

    //初始化所有Fragments
    public FragmentTabLayout2 fragments(@IdRes Integer containerViewId, Class<? extends CommonFragment>... fragmentClasses) {
        CommonActivity activity = (CommonActivity) getContext();
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            FragmentTabItem2 item = (FragmentTabItem2) getChildAt(i);
            item.fragmentClazz = fragmentClasses[i];
            item.fragment = Reflection.newInstance(fragmentClasses[i]);
            transaction.add(containerViewId, item.fragment, item.fragmentClazz.getName());
            transaction.hide(item.fragment);
        }
        transaction.commit();
        return this;
    }

    //按索引选取Fragment
    public FragmentTabLayout2 select(int index) {
        FragmentTabItem2 item = childNodes.get(index);

        //如果已选中，则刷新
        if (item == selectedItem) {
            selectedItem.fragment.onDeselected();
            selectedItem.fragment.onSelected();
            selectedTime = Times.millisOfNow();
            if (listener != null)
                listener.onItemSelected(index, item.fragment);
            invalidate();
            return this;
        }

        //取消选中旧的Fragment
        if (selectedItem != null) {
            selectedItem.selected = false;
            selectedItem.fragment.onDeselected();
        }

        //切换Fragment
        CommonActivity activity = (CommonActivity) getContext();
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (selectedItem != null)
            transaction.hide(selectedItem.fragment);
        transaction.show(item.fragment);
        transaction.commit();

        //选中当前Item
        selectedItem = item;
        selectedItem.selected = true;
        selectedItem.fragment.onSelected();
        selectedTime = Times.millisOfNow();

        //执行选中回调
        if (listener != null)
            listener.onItemSelected(index, item.fragment);

        //刷新UI
        invalidate();

        return this;
    }

    //按控件选取Fragment
    public FragmentTabLayout2 select(FragmentTabItem2 item) {
        int index = childNodes.indexOf(item);
        select(index);
        return this;
    }

    //按类名选取Fragment
    public FragmentTabLayout2 select(Class clazz) {
        int count = getChildCount();
        int index = -1;
        for (int i = 0; i < count; i++) {
            FragmentTabItem2 item = (FragmentTabItem2) getChildAt(i);
            if (item.fragmentClazz == clazz) index = i;
        }
        select(index);
        return this;
    }

    //按类名选取Fragment
    public FragmentTabLayout2 select(String className) {
        Class clazz = Reflection.findClass(className);
        select(clazz);
        return this;
    }

    //查找TabItem
    public FragmentTabItem2 item(Class clazz) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            FragmentTabItem2 item = (FragmentTabItem2) getChildAt(i);
            if (item.fragmentClazz == clazz) return item;
        }
        return null;
    }

    //查找TabItem
    public FragmentTabItem2 item(String className) {
        Class clazz = Reflection.findClass(className);
        return item(clazz);
    }

    //布局加载完成监听
    public FragmentTabLayout2 onLayoutComplete(Action onLayoutComplete) {
        this.onLayoutComplete = onLayoutComplete;
        return this;
    }

    //选中监听
    public interface OnSelected {
        void onItemSelected(int index, CommonFragment fragment);
    }
}

