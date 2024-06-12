package com.easing.commons.android.code.RemoteConsole;

@SuppressWarnings("all")
public class RemoteConsole {

    static volatile IRemoteConsole impl;

    static volatile boolean enabled = false;

    public static volatile String clientID = "";
    public static volatile String projectName = "";

    //设置实现类
    public static void impl(IRemoteConsole impl) {
        RemoteConsole.impl = impl;
    }

    //开启或关闭远程控制台
    public static void enable(boolean enabled) {
        RemoteConsole.enabled = enabled;
    }

    //判断控制台是否打开
    public static boolean enabled() {
        return enabled;
    }

    //发送远程消息
    public static void sendRemoteMessage(RemoteConsoleMessage message) {
        if (!enabled)
            return;
        if (impl == null)
            return;
        impl.send(message);
    }
}

