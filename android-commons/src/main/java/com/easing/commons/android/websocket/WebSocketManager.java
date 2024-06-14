package com.easing.commons.android.websocket;

import com.easing.commons.android.thread.WorkThread;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
public class WebSocketManager {

    static final Map<String, WebSocketClient> clientMap = new HashMap();

    //创建WebSocketClient
    public static WebSocketClient get(String url) {
        if (clientMap.containsKey(url))
            return clientMap.get(url);
        WebSocketClient client = new WebSocketClient(url);
        clientMap.put(url, client);
        //延时0.5秒执行，这样就有时间去设置监听器，是否重连等选项
        WorkThread.postLater(client::connect, 500);
        return client;
    }

    //移除WebSocketClient
    public static void remove(String url) {
        if (!clientMap.containsKey(url)) return;
        clientMap.get(url).handler = null;
        clientMap.remove(url);
    }

}
