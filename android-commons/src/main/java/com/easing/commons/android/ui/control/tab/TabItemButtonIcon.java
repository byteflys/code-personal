package com.easing.commons.android.ui.control.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easing.commons.android.R;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

import lombok.Getter;
import lombok.Setter;

// 默认样式：
// 字体颜色：淡灰-淡蓝
// 字体大小：14dp
// 标题和图标间距：2dp
// Item上下内间距：5dp
// 使用方法：放在TabLayout中，设置属性即可
public class TabItemButtonIcon extends LinearLayout {

    int icon;
    String title;
    float fontSize;
    int slideWidth;

    @Getter
    @Setter
    private int normalColor;
    @Getter
    @Setter
    private int activeColor;

    private ImageView iv;
    private TextView tv;

    public TabItemButtonIcon(Context context) {
        super(context);
        init(context, null);
    }

    public TabItemButtonIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TabItemButtonIcon(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrSet) {
        //解析属性
        TypedArray attrs = context.obtainStyledAttributes(attrSet, R.styleable.ButtonIconTabItem);
        icon = attrs.getResourceId(R.styleable.ButtonIconTabItem_image, R.drawable.color_transparent);
        CharSequence charSequence = attrs.getText(R.styleable.ButtonIconTabItem_text);
        title = (charSequence == null) ? "" : charSequence.toString();
        fontSize = attrs.getDimension(R.styleable.ButtonIconTabItem_fontSize, Dimens.toPx(context, 14));
        normalColor = attrs.getColor(R.styleable.ButtonIconTabItem_normalColor, Colors.LIGHT_GREY);
        activeColor = attrs.getColor(R.styleable.ButtonIconTabItem_activeColor, Colors.LIGHT_BLUE);
        slideWidth = (int) attrs.getDimension(R.styleable.ButtonIconTabItem_slideWidth, ViewGroup.LayoutParams.MATCH_PARENT);

        //设置Item布局和内边距
        super.setOrientation(LinearLayout.VERTICAL);
        super.setLayoutParams(new LayoutParams(0, Views.MATCH_PARENT, 1));
        super.setPadding(0, Dimens.toPx(context, 5), 0, Dimens.toPx(context, 5));
        super.setGravity(Gravity.CENTER);
        //添加文字
        tv = new TextView(context);
        tv.setText(title);
        tv.setTextColor(normalColor);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, fontSize);
        tv.setGravity(Gravity.CENTER);
        super.addView(tv, new LayoutParams(Views.MATCH_PARENT, Views.WRAP_CONTENT, 0));
        //设置图片文字间距
        LayoutParams tvLayoutParams = (LayoutParams) tv.getLayoutParams();
        tvLayoutParams.setMargins(0, Dimens.toPx(context, 2), 0, 0);
        tv.setLayoutParams(tvLayoutParams);

        //添加图片
        if (icon != 0) {
            iv = new ImageView(context);
            iv.setImageResource(icon);
            //  iv.setColorFilter(normalColor);
            LayoutParams ivLayoutParams = new LayoutParams(Dimens.toPx(context, 20), Dimens.toPx(context, 6));
            // tv.setLayoutParams(ivLayoutParams);

            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            super.addView(iv, ivLayoutParams);
        }

        //设置默认为非选中状态
        super.setSelected(false);
        //设置选中监听器
        super.setOnClickListener(v -> {
            TabLayout parent = (TabLayout) super.getParent();
            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                TabItemButtonIcon child = (TabItemButtonIcon) parent.getChildAt(i);
                if (child != v) {
                    child.setSelected(false);
                    //  child.iv.setColorFilter(child.getNormalColor());
                    child.tv.setTextColor(child.getNormalColor());

                    if (child.iv != null) {
                        child.iv.setVisibility(Views.INVISIBLE);
                    }
                }
            }
            this.setSelected(true);
            this.tv.setTextColor(activeColor);
            if (this.iv != null) {
                this.iv.setVisibility(Views.VISIBLE);
            }
            int pos = parent.indexOfChild(v);
            parent.selectedIndex = pos;
            parent.selectedItem = this;


            if (parent.onSelected != null)
                parent.onSelected.onSelect(this, pos);

        });
    }
}
