package com.easing.commons.android.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easing.commons.android.R;
import com.easing.commons.android.data.Storable;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.event.OnEvent;
import com.easing.commons.android.format.Maths;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.helper.thread.ComponentState;
import com.easing.commons.android.manager.Device;
import com.easing.commons.android.thread.Handlers;
import com.easing.commons.android.value.identity.Codes;
import com.easing.commons.android.view.Views;

import butterknife.ButterKnife;

@SuppressWarnings("all")
public abstract class CommonFragment extends Fragment implements Storable {

    public CommonActivity ctx;
    public Handler handler;
    protected View root;

    //记录Fragment状态
    public final ComponentState componentState = ComponentState.create();

    protected Integer requestCodeForFilePick;

    int resumeTimes = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        componentState.value = ComponentState.StateValue.ALIVE;
        ctx = (CommonActivity) getActivity();
        handler = ctx.handler;
        if (root != null) {
            ViewGroup parent = (ViewGroup) root.getParent();
            if (parent != null)
                parent.removeView(root);
        } else {
            root = inflateView();
            ButterKnife.bind(this, root);
            try {
                createView();
            } catch (Throwable e) {
                CommonApplication.ctx.handleGlobalException(e);
            }
        }
        //注册EventBus
        EventBus.core.subscribe(this);
        return root;
    }

    @Override
    public void onDestroy() {
        componentState.value = ComponentState.StateValue.DISPOSED;
        EventBus.core.unsubscribe(this);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeTimes++;
    }

    //判断是否首次进入界面
    public boolean isFirstLauch() {
        return resumeTimes <= 1;
    }

    public abstract View inflateView();

    public abstract void createView();

    //显示状态栏占位View
    public void adaptStatuBar() {
        View statuPlaceholder = root.findViewById(R.id.status_bar);
        if (statuPlaceholder != null) {
            int statuBarHeight = Device.statuBarHeight(ctx);
            Views.size(statuPlaceholder, null, statuBarHeight);
        }
    }

    //在主线程执行
    public void post(Action r) {
        Handlers.post(handler, r);
    }

    //在主线程延时执行
    public void postLater(Action r, long ms) {
        Handlers.postLater(handler, r, ms);
    }

    //设置点击事件
    public void onClick(@IdRes int viewId, Views.OnClick listener) {
        View view = root.findViewById(viewId);
        Views.onClick(view, listener);
    }

    //设置点击事件
    public void onClick(View view, Views.OnClick listener) {
        Views.onClick(view, listener);
    }

    //选取文件
    public void pickFile(String mediaType) {
        requestCodeForFilePick = Codes.randomCode();
        ctx.pickFile(requestCodeForFilePick, mediaType);
    }

    @OnEvent(type = "onFileSelect")
    public void onFileSelect(String type, Integer requestCode, String path) {
        if (Maths.equal(requestCode, requestCodeForFilePick))
            onFilePick(path);
    }

    @OnEvent(type = "onQRCaptureResult")
    public void onQRCaptureResult(String type, int requestCode, Intent intent) {

        onQRCaptureResult(requestCode, intent);
    }

    //选取文件成功
    public void onFilePick(String path) {
    }

    //多个Fragment切换时，被选中
    public void onSelected() {
    }

    //多个Fragment切换时，取消选中
    public void onDeselected() {
    }

    protected void onQRCaptureResult(int requestCode, Intent intent) {

    }
}
