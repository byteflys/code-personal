package com.easing.commons.android.ui_component.tab_layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.easing.commons.android.R;
import com.easing.commons.android.R2;
import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.view.Views;

import java.util.ArrayList;

import butterknife.BindView;

//点击标签，切换到对应的View
public class VerticalTabLayout extends FrameLayout {

    final ArrayList<String> titles = new ArrayList();
    final ArrayList<Class<? extends VerticalTabItem>> titleClasses = new ArrayList();
    final ArrayList<Class<? extends View>> contentClasses = new ArrayList();

    @BindView(R2.id.layout_title)
    LinearLayout titleLayout;
    @BindView(R2.id.layout_content)
    FrameLayout contentLayout;

    public VerticalTabLayout(Context context) {
        this(context, null);
    }

    public VerticalTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LinearLayout root = Views.inflate(R.layout.layout_vertical_tab_layout);
        addView(root, new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
        Views.viewBinding(this, root);
    }

    public VerticalTabLayout addItem(String title, Class<? extends VerticalTabItem> titleClazz, Class<? extends View> contentClazz) {
        titles.add(title);
        contentClasses.add(contentClazz);
        View view = Reflection.createViewInstance(getContext(), (Class<? extends View>) titleClazz);
        VerticalTabItem item = (VerticalTabItem) view;
        item.setTitle(title);

        //TODO => 测试样式
        view.setPadding(Dimens.toPx(10), Dimens.toPx(10), Dimens.toPx(10), Dimens.toPx(10));
        view.setBackgroundResource(R.drawable.button_m06);
        item.textColor(R.color.color_dark_green);
        item.textSize(22);

        titleLayout.addView(view, new LinearLayout.LayoutParams(Views.MATCH_PARENT, Views.WRAP_CONTENT));
        return this;
    }

    public void select(String title) {
        contentLayout.removeAllViews();
        for (int i = 0; i < titles.size(); i++) {
            String tabTitle = titles.get(i);
            boolean select = tabTitle.equals(title);
            VerticalTabItem item = (VerticalTabItem) titleLayout.getChildAt(i);
            Class<? extends View> contentClazz = contentClasses.get(i);
            if (select) {
                View content = Reflection.createViewInstance(getContext(), contentClazz);
                contentLayout.addView(content, new FrameLayout.LayoutParams(Views.MATCH_PARENT, Views.MATCH_PARENT));
                item.selected(true);
            } else item.selected(false);
        }
    }

}
