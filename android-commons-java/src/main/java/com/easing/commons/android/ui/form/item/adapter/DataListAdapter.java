package com.easing.commons.android.ui.form.item.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewAdapter;
import com.easing.commons.android.ui.control.list.easy_list.EasyListViewHolder;
import com.easing.commons.android.ui.form.item.FormUrlLayout;

import butterknife.BindView;

public class DataListAdapter extends EasyListViewAdapter<FormUrlLayout.DataBean, DataListAdapter.Holder> {

    @Override
    public void onViewBind(Holder holder, FormUrlLayout.DataBean data, int position) {
        holder.item_name.setText(data.getName());//专家明

        holder.item_icon.setImageResource(data.isSelect ? R.drawable.anim_view_type_item_ok : R.drawable.anim_view_type_item_on);
        // holder.ask_time.setText(data.getSendTime());
        holder.root.setOnClickListener(new View.OnClickListener() { //
            @Override
            public void onClick(View v) {
                if (onItemClick != null)
                    onItemClick.onClick(v, data, position);
            }
        });
    }


    public static class Holder extends EasyListViewHolder {

        @BindView(R2.id.item_icon)
        ImageView item_icon;

        @BindView(R2.id.item_name)
        TextView item_name;
    }
}