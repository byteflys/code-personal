package com.easing.commons.android.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.RawRes;


import com.easing.commons.android.R;
import com.easing.commons.android.R2;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonDialog extends Dialog {

    private Context context;
    private View view;

    @BindView(R2.id.cancel_ll)
    LinearLayout cancelLl;
    @BindView(R2.id.ok_ll)
    LinearLayout okLl;

    @BindView(R2.id.title_tv)
    TextView titleTv;
    @BindView(R2.id.cancel_tv)
    TextView cancelTv;

    @BindView(R2.id.ok_tv)
    TextView ok_tv;

    public CommonDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        view = View.inflate(context, R.layout.common_dialog_layout, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initEvent();
        setCancelable(false);
        //点击其他区域dialog消失
        //setCanceledOnTouchOutside(true);
    }

    public CommonDialog(@NonNull Context context, View view) {
        super(context, R.style.MyDialog);
        this.context = context;
        //view = View.inflate(context, R.layout.common_dialog_layout, null);
        this.view = view;
        setContentView(view);
        ButterKnife.bind(this, view);
        initEvent();
        setCancelable(false);
        //点击其他区域dialog消失
        //setCanceledOnTouchOutside(true);
    }

    public CommonDialog(@NonNull Context context, @LayoutRes int layout) {
        super(context, R.style.MyDialog);
        this.context = context;
        view = View.inflate(context, layout, null);
        setContentView(view);
        ButterKnife.bind(this, view);
        initEvent();
        setCancelable(false);
        //点击其他区域dialog消失
        //setCanceledOnTouchOutside(true);
    }

    public View getRootView() {
        return view;
    }

    public View getView(@IdRes int id) {
        return view.findViewById(id);
    }

    private void initEvent() {
        cancelLl.setOnClickListener(v -> {
            if (cancelListener != null) {
                cancelListener.cancelOnclick(cancelLl);
            }
            dismiss();
        });
        okLl.setOnClickListener(v -> {
            if (okListener != null) {
                okListener.OkOnclick(cancelLl);
            }
            dismiss();
        });
    }

    public void dissmiss() {
        dismiss();
    }

    public CommonDialog setCancelText(String text) {
        cancelTv.setText(text);
        return this;
    }

    public CommonDialog setOkText(String text) {
        ok_tv.setText(text);
        return this;
    }

    public CommonDialog setTitle(String title) {
        titleTv.setText(title);
        return this;
    }

    public static CommonDialog create(Context context) {
        return new CommonDialog(context);
    }

    private OkListener okListener;

    public CommonDialog setOkOnclickListener(OkListener okListener) {
        if (okListener != null)
            this.okListener = okListener;
        return this;
    }

    public interface OkListener {
        void OkOnclick(View view);
    }

    private CancelListener cancelListener;

    public CommonDialog setCancelOnclickListener(CancelListener cancelListener) {
        if (cancelListener != null)
            this.cancelListener = cancelListener;
        return this;
    }

    public interface CancelListener {
        void cancelOnclick(View view);
    }

}















