package com.easing.commons.android.baidu_map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.easing.commons.android.R;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.lbs.LBSLocation;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.ui.dialog.TipBox;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("all")
public class BaiduMap extends FrameLayout {

    MapView coreMap;
    com.baidu.mapapi.map.BaiduMap controller;

    final ConcurrentLinkedQueue<OverlayInfo> overlayManager = new ConcurrentLinkedQueue();

    //初始化百度SDK
    public static void initBaiduSDK() {
        SDKInitializer.initialize(CommonApplication.ctx);
        SDKInitializer.setCoordType(CoordType.GCJ02);
    }

    public BaiduMap(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BaiduMap(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        init(context, attributeSet);
    }

    //初始化地图
    protected void init(Context context, AttributeSet attributeSet) {
        coreMap = new MapView(context);
        controller = coreMap.getMap();
        addView(coreMap, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //隐藏无用信息
        View child = coreMap.getChildAt(1);
        if (child != null)
            if (child instanceof ImageView)
                child.setVisibility(View.INVISIBLE);
        coreMap.showScaleControl(false);
        coreMap.showZoomControls(false);

        //默认使用矢量地图
        controller.setMapType(controller.MAP_TYPE_NORMAL);

        //关闭默认位置图层
        controller.setMyLocationEnabled(false);

        //禁止地图旋转
        controller.getUiSettings().setRotateGesturesEnabled(false);

        //添加总的点击事件
        OnOverlayClick mainListener = overlay -> {
            OverlayInfo overlayInfo = findOverlayInfo(overlay);
            if (overlayInfo != null && overlayInfo.listener != null)
                return overlayInfo.listener.onMapClick(overlay);
            return true;
        };
        controller.setOnMarkerClickListener(marker -> {
            return mainListener.onMapClick(marker);
        });
        controller.setOnPolylineClickListener(marker -> {
            return mainListener.onMapClick(marker);
        });
    }

    //========================================  地图相关API  ========================================

    //获取地图中心坐标
    public LBSLocation getCenterLocation() {
        LatLng target = controller.getMapStatus().target;
        return LBSLocation.of(target.latitude, target.longitude);
    }

    //获取地图缩放等级
    public float getZoomLevel() {
        return controller.getMapStatus().zoom;
    }

    //设置地图缩放等级
    public void setZoomLevel(float zoom) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(zoom);
        MapStatus newMapStatus = builder.build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(newMapStatus);
        controller.setMapStatus(mapStatusUpdate);
    }

    //放大
    public void zoomIn() {
        float zoom = getZoomLevel();
        zoom = zoom + 1;
        if (zoom > 18) zoom = 18;
        setZoomLevel(zoom);
    }

    //缩小
    public void zoomOut() {
        float zoom = getZoomLevel();
        zoom = zoom - 1;
        if (zoom < 5) zoom = 5;
        setZoomLevel(zoom);
    }

    //========================================  地图相关API  ========================================

    //========================================  定位相关API  ========================================

    //定位到指定位置
    public void locate(double latitude, double longitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        MapStatus newMapStatus = new MapStatus.Builder().target(latLng).build();
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newMapStatus(newMapStatus);
        controller.animateMapStatus(mapStatusUpdate);
    }

    //显示实时位置
    public void showRealLocation(double latitude, double longitude) {
        addMarkerAfterRemove(latitude, longitude, R.drawable.icon_map_marker_blue, "#realLocation", "#realLocation", overlay -> {
            return true;
        }, 0);
    }

    //========================================  定位相关API  ========================================

    //========================================  标记相关API  ========================================

    //添加标记
    public Overlay addMarker(double latitude, double longitude, @DrawableRes int drawableId, String overlayId, String overlayType, OnOverlayClick listener, Integer priority) {
        LatLng latLng = new LatLng(latitude, longitude);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(drawableId);
        OverlayOptions options = new MarkerOptions().position(latLng).icon(bitmapDescriptor);
        Overlay overlay = controller.addOverlay(options);
        overlay.setZIndex(priority == null ? 0 : priority);
        OverlayInfo overlayInfo = OverlayInfo.from(overlay, overlayId, overlayType);
        overlayInfo.listener = listener;
        overlayManager.add(overlayInfo);
        return overlay;
    }

    //添加标记，在此之前，先移除相同id和type的overlay
    public void addMarkerAfterRemove(double latitude, double longitude, @DrawableRes int drawableId, String overlayId, String overlayType, OnOverlayClick listener, Integer priority) {
        removeOverlay(overlayId, overlayType);
        addMarker(latitude, longitude, drawableId, overlayId, overlayType, listener, priority);
    }

    //========================================  标记相关API  ========================================

    //========================================  图层相关API  ========================================

    //移除Overlay
    public boolean removeOverlay(String overlayId, String overlayType) {
        Iterator<OverlayInfo> iterator = overlayManager.iterator();
        while (iterator.hasNext()) {
            OverlayInfo overlayInfo = iterator.next();
            //id相同则移除
            if (overlayId != null && Texts.equal(overlayId, overlayInfo.overlayId)) {
                controller.removeOverLays(Collections.asList(overlayInfo.overlay));
                overlayManager.remove(overlayInfo);
                return true;
            }
            //type相同则移除
            if (overlayType != null && Texts.equal(overlayType, overlayInfo.overlayType)) {
                controller.removeOverLays(Collections.asList(overlayInfo.overlay));
                overlayManager.remove(overlayInfo);
                return true;
            }
        }
        return false;
    }

    //查找Overlay对应的信息
    public OverlayInfo findOverlayInfo(Overlay overlay) {
        Iterator<OverlayInfo> iterator = overlayManager.iterator();
        while (iterator.hasNext()) {
            OverlayInfo overlayInfo = iterator.next();
            if (overlayInfo.overlay == overlay) return overlayInfo;
        }
        return null;
    }

    //========================================  图层相关API  ========================================

    //========================================  瓦片相关API  ========================================

    //使用矢量地图
    public void userVectorMap() {
        controller.setMapType(controller.MAP_TYPE_NORMAL);
    }

    //使用影像地图
    public void userSatelliteMap() {
        controller.setMapType(controller.MAP_TYPE_SATELLITE);
    }

    //========================================  瓦片相关API  ========================================

    //========================================  事件相关API  ========================================

    //监听点击事件
    public void onMapClick(OnMapClick onMapClick) {
        controller.setOnMapClickListener(new com.baidu.mapapi.map.BaiduMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                onMapClick.onMapClick(latLng.latitude, latLng.longitude);
            }

            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
    }

    //========================================  事件相关API  ========================================

    public interface OnMapClick {

        void onMapClick(double latitude, double longitude);
    }

    public interface OnOverlayClick {

        boolean onMapClick(Overlay overlay);
    }
}
