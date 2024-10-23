package com.easing.commons.android.mqtt;

import com.easing.commons.android.data.JSON;

import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultWillBuilder implements MqttClient.WillBuilder {

    @Override
    public String getTopic(MqttClient client) {
        return "mqtt_client_disconnected";
    }

    @Override
    public String getPayload(MqttClient client) {
        Map message = new LinkedHashMap();
        message.put("type", "mqtt_client_disconnected");
        message.put("data", client.name);
        return JSON.stringify(message);
    }
}
