package com.easing.commons.android.lbs;


import static com.easing.commons.android.lbs.BDLocationManager.locateInterval;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 轨迹优化
 */
public class OptimizerLocation {

    private static OptimizerLocation location;
    private Map<String, List<LBSLocation>> comMap;
    private int maxSize;// 无gps情况最长多少个点记录一次 大概 10分钟

    private int maxSpeed = 1;   //最大移动速度，一下者过滤
    private int maxRadius = 20; //最大定位精度以上过滤
    private int maxTime = 5;// 无gps情况最长多少分钟录一次

    public LBSLocation getOptimizerLocation(String tag, LBSLocation location) {

        if (location == null)
            return null;

        //过滤
        if (location.radius < maxRadius && location.speed > maxSpeed) {
            if (comMap != null && comMap.get(tag) != null && comMap.get(tag).size() > 0)
                comMap.get(tag).clear();
            return location;
        } else {
            if (comMap != null && !comMap.containsKey(tag))
                comMap.put(tag, new ArrayList<>());

            if (comMap.get(tag).size() > maxSize) {
                return getLocation(tag);
            }
            if (comMap.get(tag).size() > 0) {

                if (comMap.get(tag).get(0).radius > location.radius) {
                    comMap.get(tag).add(0, location);
                } else {
                    comMap.get(tag).add(location);
                }

            } else {
                comMap.get(tag).add(location);
            }
            return null;
        }
    }

    public OptimizerLocation() {
        comMap = new LinkedHashMap<>();
        maxSize = (maxTime * 60) / (locateInterval / 1000); //计数多少个点
    }

    public static OptimizerLocation initOptimizer() {
        if (location == null)
            location = new OptimizerLocation();
        return location;
    }

    /**
     * 获取信号最好的点
     */
    private LBSLocation getLocation(String tag) {
        if (comMap.containsKey(tag) && comMap.get(tag) != null) {
            LBSLocation location = Objects.requireNonNull(comMap.get(tag)).get(0);
            Objects.requireNonNull(comMap.get(tag)).clear(); //拿了后清空
            return location;

        } else {
            return null;
        }
    }
}
