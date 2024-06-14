package com.easing.commons.android.mqtt;

import android.util.Log;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.helper.exception.ServerError;

import org.eclipse.paho.android.service.BuildConfig;
import org.json.JSONObject;

import lombok.SneakyThrows;

//默认的MQTT消息处理器
//该消息处理器直接通过EventBus发布MqttMessage
public class DefaultMqttHandler implements MqttClient.MessageHandler {

    private static final String MQTT_LOG = "matt_message";

    @Override
    @SneakyThrows
    public void onMessage(String topic, String data) {
        //发布收到的消息
        Console.info("DefaultMqttHandler", "onMessage topic =" + topic + "  data =" + data);
        JSONObject webMessage = JSON.toJsonObject(data);
        webMessage.put("topic", topic);
        if (!webMessage.has("type"))
            throw ServerError.of("消息中不包含type字段");
        String type = webMessage.getString("type");
        EventBus.core.emit(type, webMessage.toString(), topic);

        if (BuildConfig.DEBUG)
            Log.i(MQTT_LOG, "type = " + type + " \n data = " + data);
    }

    @Override
    public void onConnected(MqttClient client) {
        Console.info("DefaultMqttHandler", "onConnected");
    }

    @Override
    public void onConnectFail(MqttClient client, Throwable e) {
        Console.info("DefaultMqttHandler", "onConnectFail");
    }

    @Override
    public void onDisconnected(MqttClient client, int code, String message) {
        Console.info("DefaultMqttHandler", "onDisconnected");
    }

    @Override
    public void onSubscribeError(MqttClient client, Throwable e) {
        Console.info("DefaultMqttHandler", "onSubscribeFail", e);
    }

    @Override
    public void onPublishError(MqttClient client, Throwable e) {
        Console.info("DefaultMqttHandler", "onPublishFail", e);
    }

    @Override
    public void onMessageError(MqttClient client, Throwable e) {
        Console.info("DefaultMqttHandler", "onError", e);
    }
}
