package com.easing.commons.android.lbs;

import com.easing.commons.android.format.Maths;
import com.easing.commons.android.format.TextBuilder;
import com.easing.commons.android.time.Times;

import lombok.Data;

@Data
@SuppressWarnings("all")
public class LBSLocation {

    public static final transient String SIGNAL_STRONG = "STRONG";
    public static final transient String SIGNAL_MEDIUM = "MEDIUM";
    public static final transient String SIGNAL_WEAK = "WEAK";
    public static final transient String SIGNAL_BAD = "BAD";

    public double latitude;
    public double longitude;
    public double altitude;
    public double direction;
    public String address;
    public String time;
    public String period = "bad";

    public String country;
    public String province;
    public String city;
    public String district;
    public String street;
    public String streetNumber;
    public float speed;//当前速度 km/h
    public float radius; //定位精度单位m

    public long timeMillis;
    public String accuracy = LBSLocation.SIGNAL_BAD;



    public int gpsStatus;

    public static LBSLocation of(double latitude, double longitude) {
        LBSLocation location = new LBSLocation();
        location.latitude = latitude;
        location.longitude = longitude;
        location.time = Times.formatDate(location.timeMillis);
        location.removeRedundantFloat();
        return location;
    }

    public static LBSLocation of(double latitude, double longitude,double altitude) {
        LBSLocation location = new LBSLocation();
        location.latitude = latitude;
        location.longitude = longitude;
        location.altitude = altitude;
        location.time = Times.formatDate(location.timeMillis);
        location.removeRedundantFloat();
        return location;
    }

    public LBSLocation() {
        timeMillis = Times.millisOfNow();
    }

    @Override
    public String toString() {
        removeRedundantFloat();
        if (address == null) address = "无";
        return "经度：" + longitude + "\n纬度：" + latitude + "\n地址：" + address + "\n时间：" + time;
    }

    public String description() {
        removeRedundantFloat();
        if (address == null) address = "无";
        TextBuilder builder = TextBuilder.create();
        builder.append("经度：").append(longitude).space(2);
        builder.append("纬度：").append(latitude).space(2);
        builder.append("地址：").append(address).space(2);
        builder.append("时间：").append(time).space(2);
        builder.append("信号：").append(accuracy).space(2);
        String description = builder.build();
        return description;
    }

    public String signalLevel() {
        if (LBSLocation.SIGNAL_STRONG.equals(accuracy)) return "强";
        if (LBSLocation.SIGNAL_MEDIUM.equals(accuracy)) return "中";
        if (LBSLocation.SIGNAL_WEAK.equals(accuracy)) return "弱";
        return "极差";
    }

    //返回度分秒式经经度
    public String longitudeToDegree() {
        String orientation = "";
        if (longitude < 0) orientation = "W";
        if (longitude > 0) orientation = "E";
        double value = longitude;
        int deg = (int) value;
        value = value - deg;
        int min = (int) (value * 60);
        value = value - min / 60D;
        String sec = Maths.keepFloat(value * 3600, 2);
        return deg + "°" + min + "′" + sec + "″" + orientation;
    }

    //返回度分秒式经纬度
    public String latitudeToDegree() {
        String orientation = "";
        if (latitude < 0) orientation = "S";
        if (latitude > 0) orientation = "N";
        double value = latitude;
        int deg = (int) value;
        value = value - deg;
        int min = (int) (value * 60);
        value = value - min / 60D;
        String sec = Maths.keepFloat(value * 3600, 2);
        return deg + "°" + min + "′" + sec + "″" + orientation;
    }

    //去除多余的小数位
    public LBSLocation removeRedundantFloat() {
        latitude = Maths.keepFloat2(latitude, 6);
        longitude = Maths.keepFloat2(longitude, 6);
        return this;
    }
}

