package com.easing.commons.android.websocket;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.event.CommonEvents;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.http.SSL;
import com.easing.commons.android.thread.StackTrace;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;

import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.security.SecureRandom;
import java.util.HashMap;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocketFactory;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class WebSocketClient {

    static final int CONNECT_TIMEOUT = 2000;

    String url;
    String name;
    MessageHandler handler;
    org.java_websocket.client.WebSocketClient client;

    boolean reconnet = true;
    boolean disposed = false;
    boolean connected = false;

    public WebSocketClient(String url) {
        this.name = StackTrace.callerInfo();
        this.url = url;
        buildWebSocketClient();
    }

    @SneakyThrows
    private void buildWebSocketClient() {
        URI uri = new URI(url);
        org.java_websocket.client.WebSocketClient client = new org.java_websocket.client.WebSocketClient(uri, new Draft_6455(), new HashMap(), CONNECT_TIMEOUT) {

            @Override
            @SneakyThrows
            public void onOpen(ServerHandshake handshakedata) {
                Console.info(WebSocketClient.class, "websocket client connected");
                connected = true;
                if (handler != null)
                    handler.onConnected(WebSocketClient.this);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                //回调设计不合理，连接失败也可能触发onClose方法
                if (!connected) {
                    Console.info(WebSocketClient.class, "websocket client connect failed");
                    if (handler != null)
                        handler.onConnectFailed(WebSocketClient.this);
                    return;
                }

                //连接断开
                connected = false;
                Console.info(WebSocketClient.class, "websocket client disconnected");
                if (handler != null)
                    handler.onDisconnected(WebSocketClient.this);
            }

            @Override
            public void onError(Exception e) {
                boolean b1 = e instanceof SSLHandshakeException;
                boolean b2 = e instanceof ConnectException;
                boolean b3 = e instanceof IOException;
                boolean ioException = b1 || b2 || b3;
                //IOException会触发onClose回调，所以此处不必重复处理
                if (!ioException) {
                    Console.info(WebSocketClient.class, "websocket error");
                    if (handler != null)
                        handler.onError(WebSocketClient.this, e);
                }
            }

            @Override
            @SneakyThrows
            public void onMessage(String data) {
                Console.info(WebSocketClient.class, "get a websocket message");
                if (handler != null)
                    handler.onMessage(WebSocketClient.this, data);
            }
        };
        //无通讯自动断开连接
        client.setConnectionLostTimeout(10 * 60);
        //使用SSL
        if (url.startsWith("wss://")) {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, SSL.trustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            client.setSocket(factory.createSocket());
            Console.info(WebSocketClient.class, "add SSL support");
        }
        //设置client
        WebSocketClient.this.client = client;
    }

    //设置消息处理器
    public WebSocketClient handler(MessageHandler handler) {
        this.handler = handler;
        return this;
    }

    //设置是否自动重连
    public WebSocketClient reconnect(boolean reconnect) {
        this.reconnet = reconnect;
        return this;
    }

    //连接服务端
    @SneakyThrows
    public WebSocketClient connect() {
        client.connect();
        //自动重连
        WorkThread.post(() -> {
            while (true) {
                if (disposed)
                    throw BizException.THREAD_NORMAL_EXIT;
                Threads.sleep(CONNECT_TIMEOUT + 1000);
                if (!disposed && reconnet && !client.isOpen()) {
                    //WebSocketClient有一个BUG，当客户端先于服务端启动时，SSL验证会一直异常
                    //所以我们只能重新创建一个新的WebSocketClient来重连
                    //当然，这个异常在实际应用中基本不会出现，因为一般WebSocketServer都是一直处于运行状态的
                    if (!client.isClosed())
                        client.close();
                    buildWebSocketClient();
                    client.connect();
                }
            }
        });
        return this;
    }

    //向服务端发送消息
    public String sendMessage(Object data) {
        //客户端已关闭
        if (disposed)
            return CommonEvents.CLIENT_CLOSED;
        //客户端未连接
        if (!connected)
            return CommonEvents.CLIENT_UNCONNECTED;
        //发送消息
        try {
            if (data instanceof String || data instanceof JSONObject)
                client.send(String.valueOf(data));
            else
                client.send(JSON.stringify(data));
            return CommonEvents.SUCCESS;
        } catch (Throwable e) {
            //程序发生未知异常
            return CommonEvents.ERROR;
        }
    }

    //销毁WebSocketClient
    public void dispose() {
        disposed = true;
        WebSocketManager.remove(url);
        if (client.isOpen())
            client.close(1001, "client actively close the connection");
    }

    public interface MessageHandler {

        default void onConnectFailed(WebSocketClient client) {
        }

        default void onConnected(WebSocketClient client) {
        }

        default void onDisconnected(WebSocketClient client) {
        }

        default void onError(WebSocketClient client, Exception e) {
        }

        void onMessage(WebSocketClient client, String data);
    }

}
