package com.easing.commons.android.ui.control.fragment_tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonFragment;
import com.easing.commons.android.view.Views;

@SuppressWarnings("all")
public class FragmentTabItem extends LinearLayout {

    Integer icon;
    String title;

    ImageView imageView;
    TextView textView;

    CommonFragment fragment;
    Class<? extends CommonFragment> fragmentClass;

    public FragmentTabItem(Context context) {
        this(context, null);
    }

    public FragmentTabItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    private void init(Context context, AttributeSet attrSet) {
        setOrientation(LinearLayout.VERTICAL);
        TypedArray attrs = context.obtainStyledAttributes(attrSet, R.styleable.FragmentTabItem);
        icon = attrs.getResourceId(R.styleable.FragmentTabItem_image, R.drawable.color_transparent);
        title = attrs.getString(R.styleable.FragmentTabItem_text);
    }

    //添加Item到Parent，并设置样式
    protected void setItemStyle(FragmentTabLayout parent, int index) {
        //添加图片
        imageView = new ImageView(getContext());
        imageView.setImageResource(icon);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setColorFilter(parent.normalColor);
        addView(imageView, new LayoutParams(Views.MATCH_PARENT, 0, 1));
        //添加文字
        textView = new TextView(getContext());
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(parent.normalColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, parent.fontSize);
        textView.setPadding(0, parent.padding.intValue(), 0, 0);
        addView(textView, new LayoutParams(Views.MATCH_PARENT, Views.WRAP_CONTENT, 0));
        //设置布局
        setLayoutParams(new LayoutParams(0, Views.MATCH_PARENT, 1));
        setPadding(0, parent.padding.intValue(), 0, parent.padding.intValue());
        //设置事件
        setOnClickListener(new OnClickListener() {
                               @Override
                               public void onClick(View v) {

                                   if (Views.isFastClick()) { //双击事件
                                       parent.selectFastClick(index);
                                   } else {
                                       parent.select(index); //点击事件
                                   }
                               }
                           }
        );


    }

}
