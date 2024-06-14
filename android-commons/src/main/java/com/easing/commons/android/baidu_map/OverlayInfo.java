package com.easing.commons.android.baidu_map;

import com.baidu.mapapi.map.Overlay;

public class OverlayInfo {

    public Overlay overlay;

    public String overlayId;
    public String overlayType;

    public BaiduMap.OnOverlayClick listener;

    public static OverlayInfo from(Overlay overlay, String overlayId, String overlayType) {
        OverlayInfo overlayInfo = new OverlayInfo();
        overlayInfo.overlay = overlay;
        overlayInfo.overlayId = overlayId;
        overlayInfo.overlayType = overlayType;
        return overlayInfo;
    }
}
