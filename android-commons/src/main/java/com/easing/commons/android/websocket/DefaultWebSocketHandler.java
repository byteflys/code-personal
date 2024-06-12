package com.easing.commons.android.websocket;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.webmessage.WebMessage;

import org.json.JSONObject;

import lombok.SneakyThrows;

//默认的消息处理器
public class DefaultWebSocketHandler implements WebSocketClient.MessageHandler {

    @Override
    public void onConnectFailed(WebSocketClient client) {
        Console.error("DefaultWebSocketHandler", "onConnectFailed", client.name);
    }

    @Override
    public void onConnected(WebSocketClient client) {
        Console.error("DefaultWebSocketHandler", "onConnected", client.name);
    }

    @Override
    public void onDisconnected(WebSocketClient client) {
        Console.error("DefaultWebSocketHandler", "onDisconnected", client.name);
    }

    @Override
    public void onError(WebSocketClient client, Exception e) {
        Console.error("DefaultWebSocketHandler", "onError", client.name);
    }

    @Override
    @SneakyThrows
    public void onMessage(WebSocketClient client, String data) {
        //发布收到的消息
        JSONObject webMessage = JSON.toJsonObject(data);
        if (!webMessage.has("type")) throw BizException.SERVER_ERROR;
        String type = webMessage.getString("type");
        EventBus.core.emit(type, data);
    }
}
