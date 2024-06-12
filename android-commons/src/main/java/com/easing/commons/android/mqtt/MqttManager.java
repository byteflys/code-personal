package com.easing.commons.android.mqtt;

import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.webmessage.WebMessageRouter;
import com.easing.commons.android.webmessage.WebMessageRoutingRule;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class MqttManager {

    static final Map<String, MqttClient> clientMap = new HashMap();

    //创建MqttClient
    public static MqttClient get(String url) {
        return clientMap.get(url);
    }

    //创建MqttClient
    public static MqttClient get(String url, String username, String password, MqttClient.MessageHandler handler, MqttClient.WillBuilder willBuilder) {
        if (clientMap.containsKey(url))
            return clientMap.get(url);
        MqttClient client = MqttClient.build(url, username, password);
        client.handler(handler == null ? new DefaultMqttHandler() : handler);
        client.will(willBuilder == null ? new DefaultWillBuilder() : willBuilder);
        clientMap.put(url, client);
        WorkThread.post(client::connect);
        return client;
    }

    //通过路由框架，自动找到服务地址，并创建MQTT客户端
    public static MqttClient getByRouter(String type, MqttClient.MessageHandler handler, MqttClient.WillBuilder willBuilder) {
        WebMessageRoutingRule rule = WebMessageRouter.find(type);
        MqttClient client = get(rule.url, rule.username, rule.password, handler, willBuilder);
        return client;
    }

    //移除MqttClient
    public static void remove(String url) {
        if (!clientMap.containsKey(url)) return;
        clientMap.get(url).handler = null;
        clientMap.remove(url);
    }

}
