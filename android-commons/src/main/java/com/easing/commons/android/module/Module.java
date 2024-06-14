package com.easing.commons.android.module;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.manager.Network;
import com.easing.commons.android.thread.Threads;
import com.easing.commons.android.thread.WorkThread;
import com.easing.commons.android.value.http.HttpMethod;

import java.util.LinkedHashMap;
import java.util.Map;

//模块基类，同时也是模块管理器
@SuppressWarnings("all")
public abstract class Module {

    protected boolean activated = false;

    protected String connectivityTestUrl = "https://www.baidu.com";

    protected boolean isNetworkConnected = false;

    //判断是否具有模块访问权限
    public boolean hasPermission() {
        return true;
    }

    //注册模块
    final public Module register() {
        if (!activated) {
            EventBus.core.subscribe(this);
            activated = true;
        }
        return this;
    }

    //解注册模块
    final public Module unregister() {
        activated = false;
        EventBus.core.unsubscribe(this);
        return this;
    }

    //模块是否激活
    final public boolean activated() {
        return activated;
    }

    //模块启动
    public Module launch() {
        Console.info("模块启动 " + getClass().getSimpleName());
        return this;
    }

    //模块销毁
    public Module destroy() {
        Console.info("模块销毁 " + getClass().getSimpleName());
        unregister();
        moduleMap.remove(getClass());
        return this;
    }

    //设置用于检测连通性的服务器地址
    public Module connectivityTestUrl(String connectivityTestUrl) {
        this.connectivityTestUrl = connectivityTestUrl;
        return this;
    }

    //服务器连通性，以测试地址的连通性作为整个模块的网络连通性
    public boolean isNetworkConnected() {
        return isNetworkConnected;
    }

    //启动线程，定期检测网络连通性
    public void startConnectivityTest() {
        WorkThread.postByLoop("网络连通性线程", () -> {
            isNetworkConnected = Network.testNetworkByServiceUrl(connectivityTestUrl, HttpMethod.GET);
            if (!activated)
                BizException.THREAD_NORMAL_EXIT.post();
            Threads.sleep(1000 * 30);
        });
    }

    //单例管理
    protected static final Map<Class, Module> moduleMap = new LinkedHashMap();

    //获取实例
    public static <T extends Module> T get(Class<T> clazz) {
        if (moduleMap.containsKey(clazz))
            return (T) moduleMap.get(clazz);
        T module = Reflection.newInstance(clazz);
        moduleMap.put(clazz, module);
        return module;
    }

    //获取实例
    public static <T extends Module> T get(String className) {
        Class<T> clazz = Reflection.findClass(className);
        return get(clazz);
    }
}

