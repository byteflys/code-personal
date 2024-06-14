package com.easing.commons.android.webmessage;

import com.easing.commons.android.clazz.BeanUtil;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("all")
public class WebMessageBuilder<T> {

    //消息类型
    String type;

    //回复码
    Integer code;

    //提示信息
    String message;

    //消息参数
    Map<String, Object> headMap = new LinkedHashMap();
    Map<String, Object> formMap = new LinkedHashMap();
    Map<String, Object> extraMap = new LinkedHashMap();

    //如果使用了body，则将body对象直接设置为data
    //否则通过formMap构建一个JSON对象作为data
    Boolean useBody = false;
    T body;

    //JSON序列化时，是否保留Null字段
    Boolean stringifyWithNull = false;

    public static WebMessageBuilder create() {
        WebMessageBuilder packet = new WebMessageBuilder();
        return packet;
    }

    public static WebMessageBuilder create(String type) {
        WebMessageBuilder packet = new WebMessageBuilder();
        packet.type = type;
        return packet;
    }

    public WebMessageBuilder type(String type) {
        this.type = type;
        return this;
    }

    public WebMessageBuilder topic(String topic) {
        extraMap.put("topic", topic);
        return this;
    }

    public WebMessageBuilder head(String key, Object value) {
        headMap.put(key, value);
        return this;
    }

    public WebMessageBuilder head(Map<String, Object> map) {
        headMap.putAll(map);
        return this;
    }

    public WebMessageBuilder head(Object entity) {
        BeanUtil.copyAttribute(entity, headMap);
        return this;
    }

    public WebMessageBuilder form(String key, Object value) {
        formMap.put(key, value);
        return this;
    }

    public WebMessageBuilder form(Map<String, Object> map) {
        formMap.putAll(map);
        return this;
    }

    public WebMessageBuilder form(Object entity) {
        BeanUtil.copyAttribute(entity, formMap);
        return this;
    }

    public WebMessageBuilder stringifyWithNull(boolean keepNull) {
        this.stringifyWithNull = keepNull;
        return this;
    }

    public WebMessageBuilder body(T body) {
        this.body = body;
        this.useBody = true;
        return this;
    }

    public WebMessageBuilder code(int code) {
        this.code = code;
        return this;
    }

    public WebMessageBuilder message(String message) {
        this.message = message;
        return this;
    }

    public WebMessageBuilder from(String from) {
        extraMap.put("from", from);
        return this;
    }

    public WebMessageBuilder to(String to) {
        extraMap.put("to", to);
        return this;
    }

    public WebMessageBuilder replyTo(String replyTo) {
        extraMap.put("replyTo", replyTo);
        return this;
    }

    public WebMessageBuilder deliverRoute(String[] deliverRoute) {
        extraMap.put("deliverRoute", deliverRoute);
        return this;
    }

    public WebMessageBuilder mac(String mac) {
        extraMap.put("mac", mac);
        return this;
    }

    public WebMessage build() {
        WebMessage webMessage = new WebMessage();
        webMessage.eventType = type;
        webMessage.code = code;
        webMessage.message = message;
        webMessage.stringifyWithNull = stringifyWithNull;
        //设置data
        if (useBody)
            webMessage.data = body;
        else
            webMessage.data = formMap;
        //设置额外参数
        if (extraMap.containsKey("from"))
            webMessage.from = (String) extraMap.get("from");
        if (extraMap.containsKey("to"))
            webMessage.to = (String) extraMap.get("to");
        if (extraMap.containsKey("replyTo"))
            webMessage.replyTo = (String) extraMap.get("replyTo");
        if (extraMap.containsKey("deliverRoute"))
            webMessage.deliverRoute = (String[]) extraMap.get("deliverRoute");
        if (extraMap.containsKey("mac"))
            webMessage.mac = (String) extraMap.get("mac");
        if (extraMap.containsKey("topic"))
            webMessage.topic = (String) extraMap.get("topic");
        //设置头部参数
        webMessage.head.putAll(headMap);
        return webMessage;
    }


}
