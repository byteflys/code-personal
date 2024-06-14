package com.easing.commons.android.webmessage;

import com.easing.commons.android.app.CommonApplication;
import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.JSON;
import com.easing.commons.android.data.Result;
import com.easing.commons.android.event.CommonEvents;
import com.easing.commons.android.event.EventBus;
import com.easing.commons.android.helper.exception.BizException;
import com.easing.commons.android.http.BodyParser;
import com.easing.commons.android.http.Postman;
import com.easing.commons.android.http.ResponseParser;
import com.easing.commons.android.manager.Resources;
import com.easing.commons.android.mqtt.MqttClient;
import com.easing.commons.android.mqtt.MqttManager;
import com.easing.commons.android.ui.dialog.TipBox;
import com.easing.commons.android.value.http.HttpMethod;
import com.easing.commons.android.websocket.WebSocketClient;
import com.easing.commons.android.websocket.WebSocketManager;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class WebMessageRouter {

    final static List<WebMessageRoutingRule> ruleList = new LinkedList();
    final static Map<String, WebMessageRoutingRule> ruleMap = new LinkedHashMap();

    public static WebMessageRoutingRule find(String type) {
        return ruleMap.get(type);
    }

    public static void init(WebMessageRoutingRule[] rules) {
        ruleList.clear();
        ruleMap.clear();
        for (WebMessageRoutingRule rule : rules) {
            rule.autoDecideProtocol();
            ruleList.add(rule);
            ruleMap.put(rule.eventType, rule);
        }
    }

    public static void add(WebMessageRoutingRule[] rules) {
        for (WebMessageRoutingRule rule : rules) {
            rule.autoDecideProtocol();
            WebMessageRouter.add(rule);
        }
    }

    public static void add(WebMessageRoutingRule rule) {
        WebMessageRoutingRule record = ruleMap.get(rule.eventType);
        if (record != null) ruleList.remove(record);
        ruleList.add(rule);
        ruleMap.put(rule.eventType, rule);
    }

    public static void initFromJson(String json) {
        WebMessageRoutingRule[] rules = JSON.parse(json, WebMessageRoutingRule[].class);
        WebMessageRouter.init(rules);
    }

    public static void initFromAsset(String path) {
        String json = Resources.readAssetText(path);
        WebMessageRoutingRule[] rules = JSON.parse(json, WebMessageRoutingRule[].class);
        WebMessageRouter.init(rules);
    }

    public static void addFromAsset(String path) {
        String json = Resources.readAssetText(path);
        WebMessageRoutingRule[] rules = JSON.parse(json, WebMessageRoutingRule[].class);
        WebMessageRouter.add(rules);
    }

    //根据消息类型，分发给不同的消息处理器处理
    public static void dispatch(WebMessage message) {
        dispatch(message, true);
    }

    //根据消息类型，分发给不同的消息处理器处理
    public static void dispatch(WebMessage message, boolean tipError) {
        dispatch(message, null, null, true);
    }

    //根据消息类型，分发给不同的消息处理器处理
    public static void dispatch(WebMessage message, ResponseParser responseParser, BodyParser bodyParser, boolean tipError) {
        try {
            if (!ruleMap.containsKey(message.eventType))
                throw BizException.of("WebMessageRoutingRule.dispatch 路由规则中找不到对应的消息类型 " + message.eventType);
            WebMessageRoutingRule rule = ruleMap.get(message.eventType).autoDecideProtocol();

            if (rule == null || rule.protocol == null)
                throw BizException.of("WebMessageRoutingRule.dispatch 找不到请求类型 " + message.eventType);

            switch (rule.protocol) {
                case WebProtocol.HTTP: {
                    Postman man = Postman.create();
                    man.url(message.url == null ? rule.url : message.url)
                            .method(HttpMethod.RAW_POST_JSON)
                            .head(message.head)
                            .stringifyWithNull(message.stringifyWithNull)
                            .rawJsonBody(message);
                    if (message.timeout != null) {
                        man.connectTimeOut(message.timeout);
                        man.readTimeOut(message.timeout);
                        man.writeTimeOut(message.timeout);
                    }
                    man.onCodeException((postman, call, e) -> {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 程序发生未知异常");
                        //发布程序异常消息
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.PROGRAM_ERROR;
                        error.errorMessage = "程序发生未知异常";
                        error.exception = e;
                        error.message = message;
                        onWebMessageFail(message);
                        EventBus.core.emit(message.eventType, error, message);
                        EventBus.core.emit(message.eventType, false, (String) null, message);
                    }).onIoException((postman, call, e) -> {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 服务器响应超时");
                        //发布网络超时消息
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.NETWORK_TIMEOUT;
                        error.errorMessage = "服务器响应超时";
                        error.exception = e;
                        error.message = message;
                        onWebMessageFail(message);
                        EventBus.core.emit(message.eventType, error, message);
                        EventBus.core.emit(message.eventType, false, (String) null, message);
                    }).onBizException((postman, call, e) -> {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 服务器发生未知异常");
                        //发布服务器异常消息
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.SERVER_ERROR;
                        error.errorMessage = "服务器发生未知异常";
                        error.exception = e;
                        error.message = message;
                        onWebMessageFail(message);
                        EventBus.core.emit(message.eventType, error, message);
                        EventBus.core.emit(message.eventType, false, (String) null, message);
                    }).onResponse((postman, call, response) -> {
                        int code = response.code();
                        //除了200系列的状态码，都视为失败
                        if (code >= 300) {
                            TipBox.tipOrPrint(tipError, rule.explain + " >> 服务器发生未知异常");
                            String body = response.body().string();
                            //发布服务器异常消息
                            WebMessageError error = new WebMessageError();
                            error.type = CommonEvents.SERVER_ERROR;
                            error.status = code;
                            error.code = code;
                            error.errorMessage = "服务器发生未知异常";
                            error.data = body;
                            error.message = message;
                            onWebMessageFail(message);
                            EventBus.core.emit(message.eventType, error, message);
                            EventBus.core.emit(message.eventType, false, body, message);
                        } else if (responseParser != null) {
                            Result result = responseParser.parse(response);
                            String body = String.valueOf(result.data);
                            if (result.success) {
                                WebMessage respMessage = JSON.parse(body, WebMessage.class);
                                onWebMessageSuccess(message);
                                EventBus.core.emit(message.eventType, body, message);
                                EventBus.core.emit(message.eventType, true, body, message);
                            } else {
                                WebMessageError error = new WebMessageError();
                                error.type = CommonEvents.SERVER_ERROR;
                                error.status = code;
                                error.code = code;
                                error.errorMessage = "服务器发生未知异常";
                                error.data = body;
                                error.message = message;
                                onWebMessageFail(message);
                                EventBus.core.emit(message.eventType, error, message);
                                EventBus.core.emit(message.eventType, false, body, message);
                            }
                        } else if (bodyParser != null) {
                            String body = response.body().string();
                            Result result = bodyParser.parse(body);
                            if (result.success) {
                                WebMessage respMessage = JSON.parse(body, WebMessage.class);
                                onWebMessageSuccess(message);
                                EventBus.core.emit(message.eventType, body, message);
                                EventBus.core.emit(message.eventType, true, body, message);
                            } else {
                                WebMessageError error = new WebMessageError();
                                error.type = CommonEvents.SERVER_ERROR;
                                error.status = code;
                                error.code = code;
                                error.errorMessage = "服务器发生未知异常";
                                error.data = body;
                                error.message = message;
                                onWebMessageFail(message);
                                EventBus.core.emit(message.eventType, error, message);
                                EventBus.core.emit(message.eventType, false, body, message);
                            }
                        } else {
                            String body = response.body().string();
                            WebMessage respMessage = JSON.parse(body, WebMessage.class);
                            onWebMessageSuccess(message);
                            EventBus.core.emit(message.eventType, body, message);
                            EventBus.core.emit(message.eventType, true, body, message);
                        }
                    }).execute(false);
                    break;
                }

                case WebProtocol.WEBSOCKET: {
                    WebSocketClient client = WebSocketManager.get(rule.url);
                    String result = client.sendMessage(message);
                    if (result == CommonEvents.SUCCESS) return;
                    //WebSocket已关闭
                    if (result == CommonEvents.CLIENT_CLOSED) {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 客户端连接已关闭");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.CLIENT_CLOSED;
                        error.errorMessage = "客户端连接已关闭";
                        error.message = message;
                        EventBus.core.emit(message.eventType, error, message);
                    }
                    //WebSocket未连接
                    else if (result == CommonEvents.CLIENT_UNCONNECTED) {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 连接服务器失败");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.CLIENT_UNCONNECTED;
                        error.errorMessage = "连接服务器失败";
                        error.message = message;
                        EventBus.core.emit(message.eventType, error, message);
                    }
                    //发布程序异常消息
                    else {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 程序发生未知异常");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.PROGRAM_ERROR;
                        error.errorMessage = "程序发生未知异常";
                        error.message = message;
                        EventBus.core.emit(message.eventType, error, message);
                    }
                    break;
                }

                case WebProtocol.MQTT: {
                    //使用统一网络协议，需要提前创建CLIENT，否则获取到的CLIENT为NULL
                    MqttClient client = MqttManager.get(rule.url);

                    if (client == null)
                        return;
                    client.publishInConcurrent(message.topic, message, result -> {

                        EventBus.core.emit(message.topic, message.eventType, result);
                        if (result == CommonEvents.SUCCESS) {
                            return;
                        }
                        //MQTT客户端已关闭
                        if (result == CommonEvents.CLIENT_CLOSED) {
                            TipBox.tipOrPrint(tipError, rule.explain + " >> 客户端连接已关闭");
                            WebMessageError error = new WebMessageError();
                            error.type = CommonEvents.CLIENT_CLOSED;
                            error.errorMessage = "客户端连接已关闭";
                            error.message = message;
                            EventBus.core.emit(message.eventType, error, message);
                        }
                        //MQTT未连接
                        else if (result == CommonEvents.CLIENT_UNCONNECTED) {
                            TipBox.tipOrPrint(tipError, rule.explain + " >> 连接服务器失败");
                            WebMessageError error = new WebMessageError();
                            error.type = CommonEvents.CLIENT_UNCONNECTED;
                            error.errorMessage = "连接服务器失败";
                            error.message = message;
                            EventBus.core.emit(message.eventType, error, message);
                        }
                        //发布程序异常消息
                        else {
                            TipBox.tipOrPrint(tipError, rule.explain + " >> 程序发生未知异常");
                            WebMessageError error = new WebMessageError();
                            error.type = CommonEvents.PROGRAM_ERROR;
                            error.errorMessage = "程序发生未知异常";
                            error.message = message;
                            EventBus.core.emit(message.eventType, error, message);
                        }
                    });
                    break;
                }
            }
        } catch (Throwable e) {
            CommonApplication.ctx.handleGlobalException(e);
        }
    }

    //阻塞式发布网络消息
    public static Result dispatchInBlocking(WebMessage message) {
        return dispatchInBlocking(message, null, null, true);
    }

    //阻塞式发布网络消息
    public static Result dispatchInBlocking(WebMessage message, ResponseParser responseParser, BodyParser bodyParser, boolean tipError) {
        final Result rst = Result.success();
        try {
            if (!ruleMap.containsKey(message.eventType))

                throw BizException.of("WebMessageRoutingRule.dispatch 路由规则中找不到对应的消息类型 " + message.eventType);
            WebMessageRoutingRule rule = ruleMap.get(message.eventType).autoDecideProtocol();

            if (rule == null) {
                rst.success = false;
                rst.code = Result.FAIL;
                rst.message = message.eventType + "_服务未配置";
                rst.error = CommonEvents.SERVICE_ERROR;
                onWebMessageFail(message);
                return rst;
            }

            switch (rule.protocol) {
                case WebProtocol.HTTP: {
                    Postman man = Postman.create();
                    man.url(message.url == null ? rule.url : message.url)
                            .method(HttpMethod.RAW_POST_JSON)
                            .head(message.head)
                            .stringifyWithNull(message.stringifyWithNull);
                    if (message.timeout != null) {
                        man.connectTimeOut(message.timeout);
                        man.readTimeOut(message.timeout);
                        man.writeTimeOut(message.timeout);
                    }
                    //默认将Message转成JOSN，作为body
                    //如果指定了纯文本格式的body，则使用该文本
                    if (message.rawTextBody == null)
                        man.rawJsonBody(message);
                    else
                        man.rawTextBody(message.rawTextBody);
                    man.onCodeException((postman, call, e) -> {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 程序发生未知异常");
                        //发布程序异常消息
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.PROGRAM_ERROR;
                        error.errorMessage = "程序发生未知异常";
                        error.exception = e;
                        error.message = message;
                        rst.success = false;
                        rst.code = Result.FAIL;
                        rst.message = "fail";
                        rst.error = error;
                    }).onIoException((postman, call, e) -> {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 服务器响应超时");
                        //发布网络超时消息
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.NETWORK_TIMEOUT;
                        error.errorMessage = "服务器响应超时";
                        error.exception = e;
                        error.message = message;
                        rst.success = false;
                        rst.code = Result.FAIL;
                        rst.message = "fail";
                        rst.error = error;
                    }).onBizException((postman, call, e) -> {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 服务器发生未知异常");
                        //发布服务器异常消息
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.SERVER_ERROR;
                        error.errorMessage = "服务器发生未知异常";
                        error.exception = e;
                        error.message = message;
                        rst.success = false;
                        rst.code = Result.FAIL;
                        rst.message = "fail";
                        rst.error = error;
                    }).onResponse((postman, call, response) -> {
                        int code = response.code();
                        //除了200系列的状态码，都视为失败
                        if (code >= 300) {
                            TipBox.tipOrPrint(tipError, rule.explain + " >> 服务器发生未知异常");
                            //发布服务器异常消息
                            WebMessageError error = new WebMessageError();
                            error.type = CommonEvents.SERVER_ERROR;
                            error.status = code;
                            error.code = code;
                            error.errorMessage = "服务器发生未知异常";
                            error.message = message;
                            rst.success = false;
                            rst.code = Result.FAIL;
                            rst.message = "fail";
                            rst.data = response.body().string();
                            rst.error = error;
                        } else if (responseParser != null) {
                            Result parseResult = responseParser.parse(response);
                            rst.success = parseResult.success;
                            rst.code = parseResult.code;
                            rst.message = parseResult.message;
                            rst.data = parseResult.data;
                            rst.error = parseResult.error;
                            rst.extraArgs.put("request", message);
                            rst.extraArgs.put("postman", postman);
                        } else if (bodyParser != null) {
                            String body = response.body().string();
                            Result parseResult = bodyParser.parse(body);
                            rst.success = parseResult.success;
                            rst.code = parseResult.code;
                            rst.message = parseResult.message;
                            rst.data = parseResult.data;
                            rst.error = parseResult.error;
                            rst.extraArgs.put("request", message);
                            rst.extraArgs.put("postman", postman);
                        } else {
                            String body = response.body().string();
                            rst.success = true;
                            rst.code = Result.SUCCESS;
                            rst.message = "success";
                            rst.data = body;
                            rst.extraArgs.put("request", message);
                            rst.extraArgs.put("postman", postman);
                        }
                    }).executeInBlocking();
                    break;
                }

                case WebProtocol.WEBSOCKET: {
                    WebSocketClient client = WebSocketManager.get(rule.url);
                    String result = client.sendMessage(message);
                    //发送成功
                    if (result == CommonEvents.SUCCESS) {
                        rst.success = true;
                        rst.message = "消息发送成功";
                    }
                    //WebSocket已关闭
                    if (result == CommonEvents.CLIENT_CLOSED) {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 客户端连接已关闭");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.CLIENT_CLOSED;
                        error.errorMessage = "客户端连接已关闭";
                        error.message = message;
                        rst.success = true;
                        rst.message = "客户端连接已关闭";
                        rst.data = error;
                    }
                    //WebSocket未连接
                    else if (result == CommonEvents.CLIENT_UNCONNECTED) {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 连接服务器失败");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.CLIENT_UNCONNECTED;
                        error.errorMessage = "连接服务器失败";
                        error.message = message;
                        rst.success = true;
                        rst.message = "连接服务器失败";
                        rst.data = error;
                    }
                    //发布程序异常消息
                    else {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 程序发生未知异常");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.PROGRAM_ERROR;
                        error.errorMessage = "程序发生未知异常";
                        error.message = message;
                        rst.success = true;
                        rst.message = "程序发生未知异常";
                        rst.data = error;
                    }
                    break;
                }

                case WebProtocol.MQTT: {
                    //使用统一网络协议，需要提前创建CLIENT，否则获取到的CLIENT为NULL
                    MqttClient client = MqttManager.get(rule.url);
                    String result = client.publish(message.topic, message);
                    //发送成功
                    if (result == CommonEvents.SUCCESS) {
                        rst.success = true;
                        rst.message = "消息发送成功";
                    }
                    //MQTT客户端已关闭
                    if (result == CommonEvents.CLIENT_CLOSED) {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 客户端连接已关闭");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.CLIENT_CLOSED;
                        error.errorMessage = "客户端连接已关闭";
                        error.message = message;
                        rst.success = true;
                        rst.message = "客户端连接已关闭";
                        rst.data = error;
                    }
                    //MQTT未连接
                    else if (result == CommonEvents.CLIENT_UNCONNECTED) {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 连接服务器失败");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.CLIENT_UNCONNECTED;
                        error.errorMessage = "连接服务器失败";
                        error.message = message;
                        rst.success = true;
                        rst.message = "连接服务器失败";
                        rst.data = error;
                    }
                    //发布程序异常消息
                    else {
                        TipBox.tipOrPrint(tipError, rule.explain + " >> 程序发生未知异常");
                        WebMessageError error = new WebMessageError();
                        error.type = CommonEvents.PROGRAM_ERROR;
                        error.errorMessage = "程序发生未知异常";
                        error.message = message;
                        rst.success = true;
                        rst.message = "程序发生未知异常";
                        rst.data = error;
                    }
                    break;
                }
            }

            if (rst.success)
                onWebMessageSuccess(message);
            else
                onWebMessageFail(message);
            return rst;
        } catch (Throwable e) {
            rst.success = false;
            rst.code = Result.FAIL;
            rst.message = "程序发生未知异常";
            rst.error = CommonEvents.PROGRAM_ERROR;
            onWebMessageFail(message);
            CommonApplication.ctx.handleGlobalException(e);
            return rst;
        }
    }

    protected static void onWebMessageSuccess(WebMessage request) {
        Console.infoWithTag("onWebMessageSuccess", request.eventType, request.findUrl());
    }

    protected static void onWebMessageFail(WebMessage request) {
        Console.infoWithTag("onWebMessageFail", request.eventType, request.findUrl());
    }

}
