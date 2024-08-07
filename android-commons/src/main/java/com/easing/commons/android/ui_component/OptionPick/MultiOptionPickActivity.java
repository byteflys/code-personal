package com.easing.commons.android.ui_component.OptionPick;

import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.data.DataCenter;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.helper.tool.NameTranslator;
import com.easing.commons.android.ui.control.title_bar.TitleBar;
import com.easing.commons.android.view.Views;

import java.util.List;

import butterknife.BindView;

@SuppressWarnings("all")
public class MultiOptionPickActivity extends CommonActivity<MultiOptionPickActivity> {

    @BindView(R2.id.title_bar)
    TitleBar titleBar;

    @BindView(R2.id.grid_layout)
    GridLayout gridLayout;

    String requestCode = DataCenter.get("requestCodeForMultiOptionPick");
    String title = DataCenter.get("titleForOptionPick");
    List fullOptionList = DataCenter.get("fullOptionList");
    List selectedOptionList = DataCenter.get("selectedOptionList");
    NameTranslator nameTranslator = DataCenter.get("nameTranslator");

    @Override
    protected boolean beforeCreate() {
        statusBarColor = R.color.color_transparent;
        return super.beforeCreate();
    }

    @Override
    protected void create() {

        setContentView(R.layout.md001_multi_option_pick_activity);

        //设置标题栏
        titleBar.setTitleText(title);

        //根据选项列表，动态添加控件
        for (Object option : fullOptionList) {
            ViewGroup item = Views.inflateAndAttach(ctx, R.layout.md001_multi_option_pick_item, gridLayout, false);
            item.setTag(option);
            gridLayout.addView(item);
            ImageView imageView = (ImageView) item.getChildAt(0);
            TextView textView = (TextView) item.getChildAt(1);
            textView.setText(nameTranslator.name(option));
            imageView.setSelected(selectedOptionList.contains(option));
            onClick(imageView, () -> {
                boolean selected = imageView.isSelected();
                if (selected)
                    selectedOptionList.remove(option);
                else
                    selectedOptionList.add(option);
                imageView.setSelected(!selected);
            });
        }

        //数量小于4个时，用空的控件进行占位，以调整布局
        if (fullOptionList.size() < 4) {
            int dCount = 4 - fullOptionList.size();
            for (int i = 0; i < dCount; i++) {
                ViewGroup item = Views.inflateAndAttach(ctx, R.layout.md001_multi_option_pick_item, gridLayout, false);
                gridLayout.addView(item);
                item.setVisibility(View.INVISIBLE);
            }
        }

        //确定
        onClick(R.id.bt_ok, () -> {
            finish();
            EventBus.core.emit("onMultiOptionSelected", requestCode, selectedOptionList);
        });
    }
}
