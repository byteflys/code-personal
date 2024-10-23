package com.easing.commons.android.format;

import java.text.DecimalFormat;
import java.text.Format;

public class Formats {

    //格式化路程，length单位为米
    public static String formatDistance(Number length) {
        if (length.doubleValue() >= 1000)
            return Maths.keepFloat(length.doubleValue() / 1000d, 1) + "km";
        return Maths.keepFloat(length.doubleValue(), 0) + "m";
    }

    //速度转换，米每秒转千米每小时
    public static String metersPerSecondToKilometersPerHour(double speed) {
        double kmPerH = speed * 3600 / 1000d;
        return Maths.keepFloat(kmPerH, 1) + "km/h";
    }

    public static String intStringTime(int speed) {
        Format format = new DecimalFormat("00");
        return format.format(speed);
    }
}
