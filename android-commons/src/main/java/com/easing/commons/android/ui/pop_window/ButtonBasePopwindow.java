package com.easing.commons.android.ui.pop_window;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.LayoutRes;

import com.easing.commons.android.R;
import com.easing.commons.android.util.PopWindowUtil;
import com.easing.commons.android.view.Views;

/**
 * 底部popPopwindow
 */
public abstract class ButtonBasePopwindow extends PopupWindow {

    private Context mContext;
    private View view;
    protected View root_view;

    //  @BindView(R2.id.title_tv)
    public TextView title_tv;

    //  @BindView(R2.id.root_layout)
    protected FrameLayout root_layout;

    protected LinearLayout button_layout;


    // @BindView(R2.id.confirm_ll)
    public View confirm_ll;


    // @BindView(R2.id.cancel_ll)
    public View cancel_ll;

    public PopwindowCallback callback;

    public ButtonBasePopwindow(Context context) {
        super(context);
        this.mContext = context;
        initPopupWindow();
        view = View.inflate(mContext, inflateView(), null);

        title_tv = view.findViewById(R.id.title_tv);
        root_layout = view.findViewById(R.id.root_layout);
        button_layout = view.findViewById(R.id.button_layout);
        confirm_ll = view.findViewById(R.id.confirm_ll);
        cancel_ll = view.findViewById(R.id.cancel_ll);
        //Views.viewBinding(this, view);
        if (inflateRootView() != 0) {
            root_view = View.inflate(mContext, inflateRootView(), null);
            root_layout.removeAllViews();
            root_layout.addView(root_view);
            Views.viewBinding(this, root_view);
        }

        initEvent();
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view);

    }

    public abstract @LayoutRes
    int inflateRootView();


    protected @LayoutRes
    int inflateView() {
        return R.layout.button_base_popwindow;
    }


    protected void initEvent() {

        cancel_ll.setOnClickListener(view1 -> {

            dismiss();
        });
      /*  confirm_ll.setOnClickListener(view1 -> {

            dismiss();
        });*/

    }


    //初始化popwindow
    private void initPopupWindow() {
        setAnimationStyle(R.style.animation);//设置动画
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(false);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        backgroundAlpha(1f);
    }

    /**
     * 显示popupWindow
     * Gravity.BOTTOM;
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            this.showAtLocation(parent, Gravity.BOTTOM, 0, PopWindowUtil.getNavigationBarHeight(mContext));
            backgroundAlpha(0.5f);
        } else {
            this.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        if (mContext != null) {
            WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
            lp.alpha = bgAlpha; //0.0-1.0
            ((Activity) mContext).getWindow().setAttributes(lp);
        }
    }

    /**
     * 关闭popupWindow
     */
    private void closePopWindow() {
        dismiss();
        backgroundAlpha(1f);
    }


}














