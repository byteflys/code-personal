package com.easing.commons.android.code.RemoteConsole;

import java.util.LinkedList;

@SuppressWarnings("all")
public class RemoteConsoleDisplayer {

    protected String consoleProject = "";
    protected String consoleTag = "";
    protected int consoleLevel = RemoteConsoleMessage.LEVEL_ALL;

    protected LinkedList<RemoteConsoleMessage> messageBuffer = new LinkedList();

    protected String targetDevice = null;

    //接收新的消息
    public final void receive(RemoteConsoleMessage message) {
        synchronized (RemoteConsoleDisplayer.class) {
            messageBuffer.add(message);
            if (messageBuffer.size() > 100)
                messageBuffer.removeFirst();
            flush();
        }
    }

    //输出消息
    public void flush() {
        for (RemoteConsoleMessage message : messageBuffer)
            System.out.println(message.stringify());
    }

    //清空消息
    public void clear() {
        synchronized (RemoteConsoleDisplayer.class) {
            messageBuffer.clear();
            flush();
        }
    }

    //只展示指定设备
    public final void setTargetDevice(String targetDevice) {
        this.targetDevice = targetDevice;
    }

    //按项目过滤
    public final boolean showThisProject(String consoleProject, RemoteConsoleMessage message) {
        if (consoleProject.isEmpty())
            return true;
        return consoleProject.toLowerCase().contains(message.project.toLowerCase());
    }

    //按标签过滤
    public final boolean showThisTag(String consoleTag, RemoteConsoleMessage message) {
        if (consoleTag.isEmpty())
            return true;
        return consoleTag.toLowerCase().contains(message.tag.toLowerCase());
    }

    //按等级过滤
    public final boolean showThisLevel(int consoleLevel, RemoteConsoleMessage message) {
        return (consoleLevel & message.level) > 0;
    }

    //按设备过滤
    public boolean showThisDevice(RemoteConsoleMessage message) {
        if (targetDevice == null)
            return true;
        return targetDevice.equalsIgnoreCase(message.clientID);
    }
}

