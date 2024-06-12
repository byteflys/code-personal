package com.easing.commons.android.webmessage;

import java.util.Map;

@SuppressWarnings("all")
public class AircraftMessage extends WebMessage<AircraftMessage.AircraftData> {

//    public String id;   //消息id(自生成uuid)
    public String functionNo;   //所属功能编号(APP_PUSH_STATE：用户)
    public String type;

    public static class AircraftData {
        public String url;
        public String eventSourceId;
        public Integer eventSourceType;
        public String uuid;
        public Integer online;
        public String eventSourceName;
        public String eventCreateTime;
        public Double latitude;
        public Double longitude;
        //额外信息
        public Map<String,Object> extraInfo;
    }
}
