package com.easing.commons.android.lbs;

import com.easing.commons.android.format.Maths;

@SuppressWarnings("all")
public class LBSCoordinateConvertor {

    //判断是否是有效的十进制经度
    public static boolean isLongitude(String strDecimal) {
        try {
            double decimal = Double.valueOf(strDecimal);
            return LBSCoordinateConvertor.isLongitude(decimal);
        } catch (Exception e) {
            return false;
        }
    }

    //判断是否是有效的十进制纬度
    public static boolean isLatitude(String strDecimal) {
        try {
            double decimal = Double.valueOf(strDecimal);
            return LBSCoordinateConvertor.isLatitude(decimal);
        } catch (Exception e) {
            return false;
        }
    }

    //判断是否是有效的度分秒式经度
    public static boolean isLongitude(String[] strDms) {
        try {
            double[] dms = new double[3];
            dms[0] = Double.valueOf(strDms[0]);
            dms[1] = Double.valueOf(strDms[1]);
            dms[2] = Double.valueOf(strDms[2]);
            return LBSCoordinateConvertor.isLongitude(dms);
        } catch (Exception e) {
            return false;
        }
    }

    //判断是否是有效的度分秒式纬度
    public static boolean isLatitude(String[] strDms) {
        try {
            double[] dms = new double[3];
            dms[0] = Double.valueOf(strDms[0]);
            dms[1] = Double.valueOf(strDms[1]);
            dms[2] = Double.valueOf(strDms[2]);
            return LBSCoordinateConvertor.isLatitude(dms);
        } catch (Exception e) {
            return false;
        }
    }

    //判断是否是有效的十进制经度
    public static boolean isLongitude(double decimal) {
        if (decimal < -180 || decimal > 180)
            return false;
        return true;
    }

    //判断是否是有效的十进制纬度
    public static boolean isLatitude(double decimal) {
        if (decimal < -90 || decimal > 90)
            return false;
        return true;
    }

    //判断是否是有效的度分秒式经度
    public static boolean isLongitude(double[] dms) {
        if (dms[0] < -180 || dms[0] > 180)
            return false;
        if (dms[0] % 1 != 0)
            return false;
        if (dms[1] < 0 || dms[1] >= 60)
            return false;
        if (dms[1] % 1 != 0)
            return false;
        if (dms[2] < 0 || dms[2] >= 60)
            return false;
        return true;
    }

    //判断是否是有效的度分秒式纬度
    public static boolean isLatitude(double[] dms) {
        if (dms[0] < -90 || dms[0] > 90)
            return false;
        if (dms[0] % 1 != 0)
            return false;
        if (dms[1] < 0 || dms[1] >= 60)
            return false;
        if (dms[1] % 1 != 0)
            return false;
        if (dms[2] < 0 || dms[2] >= 60)
            return false;
        return true;
    }

    //十进制经纬度转度分秒
    public static double[] decimalToDegree(double decimal) {
        double[] dms = new double[3];
        int pos = decimal < 0 ? -1 : 1;
        decimal = Math.abs(decimal);
        int deg = (int) decimal;
        decimal = decimal - deg;
        int min = (int) (decimal * 60);
        decimal = decimal - min / 60D;
        String sec = Maths.keepFloat(decimal * 3600, 2);
        dms[0] = pos * deg;
        dms[1] = min;
        dms[2] = Double.valueOf(sec);
        return dms;
    }

    //度分秒转十进制
    public static double degreeToDecimal(double[] dms) {
        double decimal = dms[0] + dms[1] / 60D + dms[2] / 3600D;
        return decimal;
    }

    //度分秒转十进制
    public static double degreeToDecimal(String strDms) {
        int positive = 1;
        if (strDms.contains("S") || strDms.contains("W"))
            positive = -1;
        strDms = strDms.replaceAll("E", "");
        strDms = strDms.replaceAll("W", "");
        strDms = strDms.replaceAll("N", "");
        strDms = strDms.replaceAll("S", "");
        strDms = strDms.replaceAll("°", "#");
        strDms = strDms.replaceAll("′", "#");
        strDms = strDms.replaceAll("″", "");
        String[] splits = strDms.split("#");
        double decimal = Double.valueOf(splits[0]) + Double.valueOf(splits[1]) / 60D + Double.valueOf(splits[2]) / 3600D;
        decimal = decimal * positive;
        return decimal;
    }
}
