package com.easing.commons.android.socket;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("all")
public class TcpSocketClient {

    String ip;
    int port;

    Socket socket;
    EventHandler eventHandler;
    ExecutorService sender = Executors.newSingleThreadExecutor();

    boolean close = false;

    //设置IP和端口
    public static TcpSocketClient init(String ip, int port) {
        TcpSocketClient client = new TcpSocketClient();
        client.ip = ip;
        client.port = port;
        return client;
    }

    //设置数据接收监听器
    public TcpSocketClient eventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    //连接
    //连接失败时默认会自动重连
    public void connect() {
        WorkThread.post(() -> {
            try {
                InetSocketAddress address = new InetSocketAddress(ip, port);
                socket = new Socket();
                socket.connect(address, 1000);
                //连接成功
                if (eventHandler != null)
                    eventHandler.onConnect();
                //开启读数据线程，即监听接收数据
                Socket tempSocket = socket;
                WorkThread.post(() -> {
                    while (!close && tempSocket.isConnected()) {
                        byte[] buffer = new byte[1024 * 1024];
                        int length = socket.getInputStream().read(buffer);
                        byte[] fragment = Arrays.copyOf(buffer, length);
                        long timestamp = System.currentTimeMillis();
                        if (eventHandler != null)
                            eventHandler.onReceive(fragment, timestamp);
                    }
                });
            } catch (Exception e) {
                Console.error(e);
                if (eventHandler != null)
                    eventHandler.onConnectFail();
                //连接失败后，1秒后自动重连
                Threads.sleep(500);
                connect();
            }
        });
    }

    //发送数据
    public void send(byte[] packet) {
        //用一个单例线程池来提交写入请求
        //这样可以保证不占用主线程，同时保证写入请求的有序性
        sender.submit(() -> {
            if (close || socket == null || !socket.isConnected())
                return;
            try {
                socket.getOutputStream().write(packet);
            } catch (Exception e) {
                Console.error(e);
            }
        });
    }

    //关闭
    //即便Socket连接失败，也需要手动关闭，否则会一直尝试重连
    public void close() {
        this.close = true;
        this.socket = null;
        this.eventHandler = null;
        this.sender.shutdown();
        this.sender = null;
    }

    //Socket回调
    public interface EventHandler {

        void onConnect();

        void onConnectFail();

        void onReceive(byte[] packet, long timestamp);
    }
}
