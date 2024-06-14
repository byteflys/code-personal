package com.easing.commons.android.lbs;

import com.easing.commons.android.format.Texts;

//按时间间隔和信号强度优化坐标
//10秒内的强信号坐标视为同一组
@SuppressWarnings("all")
public class OptimizerForTime implements LBSLocationOptimizer {

    //坐标所属轨迹段
    String period;

    //最大速度，超出最大速度的视为偏移坐标
    int maxSpeed = 40;

    //上个强信号坐标的时间
    Long lastTimeMillis;

    //上个保留的坐标
    LBSLocation lastKeepLocation;

    //判断坐标质量
    @Override
    public boolean bad(LBSLocation location) {
        if (Texts.equal(location.accuracy, LBSLocation.SIGNAL_BAD)) return true;
        return false;
    }

    //不管是强弱坐标都会保留，应用层需要自己决定如何使用这两类坐标
    //信号质量较差的坐标，period会被设为"bad"
    @Override
    synchronized public boolean validate(LBSLocation location) {
        //过滤距离很近的坐标
        if (lastKeepLocation != null)
            if (PolylineCalculator.distance(lastKeepLocation, location) < 1)
                return false;
        //信号非常强的坐标，予以保留
        if (Texts.equal(location.accuracy, LBSLocation.SIGNAL_STRONG))
            return true;
        //距离很远的坐标，直接视为差坐标
        if (lastKeepLocation != null)
            if (PolylineCalculator.distance(lastKeepLocation, location) > maxSpeed * BDLocationManager.locateInterval / 1000)
                location.accuracy = LBSLocation.SIGNAL_WEAK;
        //记录上次坐标
        lastKeepLocation = location;
        //信号质量较差的坐标
        if (bad(location)) {
            location.period = "bad";
            return true;
        }
        //首个高信号质量的坐标
        if (period == null) {
            period = Texts.random();
            location.period = period;
            lastTimeMillis = location.timeMillis;
            return true;
        }
        //连续的高质量坐标
        if (location.timeMillis - lastTimeMillis < 10000) {
            location.period = period;
            lastTimeMillis = location.timeMillis;
            return true;
        }
        //不连续的高质量坐标
        period = Texts.random();
        location.period = period;
        lastTimeMillis = location.timeMillis;
        return true;
    }

}

