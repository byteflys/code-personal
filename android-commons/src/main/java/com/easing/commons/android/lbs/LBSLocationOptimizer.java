package com.easing.commons.android.lbs;

@SuppressWarnings("all")
public interface LBSLocationOptimizer {

    //判断坐标质量
    boolean bad(LBSLocation location);

    //优化坐标，同时判断坐标是否需要被保留
    //true表示保留坐标，false表示舍弃坐标
    boolean validate(LBSLocation location);
}

