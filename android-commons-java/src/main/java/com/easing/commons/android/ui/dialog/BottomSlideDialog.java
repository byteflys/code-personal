package com.easing.commons.android.ui.dialog;

import android.view.View;
import android.widget.FrameLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.view.Views;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

/**
 * 下面的Dialog
 */
@SuppressWarnings("all")
public class BottomSlideDialog {

    CommonActivity ctx;
    Integer layoutId;

    CoordinatorLayout rootView;
    FrameLayout contentView;

    BottomSheetDialogEx dialog;
    BottomSheetBehavior behavior;

    Action onDialogCreate;
    Action onDialogClose;

    boolean fullscreen = false;

    public static BottomSlideDialog create(CommonActivity ctx, int layoutId) {
        BottomSlideDialog dialog = new BottomSlideDialog();
        dialog.ctx = ctx;
        dialog.layoutId = layoutId;
        return dialog;
    }

    private BottomSlideDialog() {
    }

    //初始化
    private void init() {
        //解析View
        rootView = Views.inflate(ctx, R.layout.md001_dialog_bottom_slide);
        contentView = rootView.findViewById(R.id.content);
        Views.inflateAndAttach(ctx, layoutId, contentView, true);
        //创建Dialog
        dialog = new BottomSheetDialogEx(ctx);
        dialog.setContentView(rootView);
        behavior = BottomSheetBehavior.from((View) contentView.getParent());
        behavior.setPeekHeight(Device.screenHeight());
        dialog.getWindow().findViewById(R.id.touch_outside).setBackground(null);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackground(null);
        if (onDialogCreate != null)
            onDialogCreate.runAndPostException();
        dialog.setOnShowListener(dlg -> {
            if (fullscreen) {
                View sheetView = dialog.getWindow().findViewById(R.id.design_bottom_sheet);
                sheetView.layout(0, 0, sheetView.getMeasuredWidth(), sheetView.getMeasuredHeight());
            }
        });
        dialog.setOnDismissListener(dlg -> {
            if (onDialogClose != null)
                onDialogClose.runAndPostException();
        });
    }

    public BottomSlideDialog onDialogCreate(Action onDialogCreate) {
        this.onDialogCreate = onDialogCreate;
        return this;
    }

    public BottomSlideDialog onDialogClose(Action onDialogClose) {
        this.onDialogClose = onDialogClose;
        return this;
    }

    public <T extends View> T findView(int viewId) {
        if (dialog == null)
            init();
        return contentView.findViewById(viewId);
    }

    public BottomSlideDialog onClick(int viewId, Views.OnClick listener) {
        if (dialog == null)
            init();
        View view = findView(viewId);
        Views.onClick(view, listener);
        return this;
    }

    public View contentView() {
        if (dialog == null)
            init();
        return contentView;
    }

    public BottomSlideDialog show() {
        if (dialog == null)
            init();
        fullscreen = false;
        dialog.show();
        return this;
    }

    public BottomSlideDialog showFullscreen() {
        if (dialog
                == null)
            init();
        fullscreen = true;
        dialog.show();
        return this;
    }

    public BottomSlideDialog close() {
        if (dialog == null)
            init();
        dialog.dismiss();
        return this;
    }

    public boolean isOpen() {
        if (dialog == null)
            return false;
        return dialog.isShowing();
    }

}
