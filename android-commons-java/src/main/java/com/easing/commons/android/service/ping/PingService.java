package com.easing.commons.android.service.ping;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.time.Times;

import lombok.SneakyThrows;

public class PingService {

    public static long lastConnectedTime = 0L;

    public static void startPingInternet() {
        WorkThread.postByLoop("Ping检测网络连通性", () -> {
            String serverHost = CommonApplication.ctx.getServerHost();
            Process p = Runtime.getRuntime().exec("ping -c 1 -w 1 " + serverHost); //c是发送数据包次数，w是超时，单位为秒
            boolean reachable = p.waitFor() == 0;
            if (reachable)
                lastConnectedTime = Times.millisOfNow();
            Threads.sleep(1000);
        });
    }

    //判断互联网是否连接
    public static boolean isInternetConnected() {
        return Times.millisOfNow() - lastConnectedTime < 3000;
    }

    @SneakyThrows
    public static boolean ping(String ip) {
        Process p = Runtime.getRuntime().exec("ping -c 1 -w 1 " + ip); //c是发送数据包次数，w是超时，单位为秒
        boolean reachable = p.waitFor() == 0;
        return reachable;
    }
}

