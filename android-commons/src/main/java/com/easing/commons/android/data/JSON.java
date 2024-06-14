package com.easing.commons.android.data;

import com.easing.commons.android.code.Console;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.SneakyThrows;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;

@SuppressWarnings("all")
public class JSON {

    public static final Gson gson;
    public static final Gson nullGson;
    public static final Gson prettyGson;

    static {

        //创建默认的GSON处理器
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();

        //创建保留NULL字段的GSON处理器
        GsonBuilder builderWithNull = new GsonBuilder();
        nullGson = builderWithNull.serializeNulls().create();

        //创建带换行缩进等美化效果的GSON处理器
        GsonBuilder beautyGsonBuilder = new GsonBuilder();
        prettyGson = beautyGsonBuilder.setPrettyPrinting().create();
    }

    //对象转JSON字符串
    public static String stringify(Object obj) {
        return stringify(obj, false);
    }

    //对象转JSON字符串，保留Null
    public static String stringify(Object obj, boolean keepNull) {
        if (keepNull)
            return nullGson.toJson(obj);
        else
            return gson.toJson(obj);
    }

    //对象转JSON字符串
    public static String stringifyBeautifully(Object obj) {
        return prettyGson.toJson(obj);
    }

    //JSON字符串转对象
    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (Exception e) {
            Console.error("JSON transfrom to " + clazz.getSimpleName() + " Fail");
            Console.error(e);
            return null;
        }
    }

    //JSON字符串转对象
    //带泛型参数的T类，需要使用带Type的接口，因为Class中的泛型信息编译后会被抹除
    public static <T> T parse(String json, Type type) {
        try {
            return (T) gson.fromJson(json, type);
        } catch (Exception e) {
            Console.error(e);
            return null;
        }
    }

    //根据Map中的参数生成类对象
    @SneakyThrows
    public static <T> T parse(Map<String, Object> paramMap, Class<T> clazz) {
        try {
            String json = JSON.stringify(paramMap);
            return JSON.parse(json, clazz);
        } catch (Exception e) {
            Console.error(e);
            return null;
        }
    }

    public static Map toMap(String json) {
        Map map = JSON.parse(json, Map.class);
        return map;
    }

    public static Map toMap(Object object) {
        String json = JSON.stringify(object);
        Map map = JSON.parse(json, Map.class);
        return map;
    }

    @SneakyThrows
    public static String getString(String json, String key) {
        return new JSONObject(json).getString(key);
    }

    @SneakyThrows
    public static int getInt(String json, String key) {
        return new JSONObject(json).getInt(key);
    }

    @SneakyThrows
    public static Object get(JSONObject json, String key) {
        if (json.has(key))
            return json.get(key);
        return null;
    }

    @SneakyThrows
    public static String getString(JSONObject json, String key) {
        if (json.has(key))
            return json.getString(key);
        return null;
    }

    @SneakyThrows
    public static Integer getInt(JSONObject json, String key) {
        if (json.has(key))
            return json.getInt(key);
        return null;
    }

    @SneakyThrows
    public static Double getDouble(JSONObject json, String key) {
        if (json.has(key))
            return json.getDouble(key);
        return null;
    }

    @SneakyThrows
    public static Boolean getBoolean(JSONObject json, String key) {
        if (json.has(key))
            return json.getBoolean(key);
        return null;
    }

    @SneakyThrows
    public static JSONObject newJsonObject() {
        return new JSONObject();
    }

    @SneakyThrows
    public static JSONObject getJsonObject(JSONObject json, String key) {
        return json.getJSONObject(key);
    }

    @SneakyThrows
    public static JSONArray getJsonArray(JSONObject json, String key) {
        return json.getJSONArray(key);
    }

    //生成JSONObject
    @SneakyThrows
    public static JSONObject toJsonObject(String json) {
        return new JSONObject(json);
    }

    //转为JSONObject
    @SneakyThrows
    public static JSONObject toJsonObject(Map map) {
        String json = JSON.stringify(map);
        return new JSONObject(json);
    }

    //生成JSONArray
    @SneakyThrows
    public static JSONArray toJsonArray(String json) {
        return new JSONArray(json);
    }

    //判断字符串是不是JSON
    public static boolean isJson(String json) {
        return isJsonObject(json) || isJsonArray(json);
    }

    //判断JSON字符串是不是Object
    public static boolean isJsonObject(String json) {
        try {
            new JSONObject(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //判断JSON字符串是不是Array
    public static boolean isJsonArray(String json) {
        try {
            new JSONArray(json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //JSON字符串美化
    @SneakyThrows
    public static String beautify(String json) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson beautyGson = builder.create();
        if (JSON.isJsonObject(json))
            return beautyGson.toJson(gson.fromJson(json, Map.class));
        if (JSON.isJsonArray(json))
            return beautyGson.toJson(gson.fromJson(json, Map[].class));
        return json;
    }

    //深拷贝复制一个新对象
    //注意，被拷贝的对象不能出现A对象包含B对象，B对象又包含A对象的情况
    //因为JSON只是单纯地根据字段名来创建新对象，无法理解对象间的引用关系
    //对象间的循环引用，会导致JSON无限创建对象，直到引发爆栈异常
    public static <T> T copy(T obj) {
        String json = JSON.stringify(obj);
        T newObj = (T) JSON.parse(json, obj.getClass());
        return newObj;
    }

    //深拷贝复制一个新对象
    //带泛型参数的T类，需要使用带Type的接口，因为Class中的泛型信息编译后会被抹除
    public static <T> T copy(T obj, Type type) {
        String json = JSON.stringify(obj);
        T newObj = (T) JSON.parse(json, type);
        return newObj;
    }
}
