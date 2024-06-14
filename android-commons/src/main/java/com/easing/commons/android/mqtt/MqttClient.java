package com.easing.commons.android.mqtt;

import android.os.NetworkOnMainThreadException;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.data.Jsonable;
import com.easing.commons.android.event.CommonEvents;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.thread.ReusableThreadPool;
import com.easing.commons.android.thread.SingleThreadPool;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.time.LaunchTime;
import com.easing.commons.android.time.Times;
import com.easing.commons.android.ui.dialog.TipBox;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.internal.ClientState;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class MqttClient {

    volatile String clientId = Texts.random();

    final Object clientLock = new Object();

    volatile String server = "tcp://www.aodun2017.com:1883";
    volatile String username = "admin";
    volatile String password = "password";

    volatile String name;
    volatile org.eclipse.paho.mqttv5.client.MqttClient core;
    volatile MqttConnectionOptions option;

    volatile List<String> topics = new ArrayList();

    volatile MessageHandler handler;
    volatile WillBuilder willBuilder;

    //客户端是否已销毁
    volatile boolean disposed = false;

    //最近一次会话时间
    //由于WebSocket库的连接状态检测不准，所以最好是通过与服务器进行双向心跳来判断连通性
    //20s内有过心跳，则视为已连接
    volatile long lastSessionTime = 0L;
    volatile long lastPublishFailTime = 0L;

    final SingleThreadPool singleThreadPool = SingleThreadPool.newInstance();
    final ReusableThreadPool reusableThreadPool = ReusableThreadPool.newInstance();

    private MqttClient() {}

    public static MqttClient build(String server, String username, String password) {
        MqttClient client = new MqttClient();
        client.clientId = Texts.random();
        client.server = server;
        client.username = username;
        client.password = password;
        client.name = client.getMqttName();
        client.topics.add(client.clientId);
        client.topics.add("mqtt_test");
        return client;
    }

    public String getMqttName() {
        return "@MQTT-" + Times.now(Times.FORMAT_12) + "-" + clientId;
    }

    public MqttClient handler(MessageHandler handler) {
        this.handler = handler;
        return this;
    }

    public MqttClient will(WillBuilder willBuilder) {
        this.willBuilder = willBuilder;
        return this;
    }

    protected void buildOption() {
        option = new MqttConnectionOptions();
        option.setUserName(username);
        option.setPassword(password.getBytes(StandardCharsets.UTF_8));
        //连接超时，超时未成功，则抛出异常
        option.setConnectionTimeout(30);
        //通过服务端心跳判断连接是否断开，次数关闭该功能，通过自己发心跳来判断
        option.setKeepAliveInterval(0);
        //不自动重连，自己重连更加精准
        option.setAutomaticReconnect(false);
        //设置单个包最大体积
        option.setMaximumPacketSize(10 * 1024 * 1024L);
        //重连之后，不恢复之前的会话状态
        option.setCleanStart(true);
        //设置掉线遗言
        if (willBuilder != null) {
            String topic = willBuilder.getTopic(this);
            String payload = willBuilder.getPayload(this);
            MqttMessage mqttMessage = new MqttMessage(payload.getBytes(StandardCharsets.UTF_8));
            option.setWill(topic, mqttMessage);
        }
    }

    @SneakyThrows
    protected void buildClient() {
        //创建客户端
        name = getMqttName();
        MemoryPersistence persistence = new MemoryPersistence();
        final org.eclipse.paho.mqttv5.client.MqttClient client = new org.eclipse.paho.mqttv5.client.MqttClient(server, name, persistence);
        //设置MQTT回调
        client.setCallback(new MqttCallback() {

            @Override
            public void disconnected(MqttDisconnectResponse response) {
                Console.info("MQTT", "Disconnected", server);
                handler.onDisconnected(MqttClient.this, response.getReturnCode(), response.getReasonString());
            }

            @Override
            public void mqttErrorOccurred(MqttException e) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                lastSessionTime = LaunchTime.time;
                if (client != core)
                    return;
                String message = new String(mqttMessage.getPayload());
                if (message.equals("heartbeat"))
                    return;
                if (topic.equals("mqtt_test")) {
                    InternalMessage internalMessage = JSON.parse(message, InternalMessage.class);
                    internalMessage.print(MqttClient.this);
                    return;
                }
                try {
                    if (handler != null)
                        handler.onMessage(topic, message);
                } catch (Throwable e) {
                    Console.info("MQTT", "Error on Handling Message", e.getClass().getSimpleName());
                    if (handler != null)
                        handler.onMessageError(MqttClient.this, e);
                }
            }

            @Override
            public void deliveryComplete(IMqttToken token) {

            }

            @Override
            public void connectComplete(boolean var1, String var2) {

            }

            @Override
            public void authPacketArrived(int var1, MqttProperties properties) {

            }
        });
        client.setTimeToWait(15 * 1000L);
        //使用新的客户端
        core = client;
        //关闭日志
        Logger.getLogger(ClientState.class.getName()).setLevel(Level.OFF);
    }

    //开启线程，定时重连和发送心跳
    @SneakyThrows
    public void connect() {
        //定时连接MQTT服务器
        WorkThread.postByInterval("#MQTT重连线程", () -> {
            final MqttClient temp = MqttClient.this;
            //客户端已销毁，则关闭重连
            if (disposed)
                throw BizException.THREAD_NORMAL_EXIT;
            //已连接，不处理
            if (connected())
                return;
            //未连接则重连
            try {
                Console.info("MQTT", "Connect Start", server);
                if (handler != null)
                    handler.onConnectStart(MqttClient.this);
                synchronized (clientLock) {
                    close();
                    buildOption();
                    buildClient();
                }
                core.connect(option);
                lastSessionTime = LaunchTime.time;
                Console.info("MQTT", "Connect Success", server);
                if (handler != null)
                    handler.onConnected(MqttClient.this);
            } catch (Throwable e) {
                Console.info("MQTT", "Connect Fail", server, e.getClass().getSimpleName());
                if (handler != null)
                    handler.onConnectFail(MqttClient.this, e);
            }
            try {
                for (String topic : topics)
                    core.subscribe(topic, QOS.EXACTLY_ONCE);
                Console.info("MQTT", "Subscribe Success", server, topics);
            } catch (Throwable e) {
                Console.info("MQTT", "Subscribe Fail", server, e.getClass().getSimpleName());
                if (handler != null)
                    handler.onSubscribeError(MqttClient.this, e);
            }
        }, 60 * 1000L, 0);
        //定时发送MQTT心跳
        WorkThread.postByLoop("#MQTT心跳线程", () -> {
            if (disposed)
                throw BizException.THREAD_NORMAL_EXIT;
            try {
                if (connected())
                    core.publish(clientId, "heartbeat".getBytes(StandardCharsets.UTF_8), QOS.AT_LEAST_ONCE, false);
            } catch (Throwable e) {
                Console.info("MQTT", "Send Heartbeat Fail", e.getClass().getSimpleName());
            }
            //休息20秒再发心跳
            Threads.sleep(20 * 1000L);
        });
    }

    //断开MQTT连接
    @SneakyThrows
    public void disconnect() {
        synchronized (clientLock) {
            if (core.isConnected())
                core.disconnectForcibly();
        }
    }

    //强制重连
    @SneakyThrows
    public void forceReconnect(String newServer) {
        if (newServer != null)
            server = newServer;
        WorkThread.post(this::disconnect);
    }

    //关闭客户端
    @SneakyThrows
    protected void close() {
        synchronized (clientLock) {
            Console.info("MQTT", "Close", server);
            if (core != null) {
                handler.onCloseStart(MqttClient.this);
                if (core.isConnected())
                    core.disconnectForcibly(100L, 100L, false);
                core.close(true);
                handler.onClosed(MqttClient.this);
                core = null;
            }
        }
    }

    //销毁MQTTClient
    @SneakyThrows
    public void dispose() {
        disposed = true;
        MqttManager.remove(server);
        close();
    }

    //获取服务地址
    public String server() {
        return server;
    }

    //判断MQTT是否连接
    public boolean connected() {
        synchronized (clientLock) {
            return core != null && core.isConnected() && isSessionActive();
        }
    }

    //会话是否处于激活状态，2分钟内有过心跳，则视为激活
    public boolean isSessionActive() {
        boolean active = LaunchTime.time - lastSessionTime < 2 * 60 * 1000;
        return active;
    }

    //判断MQTT是否销毁
    public boolean disposed() {
        return disposed;
    }

    //获取已订阅主题
    public List<String> getSubscribedTopics() {
        return topics;
    }

    @SneakyThrows
    public String subscribe(String topic) {
        if (topics.contains(topic))
            return CommonEvents.SUCCESS;
        topics.add(topic);
        //客户端已关闭
        if (disposed)
            return CommonEvents.CLIENT_CLOSED;
        //客户端未连接
        if (!core.isConnected())
            return CommonEvents.CLIENT_UNCONNECTED;
        //订阅主题
        try {
            core.subscribe(topic, QOS.EXACTLY_ONCE);
            return CommonEvents.SUCCESS;
        } catch (Throwable e) {
            //程序发生未知异常
            Console.info("MQTT", "Subscribe Topic Fail", e.getClass().getSimpleName());
            if (handler != null)
                handler.onSubscribeError(MqttClient.this, e);
            return CommonEvents.ERROR;
        }
    }

    //在当前线程发送MQTT消息
    @SneakyThrows
    public String publish(String topic, String message) {
        if (Threads.isMainThread()) {
            TipBox.tipInCenterLong("禁止在主线程执行网络请求代码");
            throw new NetworkOnMainThreadException();
        }
        //客户端已关闭
        if (disposed)
            return CommonEvents.CLIENT_CLOSED;
        //客户端未连接
        if (!connected())
            return CommonEvents.CLIENT_UNCONNECTED;
        //发送消息
        try {
            byte[] payload = message.getBytes(StandardCharsets.UTF_8);
            core.publish(topic, payload, QOS.EXACTLY_ONCE, false);
            return CommonEvents.SUCCESS;
        } catch (Throwable e) {
            //程序发生未知异常
            Console.info("MQTT", "Publish Fail", e.getClass().getSimpleName());
            if (handler != null)
                handler.onPublishError(MqttClient.this, e);
            //刚连接成功时，数据可能会发送失败
            //但如果是连接成功5秒后，仍然发送失败，则表示客户端可能已经断开连接
            if (LaunchTime.time - lastPublishFailTime > 5 * 1000L)
                if (LaunchTime.time - lastSessionTime > 5 * 1000L) {
                    lastPublishFailTime = LaunchTime.time;
                    close();
                }
            return CommonEvents.ERROR;
        }
    }

    //在新的线程发送MQTT消息
    public void publishInConcurrent(String topic, String message, OnPublishResult onPublishResult) {
        reusableThreadPool.submit(() -> {
            String result = publish(topic, message);
            if (onPublishResult != null)
                onPublishResult.onPublishResult(result);
        });
    }

    //排队发送MQTT消息
    public void publishInQueue(String topic, String message, OnPublishResult onPublishResult) {
        singleThreadPool.submit(() -> {
            String result = publish(topic, message);
            if (onPublishResult != null)
                onPublishResult.onPublishResult(result);
        });
    }

    //在当前线程发送MQTT消息
    @SneakyThrows
    public String publish(String topic, Object message) {
        if (message instanceof String || message instanceof JSONObject)
            return publish(topic, String.valueOf(message));
        return publish(topic, JSON.stringify(message));
    }

    //在新的线程发送MQTT消息
    public void publishInConcurrent(String topic, Object message, OnPublishResult onPublishResult) {
        reusableThreadPool.submit(() -> {
            String result = publish(topic, message);
            if (onPublishResult != null)
                onPublishResult.onPublishResult(result);
        });
    }

    //排队发送MQTT消息
    public void publishInQueue(String topic, Object message, OnPublishResult onPublishResult) {
        singleThreadPool.submit(() -> {
            String result = publish(topic, message);
            if (onPublishResult != null)
                onPublishResult.onPublishResult(result);
        });
    }

    public static class InternalMessage implements Jsonable {

        public String type;
        public String from;
        public String to;
        public String data;
        public Boolean print;

        public void print(MqttClient client) {
            if (type == null)
                return;
            if (print == null)
                return;
            if (type.equals("online_test")) {
                TipBox.tipInCenter("MQTT在线状态检测 " + from);
            }
            if (type.equals("subscribe_test")) {
                String subscribeInfo = Collections.toString(client.topics, topic -> topic, "\n");
                TipBox.tipInCenter("MQTT订阅主题检测\n" + subscribeInfo);
            }
        }
    }

    public interface OnPublishResult {

        void onPublishResult(String result);
    }

    public interface MessageHandler {

        void onMessage(String topic, String message);

        default void onConnectStart(MqttClient client) {
        }

        default void onConnected(MqttClient client) {
        }

        default void onConnectFail(MqttClient client, Throwable e) {
        }

        default void onDisconnected(MqttClient client, int code, String message) {
        }

        default void onCloseStart(MqttClient client) {
        }

        default void onClosed(MqttClient client) {
        }

        default void onSubscribeError(MqttClient client, Throwable e) {
        }

        default void onPublishError(MqttClient client, Throwable e) {
        }

        default void onMessageError(MqttClient client, Throwable e) {
        }
    }

    public interface WillBuilder {

        String getTopic(MqttClient client);

        String getPayload(MqttClient client);
    }
}

