package com.easing.commons.android.event;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.helper.callback.Action;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.thread.MainThread;
import com.easing.commons.android.thread.WorkThread;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import lombok.Synchronized;

@SuppressWarnings("all")
public class EventBus {

    public static final EventBus core = new EventBus("default");
    private static final Map<String, EventBus> instanceMap = new LinkedHashMap();

    protected final String name;
    protected final List<SubscribeInfo> subscribeInfos = Collections.sychronizedList();

    private EventBus(String name) {
        this.name = name;
    }

    //获取或创建一个新的事件管理器
    public static EventBus get(String name) {
        if (!instanceMap.containsKey(name))
            instanceMap.put(name, new EventBus(name));
        return instanceMap.get(name);
    }

    //移除事件管理器
    public static void remove(String name) {
        instanceMap.remove(name);
    }

    //订阅事件
    public void subscribe(Object object) {
        if (isRedundantSubscription(object)) return;
        //查找所有方法上的注解
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            for (Annotation annotation : annotations)
                if (annotation instanceof OnEvent)
                    addSubscribeInfo(object, method, (OnEvent) annotation);
        }
    }

    //判断是否重复订阅
    private boolean isRedundantSubscription(Object object) {
        SubscribeInfo subscriber = Collections.find(subscribeInfos, subscribeInfo -> subscribeInfo.invokeObject == object);
        return subscriber != null;
    }

    //添加订阅消息
    private void addSubscribeInfo(Object object, Method method, OnEvent annotation) {
        Class[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++)
            parameterTypes[i] = Reflection.wrap(parameterTypes[i]);
        if (parameterTypes.length < 1)
            throw new RuntimeException("subscriber method missed the event type parameter");
        if (parameterTypes[0] != String.class)
            throw new RuntimeException("subscriber method missed the event type parameter");
        SubscribeInfo subscribeInfo = new SubscribeInfo();
        subscribeInfo.invokeObject = object;
        subscribeInfo.invokeMethod = method;
        subscribeInfo.eventTypes = annotation.type();
        subscribeInfo.paramTypes = parameterTypes;
        subscribeInfo.threadMode = annotation.thread();
        subscribeInfos.add(subscribeInfo);
    }

    //解除订阅
    public void unsubscribe(Object object) {
        synchronized (subscribeInfos) {
            List<SubscribeInfo> subscribers = Collections.filter(subscribeInfos, subscribeInfo -> subscribeInfo.invokeObject == object);
            for (SubscribeInfo subscriber : subscribers)
                subscribeInfos.remove(subscriber);
        }
    }

    //发射一个事件，携带若干参数
    @Synchronized
    @SneakyThrows
    public void emit(String type, Object... args) {
        List<SubscribeInfo> targetInfos = Collections.filter(subscribeInfos, info -> {
            if (info.paramTypes.length != args.length + 1)
                return false;
            for (int i = 0; i < args.length; i++)
                if (args[i] != null)
                    if (!info.paramTypes[i + 1].isAssignableFrom(args[i].getClass()))
                        return false;
            if (info.eventTypes.length != 0)
                if (!Collections.contains(info.eventTypes, type))
                    return false;
            return true;
        });
        for (SubscribeInfo info : targetInfos) {
            Method invokeMethod = info.invokeMethod;
            Object invokeObject = info.invokeObject;
            ThreadMode threadMode = info.threadMode;
            Object[] parameters = Collections.merge(type, args);
            invokeByThreadMode(threadMode, () -> {
                try {
                    invokeMethod.invoke(info.invokeObject, parameters);
                } catch (InvocationTargetException e) {
                    Throwable targetException = e.getTargetException();
                    Console.error("EventBus.emit", "Method Invoke Error");
                    Console.error(targetException);
                }
            });
        }
    }

    //发射一个事件，通过Event包装携带的参数
    public void emit(Event event) {
        emit(event.type, event);
    }

    //延时发射事件
    @SneakyThrows
    public void emitLater(int ms, String type, Object... args) {
        WorkThread.postLater(() -> emit(type, args), ms);
    }

    //延时发射事件
    @SneakyThrows
    public void emitLater(int ms, Event event) {
        WorkThread.postLater(() -> emit(event), ms);
    }

    //投递给对应线程执行
    private void invokeByThreadMode(ThreadMode threadMode, Action action) {
        switch (threadMode) {
            case CURRENT_THREAD:
                action.runAndPostException();
                break;

            case WORK_THREAD:
                WorkThread.post(action::runAndPostException);
                break;

            case MAIN_THREAD:
                MainThread.post(action::runAndPostException);
                break;
        }
    }

}
