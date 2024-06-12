package com.easing.commons.android.lbs;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.easing.commons.android.format.Maths;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class LBSLocationHandler {

    //计算折线总长度 算距离
    public static double polylineLength(List<LBSLocation> locations) {
        return PolylineCalculator.polylineLength(locations);
    }

    //计算球面多边形面积
    public static double polygonArea(List<LBSLocation> points) {
        return PolygonCalculator.sphericalPolygonArea(points);
    }

    //计算折线总长度
    public static String formatedPolylineLength(List<LBSLocation> locations) {
        return Maths.keepFloat(PolylineCalculator.polylineLength(locations), 0) + "米";
    }

    //计算球面多边形面积
    public static String formatedPolygonArea(List<LBSLocation> locations) {
        return Maths.keepFloat(PolygonCalculator.planarPolygonArea(locations), 0) + "平方米";
    }

    /**
     * 判断一个点是否在某个区域
     *
     * @param locations 需要判断多边形点的集合
     * @param latLng    当前位置
     * @return 是否在当前区域
     */
    public static boolean isPolygonContainsPoint(List<LBSLocation> locations, LBSLocation latLng) {

        if (locations == null || latLng == null)
            return false;
        List<LatLng> latLngs = new ArrayList<>();
        for (LBSLocation location : locations) {
            LatLng latLng1 = new LatLng(location.latitude, location.longitude);
            latLngs.add(latLng1);
        }
        return SpatialRelationUtil.isPolygonContainsPoint(latLngs, new LatLng(latLng.latitude, latLng.longitude));
    }

    /**
     * 判断一个点是否在某个区域
     *
     * @param locations 需要判断多边形点的集合
     * @param latLng    当前位置
     * @return 是否在当前区域
     */
    public static boolean isPolygonContainsPoints(List<LatLng> locations, LBSLocation latLng) {

        if (locations == null || latLng == null)
            return false;
        return SpatialRelationUtil.isPolygonContainsPoint(locations, new LatLng(latLng.latitude, latLng.longitude));
    }

    /**
     * 判断一个点是否在某个区域
     *
     * @param locations 需要判断多边形点的集合
     * @param latLng    当前位置
     * @return 是否在当前区域
     */
    public static boolean isPolygonContainsGeoPoint(List<GeoPoint> locations, LBSLocation latLng) {

        if (locations == null || latLng == null)
            return false;
        List<LatLng> latLngs = new ArrayList<>();
        for (GeoPoint location : locations) {
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
            latLngs.add(latLng1);
        }
        return SpatialRelationUtil.isPolygonContainsPoint(latLngs, new LatLng(latLng.latitude, latLng.longitude));
    }

}

