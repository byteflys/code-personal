package com.easing.commons.android.helper;

//通过IP和端口自动生成URL
public class UrlBuilder {

    public static String http(String host, Integer port) {
        return "http://" + host + ":" + port;
    }

    public static String https(String host, Integer port) {
        return "https://" + host + ":" + port;
    }

    public static String ws(String host, Integer port) {
        return "ws://" + host + ":" + port;
    }

    public static String wss(String host, Integer port) {
        return "wss://" + host + ":" + port;
    }

    public static String mqtt(String host, Integer port) {
        return "tcp://" + host + ":" + port;
    }

    public static String rtmp(String host, Integer port) {
        return "rtmp://" + host + ":" + port;
    }
}
