package com.easing.commons.android.lbs;

import java.util.List;

//计算多边形长度
@SuppressWarnings("all")
public class PolylineCalculator {

    //求两个坐标间的距离
    public static Double distance(double lat1, double lng1, double lat2, double lng2) {
        //地球半径
        Double R = 6370996.81;
        //转化为墨卡托坐标求距离
        Double x = (lng2 - lng1) * Math.PI * R * Math.cos(((lat1 + lat2) / 2) * Math.PI / 180) / 180;
        Double y = (lat2 - lat1) * Math.PI * R / 180;
        Double distance = Math.hypot(x, y);
        return distance;
    }

    //求两个坐标间的距离
    public static Double distance(LBSLocation p1, LBSLocation p2) {
        return PolylineCalculator.distance(p1.latitude, p1.longitude, p2.latitude, p2.longitude);
    }

    //计算折线长度
    public static double polylineLength(List<LBSLocation> points) {
        if (points.size() < 2) return 0D;
        double distance = 0D;
        synchronized (points) {
            LBSLocation lastPoint = points.get(0);
            for (int i = 1; i < points.size(); i++) {
                LBSLocation currentPoint = points.get(i);
                Double d = PolylineCalculator.distance(lastPoint, currentPoint);
                distance += d;
                lastPoint = currentPoint;
            }
        }
        return distance;
    }


}
