package com.easing.commons.android.ui.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.easing.commons.android.R;
import com.easing.commons.android.value.bean.DelayedControlData;

import java.util.List;

/*
 * button adapter
 * */
public class SelectItemAdapter extends BaseQuickAdapter<DelayedControlData, BaseViewHolder> {


    public SelectItemAdapter(int layoutResId, @Nullable List<DelayedControlData> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, DelayedControlData delayedControlData) {

        ImageView selectImage = holder.getView(R.id.item_icon);
        selectImage.setVisibility(delayedControlData.isSelect() != null ? View.VISIBLE : View.GONE);
        if (delayedControlData.isSelect() != null) {
            selectImage.setImageResource(delayedControlData.isSelect() ? R.mipmap.icon_select_ok : R.mipmap.icon_select_on);
        }
        holder.setText(R.id.item_name, delayedControlData.getShowText());
        holder.addOnClickListener(R.id.select_on_all);
    }

}
