package com.easing.commons.android.lbs;

import java.math.BigDecimal;
import java.util.List;

//计算多边形面积
@SuppressWarnings("all")
public class PolygonCalculator {

    private static final double earthRadius = 6371000.0;
    private static final double metersPerDegree = 2.0 * Math.PI * earthRadius / 360.0;
    private static final double radiansPerDegree = Math.PI / 180.0;
    private static final double degreesPerRadian = 180.0 / Math.PI;

    //计算平面多边形面积
    public static double planarPolygonArea(List<LBSLocation> locations) {
        if (locations.size() < 3) return 0;
        double area = 0.0;
        for (int i = 0; i < locations.size(); i++) {
            int j = (i + 1) % locations.size();
            double xi = locations.get(i).longitude * metersPerDegree * Math.cos(locations.get(i).latitude * radiansPerDegree);
            double yi = locations.get(i).latitude * metersPerDegree;
            double xj = locations.get(j).longitude * metersPerDegree * Math.cos(locations.get(j).latitude * radiansPerDegree);
            double yj = locations.get(j).latitude * metersPerDegree;
            area += xi * yj - xj * yi;
        }
        area = BigDecimal.valueOf(Math.abs(area / 2.0)).setScale(1, BigDecimal.ROUND_DOWN).doubleValue();
        return area;
    }

    //计算球面多边形面积
    public static double sphericalPolygonArea(List<LBSLocation> locations) {
        if (locations.size() < 3) return 0;
        double totalAngle = 0.0;
        for (int i = 0; i < locations.size(); i++) {
            int j = (i + 1) % locations.size();
            int k = (i + 2) % locations.size();
            totalAngle += angle(locations.get(i), locations.get(j), locations.get(k));
        }

        double planarTotalAngle = (locations.size() - 2) * 180.0;
        double sphericalExcess = totalAngle - planarTotalAngle;

        if (sphericalExcess > 420.0) {
            totalAngle = locations.size() * 360.0 - totalAngle;
            sphericalExcess = totalAngle - planarTotalAngle;
        } else if (sphericalExcess > 300.0 && sphericalExcess < 420.0) {
            sphericalExcess = Math.abs(360.0 - sphericalExcess);
        }

        return sphericalExcess * radiansPerDegree * earthRadius * earthRadius;
    }

    //计算拐点夹角
    private static double angle(LBSLocation p1, LBSLocation p2, LBSLocation p3) {
        double bearing21 = orientation(p2, p1);
        double bearing23 = orientation(p2, p3);
        double angle = bearing21 - bearing23;
        if (angle < 0.0) angle += 360.0;
        return angle;
    }

    //计算线段方向
    private static double orientation(LBSLocation from, LBSLocation to) {
        double lat1 = from.latitude * radiansPerDegree;
        double lon1 = from.longitude * radiansPerDegree;
        double lat2 = to.latitude * radiansPerDegree;
        double lon2 = to.longitude * radiansPerDegree;
        double angle = -Math.atan2(
                Math.sin(lon1 - lon2) * Math.cos(lat2),
                Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2)
        );
        if (angle < 0.0) angle += Math.PI * 2.0;
        angle = angle * degreesPerRadian;
        return angle;
    }


}
