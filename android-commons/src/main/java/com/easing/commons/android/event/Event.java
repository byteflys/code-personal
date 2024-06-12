package com.easing.commons.android.event;

@SuppressWarnings("all")
public class Event {

    public String type;
    public Object data;

    //携带额外数据
    public Object[] args;

    //已经被消费，则其它接收者不再处理
    public boolean consumed;

    //指定某个类接收
    public Class targetClass;

    public static Event create(String type) {
        Event event = new Event();
        event.type = type;
        return event;
    }

    public static Event create(String type, Object data) {
        Event event = new Event();
        event.type = type;
        event.data = data;
        return event;
    }

    //设置data
    public Event data(Object data) {
        this.data = data;
        return this;
    }

    //设置额外参数
    public Event args(Object... args) {
        this.args = args;
        return this;
    }

    //消费该事件，不再继续传递
    public Event consume() {
        this.consumed = true;
        return this;
    }

    //只发送给指定类
    public Event targetClass(Class targetClass) {
        this.targetClass = targetClass;
        return this;
    }

    //获取携带数据
    public <T> T data() {
        return (T) data;
    }

    //获取额外参数
    public <T> T arg(int index) {
        return (T) args[index];
    }

    //发送当前事件
    public Event emit() {
        EventBus.core.emit(this);
        return this;
    }

}

