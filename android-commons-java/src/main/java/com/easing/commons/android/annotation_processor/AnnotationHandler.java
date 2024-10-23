package com.easing.commons.android.annotation_processor;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.easing.commons.android.annotation.Autowired;
import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.mqtt.MQTT;
import com.easing.commons.android.mqtt.MqttClient;
import com.easing.commons.android.mqtt.MqttManager;
import com.easing.commons.android.ui_component.Injection;
import com.easing.commons.android.websocket.WebSocket;
import com.easing.commons.android.redirection.Redirection;
import com.easing.commons.android.redirection.ViewRouting;
import com.easing.commons.android.redirection.ViewRoutingRule;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.ui_component.ViewContainer;
import com.easing.commons.android.ui_component.Items;
import com.easing.commons.android.ui_component.LayoutBinding;
import com.easing.commons.android.app.CommonActivity;
import com.easing.commons.android.webmessage.WebMessageRouter;
import com.easing.commons.android.webmessage.WebMessageRoutingRule;
import com.easing.commons.android.websocket.WebSocketClient;
import com.easing.commons.android.websocket.WebSocketManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

@SuppressWarnings("all")
public class AnnotationHandler {

    //自动装载对象
    @SneakyThrows
    public static void autowireInstance(Object object, Field field, Autowired annotation) {
        Class clazz = annotation.clazz();
        Class[] args = annotation.args();
        Constructor constructor = Reflection.getConstructor(clazz, args);
        Object instance;
        if (args.length == 0)
            instance = constructor.newInstance();
        else {
            Object[] paramArray = new Object[args.length];
            paramArray[0] = object;
            instance = constructor.newInstance(paramArray);
        }
        field.setAccessible(true);
        field.set(object, instance);
    }

    //将UI组件注入指定布局容
    @SneakyThrows
    public static void injectToTarget(CommonActivity activity, Field field, Injection annotation) {
        String name = annotation.value();
        int id = findId(name);
        ViewGroup viewGroup = activity.findViewById(id);
        View view = (View) field.get(activity);
        viewGroup.addView(view);
    }

    //查找R.id资源
    @SneakyThrows
    public static int findId(String name) {
        String className = CommonApplication.ctx.getPackageName() + ".R$id";
        Class clazz = Class.forName(className);
        Field field = clazz.getField(name);
        field.setAccessible(true);
        Object fieldValue = field.get(clazz);
        return (int) fieldValue;
    }

    //查找R.drawable资源
    @SneakyThrows
    public static int findDrawable(String name) {
        String className = CommonApplication.ctx.getPackageName() + ".R$drawable";
        Class clazz = Class.forName(className);
        Field field = clazz.getField(name);
        field.setAccessible(true);
        Object fieldValue = field.get(clazz);
        return (int) fieldValue;
    }

    //查找R.layout资源
    @SneakyThrows
    public static int findLayout(String name) {
        String className = CommonApplication.ctx.getPackageName() + ".R$layout";
        Class clazz = Class.forName(className);
        Field field = clazz.getField(name);
        field.setAccessible(true);
        Object fieldValue = field.get(clazz);
        return (int) fieldValue;
    }

    //设置UI容器的布局
    @SneakyThrows
    public static void setLayout(CommonActivity activity, Field field, LayoutBinding annotation) {
        ViewContainer container = (ViewContainer) field.get(activity);
        container.layout(annotation.layout());
        container.column(annotation.column());
        container.width(annotation.width());
        container.height(annotation.height());
    }

    //添加子元素
    @SneakyThrows
    public static void addItems(CommonActivity activity, Field field, Items annotation) {
        ViewContainer container = (ViewContainer) field.get(activity);
        Class<? extends View>[] classes = annotation.value();
        View[] views = new View[classes.length];
        int index = 0;
        for (Class<? extends View> clazz : classes) {
            Constructor<? extends View> constructor = clazz.getConstructor(Context.class, AttributeSet.class);
            View view = constructor.newInstance(activity, null);
            views[index++] = view;
            container.add(view);
        }
    }

    //自动创建WebSocketClient
    @SneakyThrows
    public static void autowireWebSocket(Object object, Field field, WebSocket annotation) {
        //直接获取URL
        String url = annotation.url();
        //通过路由框架获取URL
        if (url.isEmpty() && !annotation.type().isEmpty()) {
            String type = annotation.type();
            WebMessageRoutingRule rule = WebMessageRouter.find(type);
            if (rule != null) url = rule.url;
        }
        //通过配置类获取URL
        if (url.isEmpty() && annotation.urlConfiguration() != Object.class) {
            Class configClass = annotation.urlConfiguration();
            String fieldName = annotation.urlField();
            Field urlField = configClass.getDeclaredField(fieldName);
            if (urlField != null)
                url = urlField.get(configClass).toString();
        }
        //创建WebSocket客户端
        WebSocketClient client = WebSocketManager.get(url);
        field.setAccessible(true);
        field.set(object, client);
        //设置消息处理器
        Class<? extends WebSocketClient.MessageHandler> handlerClass = annotation.handler();
        WebSocketClient.MessageHandler handler = handlerClass.newInstance();
        client.handler(handler);
    }

    //自动创建MQTT客户端
    @SneakyThrows
    public static void autowireMqtt(Object object, Field field, MQTT annotation) {
        //直接获取URL
        String url = annotation.url();
        String username = annotation.username();
        String password = annotation.password();
        Class<? extends MqttClient.MessageHandler> handlerClass = annotation.handler();
        Class<? extends MqttClient.WillBuilder> willBuilderClass = annotation.willBuilder();
        MqttClient.MessageHandler handler = handlerClass.newInstance();
        MqttClient.WillBuilder willBuilder = willBuilderClass.newInstance();
        //通过路由框架获取URL
        if (url.isEmpty() && !annotation.type().isEmpty()) {
            String type = annotation.type();
            WebMessageRoutingRule rule = WebMessageRouter.find(type);
            if (rule != null) {
                url = rule.url;
                username = rule.username;
                password = rule.password;
            }
        }
        //创建MQTT客户端
        MqttClient client = MqttManager.get(url, username, password, handler, willBuilder);
        field.setAccessible(true);
        field.set(object, client);
    }

    //从配置类中解析界面路由规则
    //这个方法建议在Application.onCreate中调用，应用一启动就加载规则
    public static ViewRoutingRule[] loadViewRoutingRule(Class configuration) {
        List<ViewRoutingRule> rules = new ArrayList();
        Field[] fields = configuration.getDeclaredFields();
        for (Field field : fields) {
            ViewRouting annotation = field.getAnnotation(ViewRouting.class);
            if (annotation != null) {
                String name = field.getName();
                Class<? extends Activity> target = annotation.target();
                boolean destroyCurrent = annotation.destroyCurrent();
                ViewRoutingRule rule = new ViewRoutingRule();
                rule.name = name;
                rule.target = target;
                rule.destroyCurrent = destroyCurrent;
                rules.add(rule);
            }
        }
        ViewRoutingRule[] ruleArray = Collections.toArray(rules, ViewRoutingRule[]::new);
        return ruleArray;
    }

    //查找View上的@Redirection注解
    @SneakyThrows
    public static String findRedirectionAnnotation(CommonActivity activity, View view) {
        Class clazz = activity.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Redirection annotation = field.getDeclaredAnnotation(Redirection.class);
            if (annotation == null) continue;
            if (view != field.get(activity)) continue;
            return annotation.ruleName();
        }
        return null;
    }

    //查找Activity上的@Redirection注解
    @SneakyThrows
    public static String findRedirectionAnnotation(CommonActivity activity) {
        Class clazz = activity.getClass();
        Redirection annotation = (Redirection) clazz.getDeclaredAnnotation(Redirection.class);
        if (annotation != null) return annotation.ruleName();
        return null;
    }

    //查找类注解
    @SneakyThrows
    public static <T extends Annotation> T findAnnotation(Class hostClass, Class<T> annotationClass) {
        T annotation = (T) hostClass.getDeclaredAnnotation(annotationClass);
        return annotation;
    }
}
