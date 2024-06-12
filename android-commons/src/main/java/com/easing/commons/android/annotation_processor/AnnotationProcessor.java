package com.easing.commons.android.annotation_processor;

import com.easing.commons.android.annotation.Autowired;
import com.easing.commons.android.annotation.ViewBinding;
import com.easing.commons.android.mqtt.MQTT;
import com.easing.commons.android.redirection.Redirection;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.ui_component.Injection;
import com.easing.commons.android.ui_component.Items;
import com.easing.commons.android.ui_component.LayoutBinding;
import com.easing.commons.android.websocket.WebSocket;
import com.easing.commons.android.app.CommonActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

//注解处理器，用来处理自定义注解
@SuppressWarnings("all")
public class AnnotationProcessor {

    //处理自定义注解
    public static void init(CommonActivity activity) {
        //查找所有字段上的注解
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            annotations = sort(annotations);
            for (Annotation annotation : annotations)
                dispatchFieldAnnotation(activity, field, annotation);
        }
        //查找所有方法上的注解
        Method[] methods = activity.getClass().getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            annotations = sort(annotations);
            for (Annotation annotation : annotations)
                dispatchMethodAnnotation(activity, method, annotation);
        }
    }

    //处理自定义注解
    public static void init(Object object) {
        //查找所有字段上的注解
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            annotations = sort(annotations);
            for (Annotation annotation : annotations)
                dispatchFieldAnnotation(object, field, annotation);
        }
        //查找所有方法上的注解
        Method[] methods = object.getClass().getMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getDeclaredAnnotations();
            annotations = sort(annotations);
            for (Annotation annotation : annotations)
                dispatchMethodAnnotation(object, method, annotation);
        }
    }

    //注解排序
    //AnnotationProcessor.init不能直接遍历注解
    //应当按注解类型进行排序，因为有些注解需要先处理完，其它注解才能处理
    //比如ViewBinding之类的注解，应当先执行，View不为空之后，其它注解才能使用View对象
    public static Annotation[] sort(Annotation[] annotations) {
        List<Annotation> list = Collections.asList(annotations);
        Collections.sort(list, (l, r) -> annotationOrder(l) - annotationOrder(r));
        Annotation[] sortedArray = Collections.toArray(list, Annotation[]::new);
        return sortedArray;
    }

    //设置注解优先级
    public static int annotationOrder(Annotation annotation) {
        int order = 0;
        order++;
        if (annotation instanceof WebSocket) return order;
        order++;
        if (annotation instanceof MQTT) return order;
        order++;
        if (annotation instanceof ViewBinding) return order;
        order++;
        if (annotation instanceof Autowired) return order;
        order++;
        if (annotation instanceof LayoutBinding) return order;
        order++;
        if (annotation instanceof Items) return order;
        order++;
        if (annotation instanceof Injection) return order;
        order++;
        if (annotation instanceof Redirection) return order;
        return ++order;
    }

    //分发字段注解
    private static void dispatchFieldAnnotation(CommonActivity activity, Field field, Annotation annotation) {
        if (annotation instanceof Autowired)
            dispatchFieldAnnotation((Object) activity, field, annotation);
        else if (annotation instanceof Injection)
            AnnotationHandler.injectToTarget(activity, field, (Injection) annotation);
        else if (annotation instanceof LayoutBinding)
            AnnotationHandler.setLayout(activity, field, (LayoutBinding) annotation);
        else if (annotation instanceof Items)
            AnnotationHandler.addItems(activity, field, (Items) annotation);
        else if (annotation instanceof WebSocket)
            dispatchFieldAnnotation((Object) activity, field, (WebSocket) annotation);
        else if (annotation instanceof MQTT)
            dispatchFieldAnnotation((Object) activity, field, (MQTT) annotation);
    }

    //分发字段注解
    private static void dispatchFieldAnnotation(Object object, Field field, Annotation annotation) {
        if (annotation instanceof Autowired)
            AnnotationHandler.autowireInstance(object, field, (Autowired) annotation);
        else if (annotation instanceof WebSocket)
            AnnotationHandler.autowireWebSocket(object, field, (WebSocket) annotation);
        else if (annotation instanceof MQTT)
            AnnotationHandler.autowireMqtt(object, field, (MQTT) annotation);
    }

    //分发方法注解
    private static void dispatchMethodAnnotation(CommonActivity activity, Method method, Annotation annotation) {
    }

    //分发方法注解
    private static void dispatchMethodAnnotation(Object object, Method method, Annotation annotation) {
    }


}
