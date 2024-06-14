package com.easing.commons.android.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.value.measure.Size;
import com.easing.commons.android.view.Views;

//弹出任意布局
public class LayoutDialogEx2 extends DialogFragment {

    CommonActivity ctx;
    View root;

    Action onDialogShow;
    Action onDialogClose;

    //静态创建方法
    public static LayoutDialogEx2 create(CommonActivity ctx) {
        LayoutDialogEx2 dialog = new LayoutDialogEx2();
        dialog.setCancelable(false);
        dialog.ctx = ctx;
        return dialog;
    }

    //设置打开回调
    public LayoutDialogEx2 onDialogShow(Action onDialogShow) {
        this.onDialogShow = onDialogShow;
        return this;
    }

    //设置关闭回调
    public LayoutDialogEx2 onDialogClose(Action onDialogClose) {
        this.onDialogClose = onDialogClose;
        return this;
    }

    //设置是否可以取消
    public LayoutDialogEx2 cancelable(boolean cancelable) {
        setCancelable(cancelable);
        return this;
    }

    //设置布局
    public LayoutDialogEx2 layout(int layoutId) {
        this.root = Views.inflate(ctx, layoutId);
        return this;
    }

    //显示
    public LayoutDialogEx2 show() {
        FragmentManager manager = ctx.getSupportFragmentManager();
        show(manager, Texts.random());
        return this;
    }

    //销毁
    public void dispose() {
        ctx.postLater(super::dismiss, 500);
        ctx = null;
        root = null;
    }

    //立刻销毁
    public void disposeImmediately() {
        ctx.post(super::dismiss);
        ctx = null;
        root = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        //创建CardView作为根节点
        CardView cardView = new CardView(ctx);
        cardView.setRadius(Dimens.toPx(3));
        cardView.setCardElevation(Dimens.toPx(25));
        cardView.setCardBackgroundColor(Colors.TRANSPARENT);
        cardView.addView(root);
        builder.setView(cardView);
        //创建Dialog
        AlertDialog dialog = builder.create();
        //设置监听
        dialog.setOnShowListener(dialog1 -> {
            if (onDialogShow != null)
                onDialogShow.runAndPostException();
        });
        dialog.setOnDismissListener(dialog2 -> {
            if (onDialogClose != null)
                onDialogClose.runAndPostException();
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        Size screenSize = Device.getScreenSize(ctx);
        int w = (int) (screenSize.w * 0.9);
        getDialog().getWindow().setLayout(w, Views.WRAP_CONTENT);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.color_transparent);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) root.getLayoutParams();
        layoutParams.width = w;
        layoutParams.height = Views.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        root.setLayoutParams(layoutParams);
    }

    //解析子控件
    public <T extends View> T findView(int viewId) {
        return (T) root.findViewById(viewId);
    }
}
