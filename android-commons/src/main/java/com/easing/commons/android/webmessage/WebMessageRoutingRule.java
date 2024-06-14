package com.easing.commons.android.webmessage;

public class WebMessageRoutingRule {

    public String eventType;
    public String explain = "未知操作";
    public String url;

    public String protocol;
    public Boolean ssl;

    //用于MQTT等需要登录的协议
    public String username;
    public String password;

    @Override
    public String toString() {
        return explain + "  " + eventType + "  " + url;
    }

    public WebMessageRoutingRule autoDecideProtocol() {
        if (url.startsWith("http://")) {
            protocol = WebProtocol.HTTP;
            ssl = false;
        } else if (url.startsWith("https://")) {
            protocol = WebProtocol.HTTP;
            ssl = true;
        } else if (url.startsWith("ws://")) {
            protocol = WebProtocol.WEBSOCKET;
            ssl = false;
        } else if (url.startsWith("wss://")) {
            protocol = WebProtocol.WEBSOCKET;
            ssl = true;
        } else if (url.startsWith("tcp://")) {
            protocol = WebProtocol.MQTT;
        }
        return this;
    }
}
