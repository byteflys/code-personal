package com.easing.commons.android.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.Data;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.helper.callback.DataCallback;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.bean.ViewXy;
import com.easing.commons.android.ui.listener.TextChangeListener;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.value.measure.Pos;
import com.easing.commons.android.value.measure.Rect;
import com.easing.commons.android.value.measure.Size;

import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;

@SuppressWarnings("all")
public class Views {

    public static final int MATCH_PARENT = LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = LayoutParams.WRAP_CONTENT;

    public static final int VISIBLE = View.VISIBLE;
    public static final int INVISIBLE = View.INVISIBLE;
    public static final int GONE = View.GONE;

    //解析Layout，并返回被解析出来的View
    public static <T extends View> T inflateAndAttach(Context context, int layoutId, ViewGroup parent, boolean attach) {
        //attach为true时，inflate返回的是root
        //attach为false时，inflate返回的是inflate出来的view
        //attach为false时，仍然需要传入parent，这个parent是用来创建LayoutParam的，如果不传入，layout将丢失xml中的LayoutParam信息
        View inflatedView = LayoutInflater.from(context).inflate(layoutId, parent, parent == null ? false : attach);
        //未绑定parent，直接返回解析出的layout
        if (!attach)
            return (T) inflatedView;
        //如果绑定了parent，则获取parent的最后节点
        ViewGroup viewGroup = (ViewGroup) inflatedView;
        int childCount = viewGroup.getChildCount();
        return (T) viewGroup.getChildAt(childCount - 1);
    }

    //解析Layout
    public static <T extends View> T inflate(Context context, int layoutId) {
        return (T) inflateAndAttach(context, layoutId, null, false);
    }

    //解析Layout
    public static <T extends View> T inflate(int layoutId) {
        return (T) inflateAndAttach(CommonApplication.ctx, layoutId, null, false);
    }

    //解析View
    public static <T extends View> T findView(View v, int vid) {
        return (T) v.findViewById(vid);
    }

    //获取控件绑定的数据
    public static <T> T getTag(View v) {
        return (T) v.getTag();
    }

    //获取控件相当于屏幕的位置
    public static Pos positionInScreen(View v) {
        int[] pos = new int[2];
        v.getLocationOnScreen(pos);
        return new Pos(pos[0], pos[1]);
    }

    //获取控件相当于屏幕的位置
    public static Pos positionInWindow(View v) {
        int[] pos = new int[2];
        v.getLocationInWindow(pos);
        return new Pos(pos[0], pos[1]);
    }

    //获取控件相对于父级的位置
    public static Pos positionInParent(View v) {
        int x = v.getLeft();
        int y = v.getTop();
        return new Pos(x, y);
    }

    //设置控件内边距
    public static void padding(View v, int top, int bottom, int left, int right) {
        v.setPadding(left, top, right, bottom);
    }

    //设置控件外边距
    public static void margin(View v, Integer top, Integer bottom, Integer left, Integer right) {
        v.post(() -> {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            if (top != null) lp.topMargin = top;
            if (bottom != null) lp.bottomMargin = bottom;
            if (left != null) lp.leftMargin = left;
            if (right != null) lp.rightMargin = right;
            v.setLayoutParams(lp);
        });
    }

    //获取屏幕大小
    @Deprecated
    public static Size getScreenSize(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return new Size(dm.widthPixels, dm.heightPixels);
    }

    //获取控件实际宽度
    public static int width(View v) {
        return v.getMeasuredWidth();
    }

    //获取控件实际高度
    public static int height(View v) {
        return v.getMeasuredHeight();
    }

    //获取控件实际高度
    public static int dpHeight(View v) {
        int pxHeight = v.getMeasuredHeight();
        int dpHeight = Dimens.toDp(pxHeight);
        return dpHeight;
    }

    //获取控件实际大小
    public static Size size(View v) {
        int w = v.getMeasuredWidth();
        int h = v.getMeasuredHeight();
        return new Size(w, h);
    }

    //设置控件背景
    public static void setBackground(View v, int drawableId) {
        Drawable drawable = v.getContext().getDrawable(drawableId);
        v.setBackground(drawable);
    }

    //设置控件大小
    public static void sizeInDp(View v, Number dpW, Number dpH) {
        int w = Dimens.toPx(dpW.intValue());
        int h = Dimens.toPx(dpH.intValue());
        size(v, w, h);
    }

    //设置控件大小
    public static void size(View v, Number w, Number h) {
        LayoutParams params = v.getLayoutParams();
        if (params == null)
            params = new LayoutParams(0, 0);
        if (w != null)
            params.width = w.intValue();
        if (h != null)
            params.height = h.intValue();
        v.setLayoutParams(params);
    }

    //控件铺满父容器
    public static void fillParent(View view) {
        size(view, Views.MATCH_PARENT, Views.MATCH_PARENT);
    }

    //添加并铺满父容器
    public static void attachAndFill(ViewGroup parent, View view) {
        parent.addView(view, new LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
    }

    //添加并铺满父容器
    public static void attachWithFullscreenSize(ViewGroup parent, View view) {
        Size size = Device.phoneSize();
        parent.addView(view, new LayoutParams(size.w, size.h));
    }

    //添加到父容器，并设置布局
    public static void attachAndLayout(ViewGroup parent, View view, LayoutParams layoutParams) {
        parent.addView(view);
        view.setLayoutParams(layoutParams);
    }

    //设置线性布局中的控件大小
    public static void size(View v, int w, int h, float weight) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, h, weight);
        v.setLayoutParams(params);
    }

    //获取Activity的根节点View
    public static ViewGroup getRootView(Activity activity) {
        return activity.findViewById(android.R.id.content);
    }

    //获取控件文本
    public static String text(TextView tv) {
        String text = tv.getText().toString();
        return text;
    }

    //获取控件文本，当文本为空时，返回null
    public static String textWithNull(TextView tv) {
        String text = tv.getText().toString();
        if (text.isEmpty()) return null;
        return text;
    }

    //获取控件中的小数值
    public static Double doubleValue(TextView tv) {
        String text = tv.getText().toString().trim();
        if (text.isEmpty()) return null;
        return Double.valueOf(text);
    }

    //获取控件中的小数值
    public static Integer intValue(TextView tv) {
        String text = tv.getText().toString().trim();
        if (text.isEmpty()) return null;
        return Integer.valueOf(text);
    }

    //获取控件中的布尔值
    public static Boolean boolValue(TextView tv) {
        String text = tv.getText().toString().trim();
        if (Texts.equal(text, "是")) return true;
        if (Texts.equal(text, "否")) return false;
        return null;
    }

    //获取控件文本
    public static String text(Spinner spinner) {
        String text = spinner.getSelectedItem().toString();
        return text;
    }

    //获取控件文本
    public static String text(TextView tv, boolean trim, boolean ignoreCase) {
        String text = tv.getText().toString();
        if (trim)
            text = text.trim();
        if (ignoreCase)
            text = text.toLowerCase();
        return text;
    }

    //获取控件中的整数
    public static Integer getInt(TextView textView, Integer defaultValue) {
        String text = textView.getText().toString().trim().toLowerCase();
        if (text.isEmpty())
            return defaultValue;
        else
            return Integer.valueOf(text);
    }

    //获取控件中的小数
    public static Double getDouble(TextView textView, Double defaultValue) {
        String text = textView.getText().toString().trim().toLowerCase();
        if (text.isEmpty())
            return defaultValue;
        else
            return Double.valueOf(text);
    }

    //获取控件中的布尔值
    public static Boolean getBool(TextView textView, Boolean defaultValue) {
        String text = textView.getText().toString().trim().toLowerCase();
        if (text.isEmpty())
            return defaultValue;
        else
            return Boolean.valueOf(text);
    }

    //移除编辑框默认的双击事件
    public static void removeDoubleClickEvent(EditText et) {
        et.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
            }
        });
    }

    //设置控件文本
    public static void text(CommonActivity activity, @IdRes int viewId, Object data) {
        MainThread.post(() -> {
            TextView view = activity.findViewById(viewId);
            if (data == null)
                view.setText("");
            else
                view.setText(data.toString());
        });
    }

    //设置控件文本
    //这个方法有一个缺陷，由于操作是交给Handler去执行的，并不会立刻生效，所以不能在执行完这个方法就立刻去getText
    public static void text(TextView v, Object data) {
        MainThread.post(() -> {
            if (data == null)
                v.setText("");
            else
                v.setText(data.toString());
        });
    }

    //设置控件文本
    public static void text(TextView v, boolean shouldSet, Object data) {
        if (!shouldSet)
            return;
        MainThread.post(() -> {
            if (data == null)
                v.setText("");
            else
                v.setText(data.toString());
        });
    }

    //设置控件文本，没有时显示默认值
    public static void text(TextView v, Object data, String nullValue) {
        MainThread.post(() -> {
            if (data == null || data.toString().isEmpty())
                v.setText(nullValue);
            else
                v.setText(data.toString());
        });
    }

    //设置控件图标
    public static void src(ImageView imageView, @DrawableRes int drawableId) {
        MainThread.post(() -> {
            imageView.setImageResource(drawableId);
        });
    }

    //判断控件内容是否为空
    public static boolean isEmpty(View v) {
        TextView textView = (TextView) v;
        return textView.getText().toString().trim().isEmpty();
    }

    //判断控件内容是否为空
    public static boolean notEmpty(View v) {
        TextView textView = (TextView) v;
        return !textView.getText().toString().trim().isEmpty();
    }

    //平移控件
    public static void translate(View v, int dx, int dy) {
        v.offsetLeftAndRight(dx);
        v.offsetTopAndBottom(dy);
    }

    //获取子控件
    public static LinkedList<View> allWindowNode(Activity ctx) {
        LinkedList<View> list = allViewNode(ctx.getWindow().getDecorView(), true);
        return list;
    }

    //获取子控件
    public static LinkedList<View> allWindowNode(Window window) {
        LinkedList<View> list = allViewNode(window.getDecorView(), true);
        return list;
    }

    //获取子控件
    public static LinkedList<View> allViewNode(View view, boolean recursive) {
        LinkedList<View> children = new LinkedList();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                children.add(child);
                if (recursive)
                    children.addAll(allViewNode(child, recursive));
            }
        }
        return children;
    }

    //禁用所有子控件
    public static void disableAll(View parent) {
        LinkedList<View> children = Views.allViewNode(parent, true);
        for (View child : children)
            child.setEnabled(false);
    }

    //禁用或启用控件
    public static void enable(View view, boolean enabled) {
        MainThread.post(() -> {
            view.setEnabled(enabled);
        });
    }

    //设置文本颜色
    public static void textColor(TextView v, int resourceId) {
        int color = Colors.getColor(v.getContext(), resourceId);
        v.setTextColor(color);
    }

    //选中下拉框
    public static Spinner select(Spinner spinner, int position) {
        spinner.setSelection(position);
        return spinner;
    }

    //选中下拉框
    public static Spinner select(Spinner spinner, String position) {
        spinner.setSelection(Integer.valueOf(position));
        return spinner;
    }

    //选中下拉框
    public static Spinner select(Spinner spinner, List<String> options, String value) {
        spinner.setSelection(options.indexOf(value));
        return spinner;
    }

    //改变控件的选中状态，返回最终的选中状态
    public static boolean toggleSelection(View view) {
        boolean selected = view.isSelected();
        view.setSelected(!selected);
        return !selected;
    }

    //通过一个控件，控制另一个控件的可见性
    public static void bindVisibilityController(View target, View controller) {
        controller.setOnClickListener(v -> {
            if (target.getVisibility() == View.VISIBLE)
                target.setVisibility(View.GONE);
            else
                target.setVisibility(View.VISIBLE);
        });
    }

    //将ClearButton与TextView绑定，清空TextView中的文本
    public static void bindClearButton(TextView textView, View clearButton, Action onClear) {
        clearButton.setVisibility(View.INVISIBLE);
        textView.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                boolean empty = s.length() == 0;
                clearButton.setVisibility(empty ? View.INVISIBLE : View.VISIBLE);
            }
        });
        onClick(clearButton, () -> {
            textView.setText("");
            onClear.runAndPostException();
        });
    }

    //获取控件相当于屏幕的坐标位置
    //六个参数分别为：x1, x2, y1, y2, width, height
    public static int[] location(View v) {
        int[] location = new int[6];
        int[] temp = new int[2];
        v.getLocationOnScreen(temp);
        location[0] = temp[0];
        location[1] = temp[0] + v.getMeasuredWidth();
        location[2] = temp[1];
        location[3] = temp[1] + v.getMeasuredHeight();
        location[4] = v.getMeasuredWidth();
        location[5] = v.getMeasuredHeight();
        return location;
    }

    //获取控件在屏幕的矩形位置
    public static Rect rect(View v) {
        if (v.getMeasuredWidth() == 0 && v.getMeasuredHeight() == 0)
            return null;
        int[] location = Views.location(v);
        Rect rect = new Rect();
        rect.width = v.getMeasuredWidth();
        rect.height = v.getMeasuredHeight();
        rect.left = location[0];
        rect.top = location[1];
        rect.right = location[0] + rect.width;
        rect.bottom = location[1] + rect.height;
        return rect;
    }


    //为控件设置触摸事件
    public static void onTouch(View view, OnTouch listener) {
        view.setOnTouchListener((v, event) -> {
            if (listener == null)
                return true;
            try {
                return listener.onTouch(event);
            } catch (Throwable e) {
                CommonApplication.ctx.handleGlobalException(e);
                return true;
            }
        });
    }


    //文字粗体
    public static void setTypeface(TextView parent, boolean fakeBoldText) {

        parent.setTypeface(Typeface.defaultFromStyle(fakeBoldText ? Typeface.BOLD : Typeface.NORMAL));

    }

    //为控件设置点击事件
    public static void onClick(View parent, int viewId, OnClick listener) {
        View view = parent.findViewById(viewId);
        onClick(view, listener);
    }

    //为控件设置点击事件
    public static void onClick(View view, OnClick listener) {
        if (view == null || listener == null)
            return;
        view.setOnClickListener(v -> {
            try {
                listener.onClick();
            } catch (Throwable e) {
                CommonApplication.ctx.handleGlobalException(e);
            }
        });
    }

    //为控件设置点击事件
    public static void onClicks(OnClicks listener, View... views) {
        if (views == null)
            return;
        for (View view : views) {
            view.setOnClickListener(v -> {
                if (listener == null) return;
                try {
                    listener.onClick(view);
                } catch (Throwable e) {
                    CommonApplication.ctx.handleGlobalException(e);
                }
            });
        }
    }

    //为控件设置长按事件
    public static void onLongClick(View parent, int viewId, OnLongClick listener) {
        View view = parent.findViewById(viewId);
        onLongClick(view, listener);
    }

    public static void onLongClicks(OnLongClick listener, View... views) {
        if (views == null)
            return;
        for (View view : views) {
            view.setOnLongClickListener(v -> {
                if (listener == null)
                    return true;
                try {
                    listener.onLongClick();
                } catch (Throwable e) {
                    CommonApplication.ctx.handleGlobalException(e);
                }
                return true;
            });
        }
    }

    //为控件设置长按事件
    public static void onLongClick(View view, OnLongClick listener) {
        view.setOnLongClickListener(v -> {
            if (listener == null)
                return true;
            try {
                listener.onLongClick();
            } catch (Throwable e) {
                CommonApplication.ctx.handleGlobalException(e);
            }
            return true;
        });
    }

    //限制控件连续点击
    public static void setOnClickListenerWithIntervalLimit(View view, View.OnClickListener listener, long interval) {
        final Data<Long> lastClickTime = Data.create(Times.millisOfNow());
        view.setOnClickListener(v -> {
            try {
                long time = Times.millisOfNow();
                if (time - lastClickTime.data < interval) {
//                    TipBox.tip("操作频繁");
                    return;
                }
                lastClickTime.set(time);
                if (listener != null)
                    listener.onClick(view);
            } catch (Throwable e) {
                CommonApplication.ctx.handleGlobalException(e);
            }
        });
    }


    private static final int MIN_CLICK_DELAY_TIME = 500;
    private static long sLastClickTime = 0;

    /**
     * 通过两次点击毫秒值间隔判断是否不可以点击
     * 防重复点击 双击
     */
    public static boolean isFastClick() {
        return isFastClick(MIN_CLICK_DELAY_TIME);
    }

    public static boolean isFastClick(int gapTime) {
        long currentTime = System.currentTimeMillis();
        boolean isFastClick = currentTime - sLastClickTime < gapTime;
        sLastClickTime = currentTime;
        return isFastClick;
    }

    //等View绑定窗口后再提交任务，绑定窗口前控件的宽高都为0，无法正确处理某些界面计算任务
    public static void postAfterViewAttach(View view, Action r) {
        view.post(() -> {
            r.runAndPostException();
        });
    }

    //自动绑定控件
    public static void viewBinding(Object to, Activity from) {
        try {
            ButterKnife.bind(to, from);
        } catch (Throwable e) {
            Console.error(e);
        }
    }

    //自动绑定控件
    public static void viewBinding(Object to, View from) {
        try {
            ButterKnife.bind(to, from);
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    //自动绑定控件
    public static void viewBinding(Object to, Dialog from) {
        ButterKnife.bind(to, from);
    }

    //禁止所有子控件的点击事件
    public static void forbidClickForAllChild(ViewGroup parent) {
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            view.setClickable(false);
        }
    }

    //控制控件可见性
    public static void visibility(CommonActivity activity, @IdRes int viewId, int visibility) {
        MainThread.post(() -> {
            View view = activity.findViewById(viewId);
            view.setVisibility(visibility);
        });
    }

    //控制控件可见性
    public static void visibility(View view, int visibility) {
        MainThread.post(() -> {
            view.setVisibility(visibility);
        });
    }

    //设置控件可见
    public static void visible(View view) {
        MainThread.post(() -> {
            view.setVisibility(View.VISIBLE);
        });
    }

    //设置控件不可见，但占据布局控件
    public static void invisible(View view) {
        MainThread.post(() -> {
            view.setVisibility(View.INVISIBLE);
        });
    }

    //设置控件不可见
    public static void gone(View view) {
        MainThread.post(() -> {
            view.setVisibility(View.GONE);
        });
    }

    //从父控件移除
    public static LayoutParams detachFromParent(View view) {
        LayoutParams layoutParams = view.getLayoutParams();
        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);
        return layoutParams;
    }

    //设置输入法类型
    public static void inputType(EditText editText, InputType inputType) {
        if (inputType == InputType.NUMBER)
            editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        if (inputType == InputType.FLOAT)
            editText.setInputType(android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (inputType == InputType.PHONE)
            editText.setInputType(android.text.InputType.TYPE_CLASS_PHONE);
        if (inputType == InputType.PASSWORD) {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            inputLimit(editText, "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ~!@#$%^&*()_+`-=");
        }
        if (inputType == InputType.EMAIL)
            editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        if (inputType == InputType.URI)
            editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_URI);
    }

    //限制输入字符
    public static void inputLimit(EditText editText, String expression) {
        editText.setKeyListener(DigitsKeyListener.getInstance(expression));
    }

    //监听文字数量变化
    public static void listenTextLength(TextView textView, DataCallback<Integer> callback) {
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                callback.onDataChange(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //为ImageView设置颜色叠加
    public static void tintColor(ImageView imageView, @ColorRes int colorId) {
        imageView.setImageTintList(
                ColorStateList.valueOf(
                        CommonApplication.ctx.getResources().getColor(colorId, null)
                )
        );
    }

    //ImageView移除颜色叠加
    public static void removeTintColor(ImageView imageView) {
        imageView.setImageTintList(null);
    }

    //创建空白的占位View
    public static View createEmptyView(Activity context, Integer dpWidth, Integer dpHeight) {
        View view = new View(context);
        size(view, Dimens.toPx(dpWidth), Dimens.toPx(dpHeight));
        return view;
    }

    //焦点移到编辑框最后
    public static void focusAtEnd(EditText editText) {
        editText.requestFocus();
        editText.setSelection(editText.getText().length());
    }

    //取消焦点
    public static void clearFocus(EditText editText) {
        editText.clearFocus();
    }

    //控件中心定位到(x,y)位置
    public static void locateInCenter(View view, int x, int y) {
        int w = view.getMeasuredWidth();
        int h = view.getMeasuredHeight();
        view.layout(x - w / 2, y - h / 2, x + w / 2, y + h / 2);
    }

    //通过控制边距，让控件中心定位到(x,y)位置
    public static void locateInCenterByMargin(View view, int x, int y) {
        ViewGroup parent = (ViewGroup) view.getParent();
        if (x + view.getMeasuredWidth() / 2 > parent.getMeasuredWidth())
            x = parent.getMeasuredWidth() - view.getMeasuredWidth() / 2;
        if (y + view.getMeasuredHeight() / 2 > parent.getMeasuredHeight())
            y = parent.getMeasuredHeight() - view.getMeasuredHeight() / 2;
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        lp.leftMargin = x - view.getMeasuredWidth() / 2;
        lp.topMargin = y - view.getMeasuredHeight() / 2;
        view.setLayoutParams(lp);
    }


    /**
     * 密码输入框的文字显示与隐藏
     */
    public static void isShowEditViewPassword(@Nullable EditText passwordEdit, boolean isShow) {
        passwordEdit.post(new Runnable() {
            @Override
            public void run() {
                passwordEdit.setInputType(isShow ? android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD : android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);

            }
        });
    }

    /**
     * 判断点击的位置是否在范围内
     */
    public static boolean isPointListInPoint(ViewXy poing, List<ViewXy> pointList) {
        int iSum = 0;
        int iCount = pointList.size();
        if (iCount < 3) {
            return false;
        }
        //  待判断的点(x, y) 为已知值
        float y = poing.getY();
        float x = poing.getX();
        for (int i = 0; i < iCount; i++) {
            float y1 = pointList.get(i).getY();
            float x1 = pointList.get(i).getX();

            float y2 = 0;
            float x2 = 0;
            if (i == iCount - 1) {
                y2 = pointList.get(0).getY();
                x2 = pointList.get(0).getX();
            } else {
                y2 = pointList.get(i + 1).getY();
                x2 = pointList.get(i + 1).getX();
            }
            // 当前边的 2 个端点分别为 已知值(x1, y1), (x2, y2)
            if (((y >= y1) && (y < y2)) || ((y >= y2) && (y < y1))) {
                //  y 界于 y1 和 y2 之间
                //  假设过待判断点(x, y)的水平直线和当前边的交点为(x_intersect, y_intersect)，有y_intersect = y
                // 则有（2个相似三角形，公用顶角，宽/宽 = 高/高）：|x1 - x2| / |x1 - x_intersect| = |y1 - y2| / |y1 - y|
                if (Math.abs(y1 - y2) > 0) {
                    float x_intersect = x1 - ((x1 - x2) * (y1 - y)) / (y1 - y2);
                    if (x_intersect < x) {
                        iSum += 1;
                    }
                }
            }
        }
        if (iSum % 2 != 0) {
            return true;
        } else {
            return false;
        }
    }

    public interface OnTouch {

        boolean onTouch(MotionEvent event);
    }

    public interface OnClick {

        void onClick();
    }

    public interface OnClicks {

        void onClick(View view);
    }

    public interface OnClickData<T> {

        void onClick(View view, T data);
    }

    public interface OnLongClick {

        void onLongClick();
    }
}
