package com.easing.commons.android.ui.control.selection_layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.easing.commons.android.struct.Collections;

import java.util.List;

@SuppressWarnings("all")
public class SelectionLinearLayout extends LinearLayout implements ISelectionLayout {

    protected View selectedView = null;
    protected OnViewSelectedListener listener = null;

    public SelectionLinearLayout(Context context) {
        this(context, null);
    }

    public SelectionLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    protected void init(Context context, AttributeSet attributeSet) {

    }

    @Override
    public List<View> children() {
        List<View> children = Collections.emptyList();
        for (int i = 0; i < getChildCount(); i++)
            children.add(getChildAt(i));
        return children;
    }

    @Override
    public SelectionLinearLayout selectView(View selectedView) {
        if (this.selectedView != null)
            this.selectedView.setSelected(false);
        selectedView.setSelected(true);
        this.selectedView = selectedView;
        if (listener != null)
            listener.onViewSelected(selectedView);
        return this;
    }

    @Override
    public SelectionLinearLayout selectViewById(Integer viewId) {
        int childCount = getChildCount();
        if (childCount == 0) return this;
        View view = (viewId == null || viewId == 0) ? getChildAt(0) : findViewById(viewId);
        selectView(view);
        return this;
    }

    @Override
    public SelectionLinearLayout onViewSelectedListener(OnViewSelectedListener listener) {
        this.listener = listener;
        return this;
    }

}
