package com.easing.commons.android.ui.control.fragment_tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.easing.commons.android.R;
import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.app.CommonFragment;
import com.easing.commons.android.manager.Dimens;
import com.easing.commons.android.value.color.Colors;
import com.easing.commons.android.view.Views;

@SuppressWarnings("all")
public class FragmentTabLayout extends LinearLayout {

    Float fontSize = (float) Dimens.toPx(14);
    Float padding = (float) Dimens.toPx(5);

    Integer normalColor = Colors.LIGHT_GREY;
    Integer activeColor = Colors.LIGHT_BLUE;

    OnSelected onSelected;

    Integer selectedIndex = -1;
    FragmentTabItem selectedItem = null;

    public FragmentTabLayout(Context context) {
        this(context, null);
    }

    public FragmentTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrSet) {
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray attrs = context.obtainStyledAttributes(attrSet, R.styleable.FragmentTabLayout);
        fontSize = attrs.getDimension(R.styleable.FragmentTabLayout_fontSize, fontSize);
        padding = attrs.getDimension(R.styleable.FragmentTabLayout_padding, padding);
        normalColor = attrs.getColor(R.styleable.FragmentTabLayout_normalColor, normalColor);
        activeColor = attrs.getColor(R.styleable.FragmentTabLayout_activeColor, activeColor);
    }

    //初始化所有Fragments
    public FragmentTabLayout fragments(@IdRes Integer containerViewId, Class<? extends CommonFragment>... fragmentClasses) {
        CommonActivity activity = (CommonActivity) getContext();
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            FragmentTabItem item = (FragmentTabItem) getChildAt(i);
            item.fragmentClass = fragmentClasses[i];
            item.fragment = Reflection.newInstance(item.fragmentClass);
            item.setItemStyle(this, i);
            transaction.add(containerViewId, item.fragment, item.fragmentClass.getName());
            transaction.hide(item.fragment);
        }
        transaction.commit();
        return this;
    }

    //选中某个Fragment
    public FragmentTabLayout select(int index) {
        FragmentTabItem item = (FragmentTabItem) getChildAt(index);

        //如果已选中，则刷新
        if (item == selectedItem) {
            selectedItem.fragment.onDeselected();
            selectedItem.fragment.onSelected();
            return this;
        }

        //如果有其它Fragment选中，取消选中
        if (selectedItem != null) {
            selectedItem.fragment.onDeselected();
            selectedItem.imageView.setColorFilter(normalColor);
            selectedItem.textView.setTextColor(normalColor);
        }

        //切换Fragment
        CommonActivity activity = (CommonActivity) getContext();
        FragmentManager manager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (selectedItem != null)
            transaction.hide(selectedItem.fragment);
        transaction.show(item.fragment);
        transaction.commit();

        //选中当前Fragment
        selectedIndex = index;
        selectedItem = item;
        selectedItem.imageView.setColorFilter(activeColor);
        selectedItem.textView.setTextColor(activeColor);
        selectedItem.fragment.onSelected();

        //执行选中回调
        if (onSelected != null)
            onSelected.onSelect(index, selectedItem.fragment);

        return this;
    }

    //选中某个Fragment
    public FragmentTabLayout selectFastClick(int index) {
        //执行选中回调
        if (onSelected != null)
            onSelected.selectFastClick(index, selectedItem.fragment);

        return this;
    }

    //按类名选取Fragment
    public FragmentTabLayout select(Class clazz) {
        int count = getChildCount();
        int index = -1;
        for (int i = 0; i < count; i++) {
            FragmentTabItem item = (FragmentTabItem) getChildAt(i);
            if (item.fragmentClass == clazz) index = i;
        }
        select(index);
        return this;
    }

    //按类名选取Fragment
    public FragmentTabLayout select(String className) {
        Class clazz = Reflection.findClass(className);
        return select(clazz);
    }

    //获取选中的Fragment
    public Integer selectedIndex() {
        return selectedIndex;
    }

    //获取选中的Fragment
    public CommonFragment selectedFragment() {
        if (selectedItem == null) return null;
        return selectedItem.fragment;
    }

    //选中回调
    public void onSelected(OnSelected listener) {
        this.onSelected = listener;
    }

    public interface OnSelected {
        void onSelect(int index, CommonFragment fragment);

        void selectFastClick(int index, CommonFragment fragment);
    }
}
