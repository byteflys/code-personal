package com.easing.commons.android.lbs;

import com.easing.commons.android.format.Texts;
import com.easing.commons.android.journal.Journal;
import com.easing.commons.android.manager.Device;

import java.util.LinkedList;

//按距离优化坐标，去除不连续的坐标
@SuppressWarnings("all")
public class OptimizerForDistance implements LBSLocationOptimizer {

    final int validateCount = 3;
    final double minDistance = 0.1 * BDLocationManager.locateInterval / 1000;
    final double maxDistance = 40 * BDLocationManager.locateInterval / 1000;

    //坐标校验数组
    final LinkedList<LBSLocation> validateArray = new LinkedList();

    //坐标所属轨迹段
    String period;

    //判断坐标质量
    @Override
    public boolean bad(LBSLocation location) {
        return false;
    }

    //校验坐标连续性
    //如果连续出现3个紧凑的坐标，则视为正常坐标
    //period表示坐标属于不连续的轨迹段，在绘制轨迹时，不同period应当分开绘制
    @Override
    synchronized public boolean validate(LBSLocation location) {

        //位置距离过大，则跳过
        //20米可有效防止坐标抖动，但可能舍弃过多坐标，信号不好时线段会断断续续
        if (validateArray.size() > 0)
            if (PolylineCalculator.distance(location.latitude, location.longitude, validateArray.getLast().latitude, validateArray.getLast().longitude) > maxDistance) {
                Journal.save("坐标过滤", "坐标距离过远", location.longitude, location.latitude, location.accuracy, location.period, Device.screenState(), Device.sdkVersionName(), Device.modelInfo());
                //重置校验数组
                validateArray.clear();
                period = Texts.random();
                return false;
            }

        //坐标很近，不上传，只更新时间
        if (validateArray.size() > 0)
            if (PolylineCalculator.distance(location.latitude, location.longitude, validateArray.getLast().latitude, validateArray.getLast().longitude) < minDistance) {
                validateArray.getLast().timeMillis = location.timeMillis;
                Journal.save("坐标过滤", "坐标距离过近", location.longitude, location.latitude, location.accuracy, location.period, Device.screenState(), Device.sdkVersionName(), Device.modelInfo());
                return false;
            }

        //已满，则更新校验数组
        if (validateArray.size() == validateCount) {
            validateArray.removeFirst();
            validateArray.add(location);
            location.period = period;
            Journal.save("坐标保留", "Available", location.longitude, location.latitude, location.accuracy, location.period, Device.screenState(), Device.sdkVersionName(), Device.modelInfo());
            return true;
        }

        //未满，直接加入
        validateArray.add(location);
        Journal.save("坐标过滤", "校验坐标不足", location.longitude, location.latitude, location.accuracy, location.period, Device.screenState(), Device.sdkVersionName(), Device.modelInfo());
        return false;
    }


}

