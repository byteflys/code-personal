package com.easing.commons.android.webmessage;

import com.easing.commons.android.annotation_processor.Reflection;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.data.Jsonable;
import com.easing.commons.android.data.Result;
import com.easing.commons.android.format.TextBuilder;
import com.easing.commons.android.format.Texts;
import com.easing.commons.android.http.BodyParser;
import com.easing.commons.android.http.ResponseParser;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("all")
public class WebMessage<T> implements Jsonable {

    /**
     * 分页查询每页的数量
     */
    public static int pageSize = 20;
    public static int maxPageSize = 10000;
    public Object id = Texts.random();
    public transient String eventType;
    public String server;

    public Integer code;
    public String message;
    public T data;

    public String from;
    public String to;
    public String replyTo;
    public String[] deliverRoute;
    public String mac;
    public String password;

    public String rawTextBody;

    public String url = null;

    public Long timeout = 15000L;

    //JSON序列化时，是否保留Null字段
    public transient boolean stringifyWithNull = false;

    //用于解析Http结果
    public transient ResponseParser responseParser;
    //用于解析WebSocket/MQTT等结果
    public transient BodyParser bodyParser;

    //用于HTTP协议
    public transient final Map<String, Object> head = new LinkedHashMap();

    //用于MQTT协议
    public String topic;

    //转字符串打印
    public String string() {
        String json = JSON.stringifyBeautifully(this);
        WebMessageRoutingRule rule = WebMessageRouter.find(eventType);
        if (rule == null)
            return json;
        TextBuilder builder = TextBuilder.create();
        builder.append("Request-Name: ").append(rule.explain).endLine();
        builder.append("Request-Url: ").append(rule.url).endLine();
        builder.append("Request-Header: ").append(JSON.stringifyBeautifully(head)).endLine();
        builder.append("Request-Body: ").append(json).endLine();
        String string = builder.build();
        return string;
    }

    @Override
    public String toString() {
        return string();
    }

    public WebMessage url(String url) {
        this.url = url;
        return this;
    }

    public <S> S data() {
        return (S) data;
    }

    public WebMessage rawJson(String rawJson) {
        this.rawTextBody = rawJson;
        return this;
    }

    public WebMessage responseParser(Class<? extends ResponseParser> parserClass) {
        this.responseParser = Reflection.newInstance(parserClass);
        return this;
    }

    public WebMessage bodyParser(Class<? extends BodyParser> parserClass) {
        this.bodyParser = Reflection.newInstance(parserClass);
        return this;
    }

    public WebMessage dispatch() {
        WebMessageRouter.dispatch(this, responseParser, bodyParser, false);
        return this;
    }

    public WebMessage dispatch(boolean tipError) {
        WebMessageRouter.dispatch(this, responseParser, bodyParser, tipError);
        return this;
    }

    public Result dispatchInBlocking() {
        return WebMessageRouter.dispatchInBlocking(this, responseParser, bodyParser, false);
    }

    public Result dispatchInBlocking(boolean tipError) {
        return WebMessageRouter.dispatchInBlocking(this, responseParser, bodyParser, tipError);
    }

    //查找服务地址
    public String findUrl() {
        if (url != null)
            return url;
        WebMessageRoutingRule rule = WebMessageRouter.find(eventType);
        if (rule != null)
            return rule.url;
        return null;
    }

    //设置超时
    public WebMessage<T> timeout(Long timeout) {
        this.timeout = timeout;
        return this;
    }

    public WebMessage<T> stringifyWithNull(boolean keepNull) {
        this.stringifyWithNull = keepNull;
        return this;
    }
}
