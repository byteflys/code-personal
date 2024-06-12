package com.easing.commons.android.lbs;

import java.util.LinkedList;
import java.util.List;

//根据圆心和半径，计算圆周边界坐标
public class CircleBoundCalculator {

    //米单位到投影单位的转换比例
    static final double metersPerUnit = 111194.87428468118D;

    //count为圆上的取点数量
    public static List<SimpleLBSLocation> getCircleBoundInFixCount(double latitude, double longitude, double radius, Integer count) {

        //默认数量
        if (count == null)
            count = 100;

        //r是以投影为单位的半径
        double r = radius / metersPerUnit;

        //取点
        List<SimpleLBSLocation> bound = new LinkedList();
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * modulo(i, count) / count;
            double lat = latitude + r * Math.sin(angle);
            double lon = longitude + r * Math.cos(angle);
            SimpleLBSLocation loc = SimpleLBSLocation.of(lat, lon);
            bound.add(loc);
        }
      //  bound.add(bound.get(0));

        //返回
        return bound;
    }

    //arc为两个相邻点之间的弧长
    public static List<SimpleLBSLocation> getCircleBoundInFixArc(double latitude, double longitude, double radius, Double arc) {

        //默认弧长
        if (arc == null)
            arc = 10D;

        //计算取点数
        double count = Math.ceil(2 * Math.PI * radius / arc);

        //r是以投影为单位的半径
        double r = radius / metersPerUnit;

        //取点
        List<SimpleLBSLocation> bound = new LinkedList();
        for (int i = 0; i < count; i++) {
            double angle = 2 * Math.PI * modulo(i, count) / count;
            double lat = latitude + r * Math.sin(angle);
            double lon = longitude + r * Math.cos(angle);
            SimpleLBSLocation loc = SimpleLBSLocation.of(lat, lon);
            bound.add(loc);
        }
      //  bound.add(bound.get(0));

        //返回
        return bound;
    }

    //取模
    protected static double modulo(double a, double b) {
        double r = a % b;
        return r * b < 0 ? r + b : r;
    }

}

