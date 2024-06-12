package com.easing.commons.android.mqtt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MQTT {

    //设置URL
    String url() default "";

    //通过路由框架自动获取URL
    String type() default "";

    //登录账号
    String username() default "";

    //登录密码
    String password() default "";

    //指定消息处理器
    Class<? extends MqttClient.MessageHandler> handler() default DefaultMqttHandler.class;

    //指定遗言构造器
    Class<? extends MqttClient.WillBuilder> willBuilder() default DefaultWillBuilder.class;
}

