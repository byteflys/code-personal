package com.easing.commons.android.code.RemoteConsole;

@SuppressWarnings("all")
public interface IRemoteConsole {

    //发送控制台消息
    void send(RemoteConsoleMessage message);
}

