package com.easing.commons.android.websocket;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.time.Times;

import org.java_websocket.WebSocket;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class WebSocketServer {

    org.java_websocket.server.WebSocketServer server;

    MessageHandler handler;

    Map<Integer, WebSocket> clientMap = new ConcurrentHashMap();

    int port = 18001;

    boolean useSSL = false;

    @SneakyThrows
    public WebSocketServer(int port) {
        this.port = port;
        //创建服务器
        List<Draft> draftList = new ArrayList();
        draftList.add(new Draft_6455());
        server = new org.java_websocket.server.WebSocketServer(new InetSocketAddress("0.0.0.0", port), draftList) {

            @Override
            public void onStart() {
                Console.info("WebSocketServer", "websocket server started");
                if (handler != null)
                    handler.onStart();
            }

            @Override
            public void onOpen(WebSocket socket, ClientHandshake handshake) {
                Console.info("WebSocketServer", "websocket client connected", socket.hashCode());
                clientMap.put(socket.hashCode(), socket);
                if (handler != null)
                    handler.onConnected(socket);
            }

            @Override
            public void onClose(WebSocket socket, int code, String reason, boolean remote) {
                Console.info("WebSocketServer", "websocket client disconnected", socket.hashCode());
                clientMap.remove(socket.hashCode());
                if (handler != null)
                    handler.onDisconnected(socket);
            }

            @Override
            public void onError(WebSocket socket, Exception e) {
                Console.info("WebSocketServer", "websocket exception", socket.hashCode());
            }

            @Override
            public void onMessage(WebSocket socket, String message) {
                Console.info("WebSocketServer", "get a websocket message", socket.hashCode());
                if (handler != null)
                    handler.onMessage(socket, message);
                //TODO => 测试代码
                Map data = new LinkedHashMap();
                data.put("id", Times.now());
                data.put("type", "response_client");
                data.put("time", Texts.random());
                sendMessage(socket, data);
            }
        };
        //定时检测连接是否断开
        server.setConnectionLostTimeout(1);
        //复用已被占用的端口
        server.setReuseAddr(true);
    }

    //是否使用SSL安全加密
    public WebSocketServer useSSL(boolean useSSL) {
        this.useSSL = useSSL;
        return this;
    }

    //设置消息处理器
    public WebSocketServer handler(MessageHandler handler) {
        this.handler = handler;
        return this;
    }

    //启动服务
    @SneakyThrows
    public void start() {
        //使用SSL
        if (useSSL) {
            DefaultSSLWebSocketServerFactory factory = WebSocketServerSSL.sslFactory();
            server.setWebSocketFactory(factory);
            Console.info("WebSocketServer", "add SSL support");
        }
        //启动服务
        server.start();
        //打印服务地址
        String protocol = useSSL ? "wss://" : "ws://";
        String url = protocol + InetAddress.getLocalHost().getHostAddress() + ":" + port;
        Console.info("WebSocketServer", "WebSocket服务启动成功", url);
    }

    //向客户端发送消息
    public void sendMessage(WebSocket client, Object data) {
        if (data instanceof String)
            client.send(String.valueOf(data));
        else
            client.send(JSON.stringify(data));
    }

    public interface MessageHandler {

        default void onStart() {
        }

        default void onConnected(WebSocket client) {
        }

        default void onDisconnected(WebSocket client) {
        }

        void onMessage(WebSocket client, String data);
    }
}
