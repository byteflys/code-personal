package com.easing.commons.android.lbs;

public enum LocateMode {

    //单次定位，定位成功后立刻停止
    Once,

    //周期性定位，每30秒定位一次
    Period,

    //轨迹记录，每5秒定位一次
    Track
}

