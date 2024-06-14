package com.easing.commons.android.ui.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.easing.commons.android.R;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Resources;
import com.easing.commons.android.view.Views;
import com.easing.commons.android.value.measure.Pos;

//显示在指定控件的某侧
@SuppressWarnings("all")
public class SideDialog extends PopupWindow {

    Context ctx;
    View root;

    Integer width = null;
    Integer height = null;
    Integer background = null;

    boolean bottomWindowTouchable = false;

    Action onShow;
    Action onClose;

    public static SideDialog create(Context ctx, int layoutId) {
        SideDialog dialog = new SideDialog();
        dialog.ctx = ctx;
        dialog.root = Views.inflate(ctx, layoutId);
        dialog.setAnimationStyle(R.style.dialog_m03);
        dialog.setOnDismissListener(() -> {
            if (dialog.onClose != null)
                dialog.onClose.runAndPostException();
        });
        return dialog;
    }

    private SideDialog() {
    }

    public View rootView() {
        return root;
    }

    public <T extends View> T findView(int viewId) {
        return root.findViewById(viewId);
    }

    public SideDialog width(Integer width) {
        this.width = width;
        return this;
    }

    public SideDialog height(Integer height) {
        this.height = height;
        return this;
    }

    public SideDialog background(Integer background) {
        this.background = background;
        return this;
    }

    public SideDialog onClick(Integer viewId, Views.OnClick listener) {
        View view = rootView().findViewById(viewId);
        Views.onClick(view, listener);
        return this;
    }

    public SideDialog onShow(Action onShow) {
        this.onShow = onShow;
        return this;
    }

    public SideDialog onClose(Action onClose) {
        this.onClose = onClose;
        return this;
    }

    //显示在指定控件顶部位置
    public SideDialog showAtTop(View anchor) {
        setWidth(width == null ? Views.MATCH_PARENT : width);
        setHeight(height == null ? Views.WRAP_CONTENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.container_back_m19_top : background));
        setFocusable(true);
        setTouchModal(bottomWindowTouchable ? false : true);
        Pos pos = Views.positionInScreen(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y);
        width = null;
        height = null;
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //显示在指定控件底部位置
    public SideDialog showAtBottom(View anchor) {
        setWidth(width == null ? Views.MATCH_PARENT : width);
        setHeight(height == null ? Views.WRAP_CONTENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.container_back_m25_bottom : background));
        setFocusable(true);
        setInputMethodMode(INPUT_METHOD_NOT_NEEDED);
        setTouchModal(bottomWindowTouchable ? false : true);
        Pos pos = Views.positionInScreen(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y + anchor.getMeasuredHeight() - root.getMeasuredHeight());
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //显示在指定控件底部位置
    public SideDialog showAtWindowBottom(View anchor) {
        setWidth(width == null ? Views.MATCH_PARENT : width);
        setHeight(height == null ? Views.WRAP_CONTENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.container_back_m25_bottom : background));
        setFocusable(true);
        setInputMethodMode(INPUT_METHOD_NOT_NEEDED);
        setTouchModal(bottomWindowTouchable ? false : true);
        Pos pos = Views.positionInWindow(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y + anchor.getMeasuredHeight() - root.getMeasuredHeight());
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //显示在指定控件左边位置
    public SideDialog showAtLeft(View anchor) {
        setWidth(width == null ? Views.WRAP_CONTENT : width);
        setHeight(height == null ? Views.MATCH_PARENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.container_back_m19_left : background));
        setFocusable(true);
        setTouchModal(bottomWindowTouchable ? false : true);
        showAtLocation(anchor, Gravity.NO_GRAVITY, 0, 0);
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //显示在指定控件右边位置
    public SideDialog showAtRight(View anchor) {
        setWidth(width == null ? Views.WRAP_CONTENT : width);
        setHeight(height == null ? Views.MATCH_PARENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.container_back_m19_right : background));
        setFocusable(true);
        setTouchModal(bottomWindowTouchable ? false : true);
        Pos pos = Views.positionInScreen(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x + anchor.getMeasuredWidth() - root.getMeasuredWidth(), pos.y);
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //显示在指定控件中间位置
    public SideDialog showInMiddle(View anchor) {
        setWidth(width == null ? Views.MATCH_PARENT : width);
        setHeight(height == null ? Views.MATCH_PARENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.color_white_95 : background));
        Pos pos = Views.positionInScreen(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y);
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //显示在指定区域
    public SideDialog showInBound(View anchor) {
        setWidth(anchor.getMeasuredWidth());
        setHeight(anchor.getMeasuredHeight());
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.color_white_95 : background));
        setFocusable(true);
        setTouchModal(bottomWindowTouchable ? false : true);
        Pos pos = Views.positionInScreen(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y);
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //测试弹窗交互方式
    public SideDialog showTest(View anchor) {
        setWidth(width == null ? Views.MATCH_PARENT : width);
        setHeight(height == null ? Views.WRAP_CONTENT : height);
        setContentView(root);
        setBackgroundDrawable(Resources.drawable(background == null ? R.drawable.container_back_m25_bottom : background));
        //点击外部无操作，按下返回无操作
        setFocusable(false); //为true时，可以接收到返回事件
        setTouchModal(true); //为true时，外部不可点击，同时会拦截Activity的返回事件
        //点击外部取消弹窗，按下返回取消弹窗
        setFocusable(true);
        setTouchModal(true);
        //点击外部触发外部窗口事件，按下返回取消弹窗
        setFocusable(true);
        setTouchModal(false);
        //点击外部触发外部窗口事件，按下返回触发Activity结束事件
        setFocusable(false);
        setTouchModal(false);
        Pos pos = Views.positionInScreen(anchor);
        showAtLocation(anchor, Gravity.NO_GRAVITY, pos.x, pos.y + anchor.getMeasuredHeight() - root.getMeasuredHeight());
        if (onShow != null)
            onShow.runAndPostException();
        return this;
    }

    //设置底部窗口是否可触摸
    public SideDialog setWindowTouchable(boolean touchable) {
        this.bottomWindowTouchable = touchable;
        return this;
    }

    //关闭
    public void close() {
        dismiss();
    }
}
