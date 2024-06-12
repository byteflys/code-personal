package com.easing.commons.android.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WebSocket {

    //设置URL
    String url() default "";

    //通过路由框架自动获取URL
    String type() default "";

    //通过配置类来管理URL
    Class urlConfiguration() default Object.class;

    //从配置类的指定字段获取URL
    String urlField() default "";

    //指定消息处理器
    Class<? extends WebSocketClient.MessageHandler> handler() default DefaultWebSocketHandler.class;
}

