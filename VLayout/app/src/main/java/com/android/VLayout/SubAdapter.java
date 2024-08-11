package com.android.VLayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.chad.library.adapter.base.BaseViewHolder;

public class SubAdapter extends DelegateAdapter.Adapter<BaseViewHolder> {

    Context context;
    LayoutHelper layoutHelper;
    int layoutResId;
    int count;
    int viewType;

    public SubAdapter(Context context, LayoutHelper layoutHelper, int layoutResId, int count, int viewType) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.layoutResId = layoutResId;
        this.count = count;
        this.viewType = viewType;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public int getItemCount() {
        return count;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType != this.viewType)
            return null;
        View itemView = LayoutInflater.from(context).inflate(layoutResId, viewGroup, false);
        return new BaseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {

    }
}

