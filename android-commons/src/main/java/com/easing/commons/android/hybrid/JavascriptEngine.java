package com.easing.commons.android.hybrid;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class JavascriptEngine {

    private JavascriptEngine() {
    }

    //开启一个新的ScriptEngine，并加载Javascript代码，同时向引擎注入一个JavascriptBridge对象
    //JavascriptBridge用于Java和JavaScript之间的互调，不能用作参数和返回值
    //在Javascript中，直接使用java.xxx()格式的代码，就可以调用JavascriptBridge中的Java方法

    //Java返回给Javascript的对象，以及ScriptEngine向Javascript中添加的对象，都是真实的原Java对象
    //不过建议所有地方都使用序列化后的JOSN字符串进行交互，由于对象可以相互引用，在序列化时可能出现死循环的现象

    @SneakyThrows
    public static void createSession(String script, JavascriptBridge bridge) {
        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
        bridge.engine(engine);
        engine.put("java", bridge);
        if (script != null)
            engine.eval(script);
    }


}

