package com.easing.commons.android.socket;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.thread.WorkThread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class UdpSocketClient {

    public String name;

    DatagramSocket socket;
    EventHandler eventHandler;
    ExecutorService sender = Executors.newSingleThreadExecutor();

    boolean close = false;

    @SneakyThrows
    public static UdpSocketClient init(String name, String ip, Integer port) {
        UdpSocketClient client = new UdpSocketClient();
        if (ip != null && port != null)
            client.socket = new DatagramSocket(port, InetAddress.getByName(ip));
        else if (port != null)
            client.socket = new DatagramSocket(port);
        else
            client.socket = new DatagramSocket();
        client.name = name;
        return client;
    }

    //开始读数据
    public UdpSocketClient read() {
        WorkThread.postByLoop("#UDP读数据线程", () -> {
            if (close)
                throw BizException.THREAD_NORMAL_EXIT;
            byte[] buffer = new byte[1024 * 1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);
            byte[] receiveBytes = Arrays.copyOf(buffer, packet.getLength());
            long timestamp = System.currentTimeMillis();
            String ip = packet.getAddress().getHostAddress();
            int port = packet.getPort();
            if (eventHandler != null)
                eventHandler.onReceive(receiveBytes, timestamp, ip, port);
        });
        return this;
    }

    //设置数据接收监听器
    public UdpSocketClient eventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        return this;
    }

    //关闭
    public void close() {
        this.close = true;
        this.socket = null;
        this.eventHandler = null;
        this.sender.shutdown();
        this.sender = null;
    }

    //发送单播消息
    @SneakyThrows
    public UdpSocketClient sendUnicast(byte[] data, String ip, int port) {
        sender.submit(() -> {
            try {
                InetAddress address = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
            } catch (Exception e) {
                Console.error(e);
            }
        });
        return this;
    }

    //发送组播消息
    //组播地址范围为224.0.0.0~239.255.255.255，发送端和接收端地址一样即可接收
    @SneakyThrows
    public UdpSocketClient sendMulticast(byte[] data, String ip, int port) {
        sender.submit(() -> {
            try {
                InetAddress address = InetAddress.getByName(ip);
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
            } catch (Exception e) {
                Console.error(e);
            }
        });
        return this;
    }

    //发送广播消息
    @SneakyThrows
    public UdpSocketClient sendBroadcast(byte[] data, int port) {
        sender.submit(() -> {
            try {
                InetAddress address = InetAddress.getByName("255.255.255.255");
                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);
            } catch (Exception e) {
                Console.error(e);
            }
        });
        return this;
    }

    //Socket回调
    public interface EventHandler {

        void onReceive(byte[] packet, long timestamp, String ip, int port);
    }
}

