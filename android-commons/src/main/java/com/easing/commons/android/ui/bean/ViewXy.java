package com.easing.commons.android.ui.bean;

import lombok.Data;

@Data
public class ViewXy {
    private float x;
    private float y;


    public ViewXy(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
