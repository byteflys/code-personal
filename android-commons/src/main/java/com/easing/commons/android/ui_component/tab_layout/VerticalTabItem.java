package com.easing.commons.android.ui_component.tab_layout;

public interface VerticalTabItem {

    void selected(boolean selected);

    boolean selected();

    void setTitle(String title);

    void textSize(int dp);

    void textColor(int colorResource);
}
