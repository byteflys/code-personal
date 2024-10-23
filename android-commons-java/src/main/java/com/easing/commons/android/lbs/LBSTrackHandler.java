package com.easing.commons.android.lbs;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LBSTrackHandler {

    //坐标分组
    //根据period分割轨迹
    //应用层可以通过关键字"bad"先取出差质量的坐标，剩下的即为高质量的坐标
    public static Map<String, List<LBSLocation>> group(List<LBSLocation> locations) {
        Map<String, List<LBSLocation>> trackMap = new LinkedHashMap();
        synchronized (locations) {
            for (LBSLocation location : locations) {
                if (location.period == null)
                    location.period = "default";
                if (!trackMap.containsKey(location.period))
                    trackMap.put(location.period, new LinkedList());
                List<LBSLocation> track = trackMap.get(location.period);
                track.add(location);
            }
        }
        return trackMap;
    }
}

