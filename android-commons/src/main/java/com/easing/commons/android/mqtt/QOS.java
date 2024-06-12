package com.easing.commons.android.mqtt;

public class QOS {

    //最多一次，Sender只发送一次消息，Receiver收不到就算了
    public static final int AT_MOST_ONCE = 0;

    //最多一次，Sender发送多次消息，直到Receiver回复确认消息
    //如果Receiver的回复丢失了，则Sender会重复发送消息给Receiver
    public static final int AT_LEAST_ONCE = 1;

    //只有一次，Sender和Receiver进行双向确认，确认消息被收到且不重复
    public static final int EXACTLY_ONCE = 2;
}
