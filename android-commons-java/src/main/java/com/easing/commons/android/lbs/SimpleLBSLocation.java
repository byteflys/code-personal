package com.easing.commons.android.lbs;

import com.easing.commons.android.format.Maths;

@SuppressWarnings("all")
public class SimpleLBSLocation {

    public double latitude;
    public double longitude;
    public double altitude;

    public static SimpleLBSLocation of(double latitude, double longitude) {
        SimpleLBSLocation location = new SimpleLBSLocation();
        location.latitude = latitude;
        location.longitude = longitude;
        location.removeRedundantFloat();
        return location;
    }

    public static SimpleLBSLocation of(LBSLocation lbsLocation) {
        if (lbsLocation == null)
            return SimpleLBSLocation.of(0, 0);
        SimpleLBSLocation location = new SimpleLBSLocation();
        location.latitude = lbsLocation.latitude;
        location.longitude = lbsLocation.longitude;
        location.altitude = lbsLocation.altitude;
        location.removeRedundantFloat();
        return location;
    }

    //去除多余的小数位
    public SimpleLBSLocation removeRedundantFloat() {
        latitude = Maths.keepFloat2(latitude, 6);
        longitude = Maths.keepFloat2(longitude, 6);
        return this;
    }
}

